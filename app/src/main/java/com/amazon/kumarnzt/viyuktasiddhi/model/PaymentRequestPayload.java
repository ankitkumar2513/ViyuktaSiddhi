package com.amazon.kumarnzt.viyuktasiddhi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequestPayload {
    @JsonProperty("CustomerPhoneNumber")
    private String customerPhoneNumber;

    @JsonProperty("StoreId")
    private String storeId;

    @JsonProperty("TransactionAmount")
    private Double transactionAmount;

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}