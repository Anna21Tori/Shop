package model;

import java.io.Serializable;
import java.util.*;

public class FulfillmentCenterContainer implements Comparator<String>, Serializable {
    ArrayList <FulfillmentCenter> listCenter;

    public FulfillmentCenterContainer(){
        listCenter = new ArrayList<>();
    }

    public ArrayList<FulfillmentCenter> getListCenter(){
        return  listCenter;
    }

    @Override
    public int compareTo(String first, String second){
        return first.compareTo(second);
    }

    public FulfillmentCenter getFulfillmentCenter(String name){
        for(FulfillmentCenter i: listCenter){
            if(compareTo(name, i.getName()) == 0)
                return i;
        }
        return null;
    }

    public void addCenter(FulfillmentCenter item){
        listCenter.add(item);
    }

    public ArrayList<Item> filter(String pattern, String fulfillment){
        ArrayList<Item> container = new ArrayList<>();
        if(fulfillment.equals("All")){
              for(FulfillmentCenter center: listCenter)
                    container.addAll(center.filter(pattern));
        }else{
            FulfillmentCenter center = getFulfillmentCenter(fulfillment);
            container.addAll(center.filter(pattern));
        }
        return container;
    }

    public boolean buy(int idProduct, String nameProduct, String nameFulfillmentCenter, int amount){
        FulfillmentCenter center = getFulfillmentCenter(nameFulfillmentCenter);
        if(center != null)
            return center.buy(idProduct, nameProduct, amount);
        return false;
    }

    public void initSaleState(){
        for(FulfillmentCenter center:listCenter)
            center.setSaleStateNew();
    }

    public void addCart(int idProduct, String nameProduct, String nameFulfillmentCenter, int amount){
        FulfillmentCenter center = getFulfillmentCenter(nameFulfillmentCenter);
        if(center != null)
            center.addCart(nameProduct, idProduct, amount);
    }

    public ArrayList<SoldItem> getCart(){
        ArrayList<SoldItem> container = new ArrayList<>();
            for(FulfillmentCenter center: listCenter)
                container.addAll(center.getCart());
        return container;
    }

    public void clearCart(){
        for(FulfillmentCenter center:listCenter)
            center.clearCart();
    }

    public void upDataCart(int idProduct, String nameProduct, String nameFulfillmentCenter, int amount){
        FulfillmentCenter center = getFulfillmentCenter(nameFulfillmentCenter);
        if(center != null)
            center.upDateCart(nameProduct, idProduct, amount);
    }
}
