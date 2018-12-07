package com.amazon.kumarnzt.viyuktasiddhi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("CurrentBalance")
    private Double currentBalance;

    @JsonProperty("PhoneNumber")
    private String phoneNumber;

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
