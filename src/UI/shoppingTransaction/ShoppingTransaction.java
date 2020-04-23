package UI.shoppingTransaction;

import UI.Adnotacje.DatabaseField;
import UI.model.ProductsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class ShoppingTransaction {

    private static DecimalFormat df = new DecimalFormat("0.00");
    private ObservableList<ProductsModel> purchasedList;
    private ObservableList<ProductsModel> rejectedList;

    @FXML
    private TableView<ProductsModel> purchasedTable;
    @FXML
    public TableColumn<ProductsModel, String> colNameP;
    @FXML
    public TableColumn<ProductsModel, String> colFulfillmentCenterP;
    @FXML
    public TableColumn<ProductsModel, Integer> colAmountP;
    @FXML
    public TableColumn<ProductsModel, String> colPriceP;
    @FXML
    private TableView<ProductsModel> rejectedTable;
    @FXML
    public TableColumn<ProductsModel, String> colNameR;
    @FXML
    public TableColumn<ProductsModel, String> colFulfillmentCenterR;
    @FXML
    public TableColumn<ProductsModel, Integer> colAmountR;
    @FXML
    public TableColumn<ProductsModel, String> colPriceR;
    @FXML
    public Label sumUp;
    @FXML
    void btnOk(ActionEvent event) {
        saveAsCVS();
        closeStage(event);
    }

    public void setAppMainObservableList(ObservableList<ProductsModel> cartList) {
        purchasedList = FXCollections.observableArrayList();
        rejectedList = FXCollections.observableArrayList();
        double sumPrice = 0;
        for(ProductsModel product: cartList){
            if(product.getIsSold()) {
                purchasedList.add(product);
                sumPrice+=product.getCurrentPriceDouble();
            }
            else
                rejectedList.add(product);
        }
        initTable(purchasedTable, colNameP, colFulfillmentCenterP, colAmountP, colPriceP, purchasedList);
        initTable(rejectedTable, colNameR, colFulfillmentCenterR, colAmountR, colPriceR, rejectedList);
        sumUp.setText(sumUp.getText()+" "+df.format(sumPrice));
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void initTable(TableView<ProductsModel> table, TableColumn<ProductsModel, String> name, TableColumn<ProductsModel, String> fulfillmentCenter, TableColumn<ProductsModel, Integer> amount, TableColumn<ProductsModel, String> price, ObservableList<ProductsModel> list){
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        fulfillmentCenter.setCellValueFactory(new PropertyValueFactory<>("fulfillmentCenterName"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amountCart"));
        price.setCellValueFactory(new PropertyValueFactory<>("currentPriceString"));
        table.setItems(list);
    }

    private void saveAsCVS() {
        for (ProductsModel product : purchasedList) {
            Field[] fields = product.getClass().getDeclaredFields();
            Method[] methods = product.getClass().getDeclaredMethods();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DatabaseField.class)) {
                    DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
                    field.setAccessible(true);
                    System.out.println("Column: " + databaseField.columnName() + " Value: ");
                }
            }
        }
    }
}
