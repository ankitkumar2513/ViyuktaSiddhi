package com.amazon.kumarnzt.viyuktasiddhi;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.amazon.kumarnzt.viyuktasiddhi.model.CustomerSellerDataModel;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class MySmsReceiver extends BroadcastReceiver {

    private static final String TAG = MySmsReceiver.class.getSimpleName();
    private static final String pdu_type = "pdus";
    private static ObjectMapper mapper = new ObjectMapper();

    private static final String OTP_REGEX = "^[0-9]{4}$";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                processMessage(msgs[i].getMessageBody(), msgs[i].getOriginatingAddress());
            }
        }
    }

    private void processMessage(final String messageString, final String originatingAddress) {
        final String strippedPhoneNumber = stripPhoneNumber(originatingAddress);
        if (messageString.matches(OTP_REGEX)) {
            processOTPMessage(messageString, strippedPhoneNumber);
        } else {
            processRequestMessage(messageString, strippedPhoneNumber);
        }
    }

    private void processRequestMessage(final String messageString, final String originatingAddress) {
        try {
            CustomerSellerDataModel message= mapper.readValue(messageString, CustomerSellerDataModel.class);
            Log.d(TAG, message.getStoreId());
            Log.d(TAG, message.getAmount().toString());
            MessageUtils.sendMessage(originatingAddress, "OTP for offline amazon payment " + OTPHandler.getOTP(originatingAddress));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processOTPMessage(final String messageString, final String originatingAddress) {
        if (OTPHandler.validateOTP(originatingAddress, messageString)) {
            //TODO: call to pay
            Log.d(TAG,"correct otp");
        }
    }

    private String stripPhoneNumber(final String phoneNumber) {
        return phoneNumber.replaceFirst("^\\+91", "");
    }
}
