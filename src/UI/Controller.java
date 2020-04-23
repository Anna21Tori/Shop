package UI;

import UI.cart.CartController;
import UI.shoppingTransaction.ShoppingTransaction;
import UI.model.ProductsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private FulfillmentCenterContainer fulfillmentCenterContainer;
    private ObservableList<ProductsModel> cartModels;
    private FilteredList<ProductsModel> productsFiltered;
    private SortedList<ProductsModel> productsSorted;
    private ObservableList<ProductsModel> productsModels;

    @FXML
    private ComboBox<String> fulfillmentComboBox;
    @FXML
    private ComboBox<String> sortedComboBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button cart;
    @FXML
    private TableView<ProductsModel> table;
    @FXML
    public TableColumn<ProductsModel, String> colName;
    @FXML
    public TableColumn<ProductsModel, String> colFulfillmentCenterName;
    @FXML
    public TableColumn<ProductsModel, Integer> colAmount;
    @FXML
    public TableColumn<ProductsModel, Double> colPrice;
    @FXML
    public TableColumn<ProductsModel, Void> colActive;
    @FXML
    public void filter() {
        if(fulfillmentComboBox.getValue().equals("All"))
            productsFiltered.setPredicate(p -> p.getName().toLowerCase().contains(nameTextField.getText().toLowerCase().trim()));
        else
            productsFiltered.setPredicate(p -> p.getName().toLowerCase().contains(nameTextField.getText().toLowerCase().trim()) && fulfillmentComboBox.getValue().equals(p.getFulfillmentCenterName()));
        switch(sortedComboBox.getValue()){
            case "Name":
                table.getSortOrder().setAll(colName);
                break;
            case "Amount":
                table.getSortOrder().setAll(colAmount);
                break;
            case "Price":
                table.getSortOrder().setAll(colPrice);
                break;
        }
    }
    private void buy() throws IOException{
        for(ProductsModel model : cartModels){
            if(fulfillmentCenterContainer.buy(model.getId(), model.getName(), model.getFulfillmentCenterName(),model.getAmountCart()))
                model.setIsSold(true);
        }
        viewPurchasedProducts();
        upData();
        table.refresh();
        fulfillmentCenterContainer.clearCart();
        serializedData("Products.ser");
    }
    @FXML
    private void openCart(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cart/style.fxml"));
        Parent parent = fxmlLoader.load();
        CartController cartController = fxmlLoader.getController();
        cartController.setAppMainObservableList(cartModels);
        Scene scene = new Scene(parent, 700, 350);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        upDataAmountCart();
        table.refresh();
        fulfillmentCenterContainer.clearCart();
        for(ProductsModel product: cartModels)
            fulfillmentCenterContainer.addCart(product.getId(), product.getName(), product.getFulfillmentCenterName(), product.getAmountCart());
        serializedData("Products.ser");
        if(cartController.getIsBuyClicked())
            buy();

    }
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fulfillmentCenterContainer = null;
        deserializedData("Products.ser");
        fulfillmentCenterContainer.initSaleState();
        ObservableList<String> fulfillmentOptions = FXCollections.observableArrayList("All");
        ObservableList<String> sortedOptions = FXCollections.observableArrayList("Name","Amount", "Price");
        for (FulfillmentCenter item : fulfillmentCenterContainer.getListCenter())
            fulfillmentOptions.add(item.getName());
        initComboBox(fulfillmentComboBox, fulfillmentOptions);
        initComboBox(sortedComboBox, sortedOptions);
        initTableProducts();
        upDataAmountCart();
        filter();
    }
    private void initComboBox(ComboBox<String> comboBox, ObservableList<String> options){
        comboBox.getItems().setAll(options);
        comboBox.getSelectionModel().selectFirst();
    }
    private void initTableProducts() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFulfillmentCenterName.setCellValueFactory(new PropertyValueFactory<>("fulfillmentCenterName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("currentAmount"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        Callback<TableColumn<ProductsModel, Void>, TableCell<ProductsModel, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ProductsModel, Void> call(final TableColumn<ProductsModel, Void> param) {
                final TableCell<ProductsModel, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("+");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            addCart(getTableView().getItems().get(getIndex()));
                            table.refresh();
                            upDataAmountCart();
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                            if(getTableView().getItems().get(getIndex()).getIsAddedCart() || getTableView().getItems().get(getIndex()).getAmount()==0)
                                btn.setDisable(true);
                            else
                                btn.setDisable(false);
                        }
                    }
                };
                return cell;
            }
        };
        colActive.setCellFactory(cellFactory);
        setToolTip(table);
        initData();
        table.setItems(productsSorted);
    }

    private void initData(){
        productsModels = FXCollections.observableArrayList();
        cartModels = FXCollections.observableArrayList();
        for (Item item : fulfillmentCenterContainer.filter("", "All"))
                productsModels.add(new ProductsModel(item.getName(), item.getMass(), item.getCurrentAmount(), item.getSaleState(), item.getPrice(), item.getId(), item.getFulfilmentCenterName()));
        productsFiltered = new FilteredList(productsModels, p -> true);
        productsSorted = new SortedList<>(productsFiltered);
        productsSorted.comparatorProperty().bind(table.comparatorProperty());
        for(SoldItem product: fulfillmentCenterContainer.getCart()){
            for(ProductsModel model:productsModels) {
                if (product.getProduct().getName().equals(model.getName()) && product.getProduct().getId() == model.getId()) {
                    cartModels.add(model);
                    model.setAmountCart(product.getAmount());
                    model.setIsAddedCart(true);
                }
            }
        }
        upDataAmountCart();
    }
    private void upData(){
        cartModels.clear();
        productsModels.clear();
        for (Item item : fulfillmentCenterContainer.filter("", "All"))
            productsModels.add(new ProductsModel(item.getName(), item.getMass(), item.getCurrentAmount(), item.getSaleState(), item.getPrice(), item.getId(), item.getFulfilmentCenterName()));
        upDataAmountCart();
        filter();
    }
    private void addCart(ProductsModel product){
        product.setAmountCart(1);
        product.setIsAddedCart(true);
        cartModels.add(product);
        fulfillmentCenterContainer.addCart(product.getId(), product.getName(), product.getFulfillmentCenterName(), product.getAmountCart());
        serializedData("Products.ser");
    }

    private void setToolTip(TableView<ProductsModel> tableView){
        tableView.setRowFactory(table -> new TableRow<>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(ProductsModel product, boolean empty) {
                super.updateItem(product, empty);
                if (product == null) {
                    setTooltip(null);
                } else {
                    FulfillmentCenter center = fulfillmentCenterContainer.getFulfillmentCenter(product.getFulfillmentCenterName());
                    tooltip.setText("More details:\n"+product.getToolTip()+"\n\n Warehouse:\n"+center.summarise());
                    setTooltip(tooltip);
                }
            }
        });
    }
    private void viewPurchasedProducts() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("shoppingTransaction/style.fxml"));
        Parent parent = fxmlLoader.load();
        ShoppingTransaction shoppingTransaction = fxmlLoader.getController();
        shoppingTransaction.setAppMainObservableList(cartModels);
        Scene scene = new Scene(parent, 500, 500);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void upDataAmountCart(){
        int amount = 0;
        for(ProductsModel product: cartModels)
            amount+=product.getAmountCart();
        cart.setText("Cart ("+amount+" products)");
    }

    private void serializedData(String path){
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(fulfillmentCenterContainer);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in" +path);
        }  catch(IOException ex)
        {
            fulfillmentCenterContainer = GenerateData.getFulfillmentCenterContainer();
            System.out.println("IOException is caught");
        }
    }

    private void deserializedData(String path){
        try
        {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);
            fulfillmentCenterContainer = (FulfillmentCenterContainer) in.readObject();
            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        }catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
    }

}
