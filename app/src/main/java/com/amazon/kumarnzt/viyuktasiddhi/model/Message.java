package com.amazon.kumarnzt.viyuktasiddhi.model;

public class Message {
    private String merchantId;
    private Double amount;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
