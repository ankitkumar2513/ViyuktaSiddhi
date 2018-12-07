package com.amazon.kumarnzt.viyuktasiddhi;

import com.amazon.kumarnzt.viyuktasiddhi.model.PaymentData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPHandler {

    private static final Random random = new Random();
    private static Map<String, PaymentData> otpMap = new HashMap<>();

    private static String generateOTP() {
        return String.format("%04d", random.nextInt(10000));

    }

    public static String getOTP(final String phone, final Double amount, final String storeId) {
        String otp = generateOTP();
        otpMap.put(phone, new PaymentData(otp, amount, storeId));
        return otp;
    }

    public static PaymentData getPaymentData(final String phone, final String otp) {
        if (otpMap.containsKey(phone)) {
            if (otpMap.get(phone).getOtp().equals(otp)) {
                PaymentData data = otpMap.get(phone);
                otpMap.remove(phone);
                return data;
            }
        }
        return null;
    }
}
