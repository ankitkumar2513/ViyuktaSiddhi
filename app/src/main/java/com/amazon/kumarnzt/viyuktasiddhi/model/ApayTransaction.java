package com.amazon.kumarnzt.viyuktasiddhi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApayTransaction {
    @JsonProperty("TransactionId")
    private String transactionId;

    @JsonProperty("CustomerId")
    private String customerId;

    @JsonProperty("StoreId")
    private String storeId;

    @JsonProperty("TransactionAmount")
    private Double transactionAmount;

    @JsonProperty("Timestamp")
    private Long timestamp;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Reason")
    private String reason;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

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
}
