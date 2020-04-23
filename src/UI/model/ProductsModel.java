package UI.model;

import UI.Adnotacje.DatabaseField;
import UI.Adnotacje.DatabaseMethod;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.text.DecimalFormat;

public class ProductsModel {
    private static DecimalFormat df = new DecimalFormat("0.00");
    @DatabaseField(columnName = "Name")
    private SimpleStringProperty  name;
    @DatabaseField(columnName = "Fulfillment Center")
    private SimpleStringProperty fulfillmentCenterName;
    private SimpleDoubleProperty mass;
    private SimpleIntegerProperty amount;
    private SimpleIntegerProperty currentAmount;
    private SimpleStringProperty saleState;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty id;
    @DatabaseField(columnName = "Amount")
    private SimpleIntegerProperty amountCart;
    private SimpleBooleanProperty isAddedCart;
    private SimpleBooleanProperty isSold;

    public ProductsModel(String name, Double mass, Integer amount, String saleState, Double price, Integer id, String fulfillmentCenterName) {
        this.name = new SimpleStringProperty(name);
        this.mass = new SimpleDoubleProperty(mass);
        this.amount = new SimpleIntegerProperty(amount);
        this.currentAmount = new SimpleIntegerProperty(amount);
        this.saleState = new SimpleStringProperty(saleState);
        this.price = new SimpleDoubleProperty(price);
        this.id = new SimpleIntegerProperty(id);
        this.amountCart = new SimpleIntegerProperty(0);
        this.fulfillmentCenterName = new SimpleStringProperty(fulfillmentCenterName);
        this.isAddedCart = new SimpleBooleanProperty(false);
        this.isSold = new SimpleBooleanProperty(false);
    }

    public String getName() {
        return name.get();
    }

    public double getMass() {
        return mass.get();
    }

    public String getFulfillmentCenterName(){
        return fulfillmentCenterName.get();
    }

    public int getAmount() {
        return amount.get();
    }

    public int getId() {
        return id.get();
    }

    public int getCurrentAmount() {
        return currentAmount.get();
    }


    public void setAmountCart(int amountCart) {
        this.amountCart.set(this.amountCart.get() + amountCart);
        this.currentAmount.set(this.amount.get() - this.amountCart.get());
    }

    public int getAmountCart() {
        return amountCart.get();
    }


    public String getSaleState() {
        return saleState.get();
    }

    public double getPrice() {
        return price.get();
    }

    @DatabaseMethod(columnName = "Price")
    public String getCurrentPriceString(){
        return df.format(price.get()*amountCart.get());
    }
    public double getCurrentPriceDouble(){
        return price.get()*amountCart.get();
    }
    public String getToolTip(){
        return "Mass: "+mass.get()+"\n"+"Sale state: "+saleState.get();
    }

    public Boolean getIsAddedCart(){
        return isAddedCart.get();
    }

    public void setIsAddedCart(Boolean isChange){
        this.isAddedCart.set(isChange);
    }

    public boolean getIsSold() {
        return isSold.get();
    }

    public void setIsSold(boolean isSold) {
        this.isSold.set(isSold);
        this.isAddedCart.set(!isSold);
    }
}
