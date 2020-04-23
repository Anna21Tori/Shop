package UI.cart;

import UI.model.ProductsModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CartController {
    ObservableList<ProductsModel> products;
    private Boolean isBuyClicked;
    @FXML
    private TableView<ProductsModel> table;
    @FXML
    public TableColumn<ProductsModel, String> colName;
    @FXML
    public TableColumn<ProductsModel, String> colFulfillmentCenterName;
    @FXML
    public TableColumn<ProductsModel, Double> colPrice;
    @FXML
    public TableColumn<ProductsModel, Void> colAction;
    @FXML
    private Button buyBtn;
    @FXML
    private void buy(ActionEvent event){
        isBuyClicked = true;
        closeStage(event);
    }
    @FXML
    private void close(ActionEvent event){
        closeStage(event);
    }
    public void setAppMainObservableList(ObservableList<ProductsModel> products) {
        this.products = products;
        this.isBuyClicked = false;
        initTable(table, colName, colFulfillmentCenterName, colPrice, colAction, products);
        if(products.size()==0)
            buyBtn.setDisable(true);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void initTable(TableView<ProductsModel> table, TableColumn<ProductsModel, String> name, TableColumn<ProductsModel, String> fulfillmentCenter, TableColumn<ProductsModel, Double> price, TableColumn<ProductsModel, Void> action, ObservableList<ProductsModel> list){
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        fulfillmentCenter.setCellValueFactory(new PropertyValueFactory<>("fulfillmentCenterName"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        Callback<TableColumn<ProductsModel, Void>, TableCell<ProductsModel, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ProductsModel, Void> call(final TableColumn<ProductsModel, Void> param) {
                final TableCell<ProductsModel, Void> cell = new TableCell<>() {
                    private final Spinner spinner = new Spinner<Integer>(1,1000000,1);
                    {
                        spinner.valueProperty().addListener(
                                (obs, oldValue, newValue) -> {
                                    if(getTableView().getItems().get(getIndex()).getAmountCart() != (int) newValue) {
                                        if (getTableView().getItems().get(getIndex()).getAmount() < (int) newValue)
                                            spinner.getValueFactory().setValue(oldValue);
                                        else if ((int) oldValue < (int) newValue)
                                            getTableView().getItems().get(getIndex()).setAmountCart(1);
                                        else if ((int) oldValue > (int) newValue)
                                            getTableView().getItems().get(getIndex()).setAmountCart(-1);
                                    }
                                }
                        );
                    }
                    private final Button btn = new Button("X");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            removeProduct(getTableView().getItems().get(getIndex()), getTableRow().getIndex());
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            spinner.getValueFactory().setValue(getTableView().getItems().get(getIndex()).getAmountCart());
                            setGraphic(new HBox(spinner, btn));
                        }
                    }
                };
                return cell;
            }
        };
        action.setCellFactory(cellFactory);
        table.setItems(products);
        table.setItems(list);
    }

    private void removeProduct(ProductsModel product, int index){
        product.setIsAddedCart(false);
        product.setAmountCart(-product.getAmountCart());
        products.remove(index);
        if(products.size()==0)
            buyBtn.setDisable(true);
    }

    public boolean getIsBuyClicked(){
        return isBuyClicked;
    }
}
