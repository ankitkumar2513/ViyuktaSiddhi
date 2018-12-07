package com.amazon.kumarnzt.viyuktasiddhi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionResponse extends ApayTransaction {
    @JsonProperty("CustomerName")
    private String customerName;

    @JsonProperty("StoreName")
    private String storeName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}

