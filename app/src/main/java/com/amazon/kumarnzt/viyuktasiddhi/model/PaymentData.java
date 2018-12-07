package com.amazon.kumarnzt.viyuktasiddhi.model;

public class PaymentData {
    private String otp;
    private Double amount;
    private String storeId;

    public PaymentData(String otp, Double amount, String storeId) {
        this.otp = otp;
        this.amount = amount;
        this.storeId = storeId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
