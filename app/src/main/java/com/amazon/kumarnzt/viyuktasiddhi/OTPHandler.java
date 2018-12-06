package com.amazon.kumarnzt.viyuktasiddhi;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPHandler {

    private static final Random random = new Random();
    private static Map<String, String> otpMap = new HashMap<>();

    private static String generateOTP() {
        return String.format("%04d", random.nextInt(10000));

    }

    public static String getOTP(final String phone) {
        String otp = generateOTP();
        otpMap.put(phone, otp);
        return otp;
    }

    public static Boolean validateOTP(final String phone, final String otp) {
        if (otpMap.containsKey(phone)) {
            if (otpMap.get(phone).equals(otp)) {
                otpMap.remove(phone);
                return true;
            }
        }
        return false;
    }
}
