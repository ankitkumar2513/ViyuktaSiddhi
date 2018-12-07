package com.amazon.kumarnzt.viyuktasiddhi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentResponse {
    @JsonProperty("Status")
    private String status;

    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("TransactionAmount")
    private Double transactionAmount;

    @JsonProperty("ApayCustomer")
    private User apayCustomer;

    @JsonProperty("ApayStore")
    private User apayStore;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public User getApayCustomer() {
        return apayCustomer;
    }

    public void setApayCustomer(User apayCustomer) {
        this.apayCustomer = apayCustomer;
    }

    public User getApayStore() {
        return apayStore;
    }

    public void setApayStore(User apayStore) {
        this.apayStore = apayStore;
    }
}
