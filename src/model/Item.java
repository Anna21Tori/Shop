package model;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private transient SaleState saleState;
    private double mass;
    private int amount;
    private int currentAmount;
    private int soldAmount;
    private double price;
    private int id;
    private int idFulfillmentCenter;
    private String fulfilmentCenterName;
    public static int ID = 1;
    public Item(String name, SaleState saleState, double mass, int amount, double price){
        this.name = name;
        this.saleState = saleState;
        this.mass = mass;
        this.amount = amount;
        this.price = price;
        this.currentAmount = amount;
        this.soldAmount = 0;
        this.id = ID++;
    }

    public void changeAmount(int amount){
        this.currentAmount -=amount;
        System.out.println(currentAmount);
        this.soldAmount +=amount;
    }
    public void setIdFulfillmentCenter(int idFulfillmentCenter){
        this.idFulfillmentCenter = idFulfillmentCenter;
    }

    public String getName(){
        return name;
    }

    public String getFulfilmentCenterName() {
        return fulfilmentCenterName;
    }

    public void setFulfilmentCenterName(String fulfilmentCenterName) {
        this.fulfilmentCenterName = fulfilmentCenterName;
    }

    public double getMass(){
        return mass;
    }

    public int getCurrentAmount(){
        return currentAmount;
    }

    public double getPrice(){
        return price;
    }

    public int getId(){
        return id;
    }

    public String getSaleState(){
        return saleState.toString();
    }

    public void setSaleState(SaleState saleState) {
        this.saleState = saleState;
    }

    public int getAmount() {
        return amount;
    }

    public int getSoldAmount() {
        return soldAmount;
    }

    public int getIdFulfillmentCenter() {
        return idFulfillmentCenter;
    }

    public boolean validateAmount(int amount){
        if(amount <= this.currentAmount)
            return true;
        return false;
    }

}
