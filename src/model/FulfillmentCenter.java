package model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FulfillmentCenter implements Comparator<String>, Serializable {
    private static DecimalFormat df = new DecimalFormat("0.00");
    private String name;
    private ArrayList<Item> listItem;
    private ArrayList<SoldItem> soldItem;
    private ArrayList<SoldItem> cart;
    private String place;
    private double amount;
    private int id;
    public static int ID = 1;
    public FulfillmentCenter(String name, double amount, String place){
        this.name = name;
        listItem = new ArrayList<>();
        soldItem = new ArrayList<>();
        cart = new ArrayList<>();
        this.amount = amount;
        this.place = place;
        this.id = ID++;
    }
    @Override
    public int compareTo(String first, String second){
        return first.compareTo(second);
    }

    public String getName(){
        return name;
    }

    public ArrayList<Item> getListItem() {
        return listItem;
    }

    public ArrayList<SoldItem> getSoldItem() {
        return soldItem;
    }

    public String getPlace() {
        return place;
    }

    public double getAmount() {
        return amount;
    }

    public ArrayList<SoldItem> getCart() {
        return cart;
    }

    public int getId() {
        return id;
    }

    private double countAmount(){
        double sum = 0;
        for(Item item: listItem)
            sum += item.getMass()*item.getCurrentAmount();
        return sum;
    }

    public boolean addProduct(Item item){
        if(countAmount()+(item.getMass()*item.getCurrentAmount()) <= amount) {
            listItem.add(item);
            item.setIdFulfillmentCenter(id);
            item.setFulfilmentCenterName(name);
            return true;
        }
        else
            return false;
    }
    public Item getProduct(int id, String name){
        for(Item product: listItem){
            if(compareTo(name, product.getName()) == 0 && product.getId() == id)
                return product;
        }
        return null;
    }
    public ArrayList<Item> filter(String pattern){
        ArrayList<Item> matchProducts = new ArrayList<>();
        for(Item product: listItem){
            if(pattern.isEmpty() || (product.getName().toLowerCase()).contains(pattern.toLowerCase()))
                matchProducts.add(product);
        }
        return matchProducts;
    }

    public boolean buy(int id, String name, int amount){
        Item product = getProduct(id, name);
        if(product !=null && product.validateAmount(amount)) {
            soldItem.add(new SoldItem(product, amount));
            product.changeAmount(amount);
            return true;
        }
        return false;
    }
    public String summarise(){
        return "Name: "+name+" \nPlace: "+place+" \nAmount: "+df.format(countAmount());
    }

    public void setSaleStateNew(){
        for(Item product: listItem)
            product.setSaleState(SaleState.NEW);
    }

    public void addCart(String name, int id, int amount){
        Item product = getProduct(id, name);
        if(product !=null)
            cart.add(new SoldItem(product, amount));
    }

    public void clearCart(){
        if(cart !=null)
            cart.clear();
    }

    public void upDateCart(String name, int id, int amount){
        for(SoldItem product: cart){
            if(product.getProduct().getName().equals(name) && product.getProduct().getId() == id)
                product.setAmount(amount);
        }
    }
}

