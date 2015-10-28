package io.caster.rxexamples.models;

public class Order {
    private String id;
    private long orderTotal;

    public Order() {
    }

    public Order(String id, long orderTotal) {
        this.id = id;
        this.orderTotal = orderTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(long orderTotal) {
        this.orderTotal = orderTotal;
    }
}
