package com.amazon.kumarnzt.viyuktasiddhi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;
import com.amazon.kumarnzt.viyuktasiddhi.model.Message;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static ObjectMapper mapper = new ObjectMapper();
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkForSmsPermission();

        button = findViewById(R.id.send_message);
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void sendMessage(View view) {
        try {
            Toast.makeText(MainActivity.this, "Sending message", Toast.LENGTH_SHORT).show();
            sendMessage("9427635124", generateMessage("ABCD", 400.0d));
        } catch (Exception e) {
            Log.d("Sending", "Error while sending message", e);
        }
    }

    private void sendMessage(final String phone, final String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+91" + phone, null, message, null, null);
    }

    private String generateMessage(final String merchantId, final Double amount) throws IOException {
        Message message = new Message();
        message.setAmount(amount);
        message.setMerchantId(merchantId);
        return mapper.writeValueAsString(message);
    }
}
