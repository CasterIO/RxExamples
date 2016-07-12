package io.caster.rxexamples.models;

public class Stock {
    private double price;
    private String ticker;

    public Stock() {
    }

    public Stock(String ticker, double price) {
        this.price = price;
        this.ticker = ticker;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", ticker, price);
    }
}
