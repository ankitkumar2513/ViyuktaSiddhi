package com.amazon.kumarnzt.viyuktasiddhi;

import android.telephony.SmsManager;

import java.util.Random;

public class MessageUtils {

    private static final Random random = new Random();

    public static void sendMessage(final String phone, final String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+91" + phone, null, message, null, null);
    }
}
