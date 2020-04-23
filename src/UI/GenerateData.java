package UI;

import model.FulfillmentCenter;
import model.FulfillmentCenterContainer;
import model.Item;
import model.SaleState;

import java.util.ArrayList;
import java.util.List;

public class GenerateData {
    public static FulfillmentCenterContainer getFulfillmentCenterContainer(){
        FulfillmentCenterContainer container = new FulfillmentCenterContainer();
        container.addCenter(new FulfillmentCenter("Fulfillment 1", 10000, "Krakow"));
        container.addCenter(new FulfillmentCenter("Fulfillment 2", 10000, "Krakow"));
        container.addCenter(new FulfillmentCenter("Fulfillment 3", 10000, "Krakow"));
        container.addCenter(new FulfillmentCenter("Fulfillment 4", 10000, "Krakow"));
        container.addCenter(new FulfillmentCenter("Fulfillment 5", 10000, "Krakow"));
        container.addCenter(new FulfillmentCenter("Fulfillment 6", 10000, "Krakow"));
        container.addCenter(new FulfillmentCenter("Fulfillment 7", 10000, "Krakow"));
        List<FulfillmentCenter> list = new ArrayList<>();
        list = container.getListCenter();
        for (FulfillmentCenter item: list) {
            item.addProduct(new Item("Paper", SaleState.NEW, 10, 100, 1.2));
            item.addProduct(new Item("ink", SaleState.NEW, 1, 100, 0.9));
            item.addProduct(new Item("Book", SaleState.NEW, 20, 50, 20.59));
            item.addProduct(new Item("Pen", SaleState.NEW, 0.9, 100, 1.79));
        }
        return container;
    }
}
