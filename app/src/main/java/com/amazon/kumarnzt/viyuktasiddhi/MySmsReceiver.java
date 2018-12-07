package com.amazon.kumarnzt.viyuktasiddhi;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.amazon.kumarnzt.viyuktasiddhi.model.CustomerSellerDataModel;
import com.amazon.kumarnzt.viyuktasiddhi.model.PaymentData;
import com.amazon.kumarnzt.viyuktasiddhi.model.PaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;

public class MySmsReceiver extends BroadcastReceiver {

    private static final String TAG = MySmsReceiver.class.getSimpleName();
    private static final String pdu_type = "pdus";
    private static ObjectMapper mapper = new ObjectMapper();

    private static final String OTP_REGEX = "^[0-9]{4}$";

    private ViyuktServiceClient viyuktServiceClient;

    public MySmsReceiver() {
        this.viyuktServiceClient = new ViyuktServiceClient();
    }

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
        } else if (messageString.startsWith("{") && messageString.endsWith("}")) {
            processRequestMessage(messageString, strippedPhoneNumber);
        } else {
            Log.i(TAG, "Ignoring message " + messageString);
        }
    }

    private void processRequestMessage(final String messageString, final String originatingAddress) {
        try {
            CustomerSellerDataModel message= mapper.readValue(messageString, CustomerSellerDataModel.class);
            Log.d(TAG, message.getStoreId());
            Log.d(TAG, message.getAmount().toString());
            MessageUtils.sendMessage(originatingAddress, "OTP for offline amazon payment " +
                    OTPHandler.getOTP(originatingAddress, message.getAmount(), message.getStoreId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processOTPMessage(final String messageString, final String originatingAddress) {
        PaymentData data = OTPHandler.getPaymentData(originatingAddress, messageString);
        if (data != null) {
            Log.i(TAG, String.format("store %s phone number %s amount %f",
            data.getStoreId(), originatingAddress, data.getAmount()));
            new ProcessPayment().execute(
                    data.getStoreId(),
                    originatingAddress,
                    Double.valueOf(data.getAmount()).toString());
            Log.d(TAG,"correct otp");
        }
    }

    private String stripPhoneNumber(final String phoneNumber) {
        return phoneNumber.replaceFirst("^\\+91", "");
    }

    private class ProcessPayment extends AsyncTask<String, Void, String> {
        private String storeId;

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i(TAG, Arrays.toString(params));
                storeId = params[0];
                String phoneNumber = params[1];
                double amount = Double.parseDouble(params[2]);
                String paymentDetails = viyuktServiceClient.completePayment(storeId, phoneNumber, amount);
                Log.i(TAG, paymentDetails);
                return paymentDetails;
            } catch (IOException e) {
                Log.e(TAG, String.format("error %s \n exception: %s\n",
                        e.getMessage(), Arrays.toString(e.getStackTrace())));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute " + result);
            try {
                final PaymentResponse paymentResponse = mapper.readValue(result, PaymentResponse.class);

                if ("SUCCESS".equals(paymentResponse.getStatus())) {

                    // Send Message to customer
                    String message = String.format("Payment of Rs. %.2f against store %s happened successfully. " +
                                    "Current Balance Rs. %.2f",
                            paymentResponse.getTransactionAmount(),
                            storeId,
                            paymentResponse.getApayCustomer().getCurrentBalance());
                    Log.i(TAG, "Sending message " + message);
                    MessageUtils.sendMessage(paymentResponse.getApayCustomer().getPhoneNumber(), message);

                    //Send Message to store;
                    String messageToStore = String.format("Payment of Rs. %.2f has been credited successfully. " +
                                    "Current Balance Rs. %.2f",
                            paymentResponse.getTransactionAmount(),
                            paymentResponse.getApayCustomer().getCurrentBalance());
                    MessageUtils.sendMessage(paymentResponse.getApayStore().getPhoneNumber(), messageToStore);
                } else {
                    // Send Message to customer

                    String message = String.format("Payment of Rs. %.2f against store %s failed because of %s",
                            paymentResponse.getTransactionAmount(),
                            storeId,
                            paymentResponse.getReason());
                    Log.i(TAG, "Sending message " + message);
                    MessageUtils.sendMessage(paymentResponse.getApayCustomer().getPhoneNumber(), message);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "preExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
