package com.associate;

import java.util.ArrayList;
import java.util.List;

public class Item implements Comparable {

    private String value;
    private Item preItem; // 前继节点Item
    private List<Item> nextItem = new ArrayList<Item>(); // 后续节点Item

    private Item sibling; // 关联节点

    private int counter;

    public Item() {

    }

    public Item(String value) {
        this.value = value;
    }

    public void addCounter() {
        this.counter += 1;
    }

    public Item getSibling() {
        return sibling;
    }

    public void setSibling(Item sibling) {
        this.sibling = sibling;
    }

    public void addNextItem(Item item) {
        this.nextItem.add(item);
    }

    public List<Item> getNextItem() {

        return this.nextItem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Item getPreItem() {
        return preItem;
    }

    public void setPreItem(Item preItem) {
        this.preItem = preItem;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int compareTo(Object o) {
        int value;
        Item i = (Item) o;

        if (this.counter > i.counter) {
            value = -1;
        } else if (this.counter == i.counter) {
            value = 0;
        } else {
            value = 1;
        }
        return value;
    }

}
