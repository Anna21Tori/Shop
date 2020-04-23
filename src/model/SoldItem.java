package model;

import java.io.Serializable;

public class SoldItem implements Serializable {
    private Item product;
    private int amount;

    public SoldItem(Item product, int amount){
        this.product = product;
        this.amount = amount;
    }

    public Item getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
