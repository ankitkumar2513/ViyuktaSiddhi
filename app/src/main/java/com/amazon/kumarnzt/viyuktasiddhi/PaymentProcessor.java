package com.amazon.kumarnzt.viyuktasiddhi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.StrictMode;

import java.io.IOException;
import java.util.Arrays;

public class PaymentProcessor extends AppCompatActivity {
    private static final String TAG = "PaymentProcessor";

    private static final ViyuktServiceClient VIYUKT_SERVICE_CLIENT = new ViyuktServiceClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment_processor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i(TAG, Arrays.toString(params));
                Log.i(TAG, VIYUKT_SERVICE_CLIENT.getTransactionsForCustomer("A2"));
                Log.i(TAG, VIYUKT_SERVICE_CLIENT.getTransactionsForStore("AS1"));
                return null;
            } catch (IOException e) {
                Log.e(TAG, String.format("error %s \n exception: %s\n",
                        e.getMessage(), Arrays.toString(e.getStackTrace())));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "preExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    static class PaymentProcess extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i(TAG, Arrays.toString(params));
                String payment = VIYUKT_SERVICE_CLIENT.completePayment("AS1", "8095232075", 10.0);
                Log.i(TAG, payment);
                return payment;
            } catch (IOException e) {
                Log.e(TAG, String.format("error %s \n exception: %s\n",
                        e.getMessage(), Arrays.toString(e.getStackTrace())));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "post execute " + result);
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "preExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
