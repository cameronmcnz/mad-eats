package com.eatrading.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quote {
    private String symbol;
    private double price;
    private double bid;
    private double ask;
    
    @JsonProperty("spreadBps")
    private double spreadBps;
    
    private String currency;
    private Double change;
    
    @JsonProperty("changePercent")
    private Double changePercent;
    
    @JsonProperty("previousClose")
    private Double previousClose;
    
    @JsonProperty("asOf")
    private String asOf;
    
    @JsonProperty("marketState")
    private String marketState;

    // Constructors
    public Quote() {}

    public Quote(String symbol, double price, double bid, double ask) {
        this.symbol = symbol;
        this.price = price;
        this.bid = bid;
        this.ask = ask;
    }

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getSpreadBps() {
        return spreadBps;
    }

    public void setSpreadBps(double spreadBps) {
        this.spreadBps = spreadBps;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }

    public Double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(Double previousClose) {
        this.previousClose = previousClose;
    }

    public String getAsOf() {
        return asOf;
    }

    public void setAsOf(String asOf) {
        this.asOf = asOf;
    }

    public String getMarketState() {
        return marketState;
    }

    public void setMarketState(String marketState) {
        this.marketState = marketState;
    }
}
