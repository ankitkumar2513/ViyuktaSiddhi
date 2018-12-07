package com.amazon.kumarnzt.viyuktasiddhi;


import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsRequestPoster {
    private static final String TAG = "PaymentProcessor";

    private static final Map<String, String> headersMap = new HashMap<String, String>() {
        {
            put("accept", "text/html");
            put("accept-charset", "utf-8");
            put("Content-length", "1218");
            put("Content-type", "application/json; charset=utf-8");
            put("Date", "Mon, 27 Aug 2018 03:11:53 GMT");
            put("Host", "junglee-dev-gamma-arjubasu-1-2a6c9a55.eu-west-1.amazon.com");
            put("servername", "");
            put("User-Agent", "RAWPagelet");
            put("x-amz-remote-content", "1");
            put("x-amz-rid", "5Y4RFP1RZA67E53GZP5B");
            put("x-amzn-actiontrace", "amzn1.tr.f2b0ac6d-a9a6-11e8-865b-0a5057f10000.1.null.IBi3cU");
            put("x-amzn-ActionTrace-caller", "10.80.87.241");
            put("X-Amzn-Bsf-Service-Target", "RAWClient/GetContent");
            put("X-Amzn-Client-TTL-Seconds", "20");
            put("X-Amzn-RequestId", "5Y4RFP1RZA67E53GZP5B");
            put("X-CLIENT-ID", "13806@preprod-gp-eux-live-feature-1a-6be2934f.eu-west-1.amazon.com");
            put("x-forwarded-for", "10.80.87.241");
            put("x-raw-version", "1.0");
        }
    };

    private static final String body = "{\"request\":{\"protocol\":\"HTTP/1.1\",\"secure\":\"1\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:52.0) Gecko/20100101 Firefox/52.0\",\"serverPort\":80,\"serverName\":\"arjubasu.amazon.in\",\"uri\":\"/mn/landing/14258646031/ref=s9_acss_bw_cg_sbc_1a1_w\",\"parameters\":{\"debug\":\"widget\"},\"clientAddress\":\"100.127.2.117\",\"method\":\"GET\",\"internal\":\"1\",\"robot\":\"0\",\"refTag\":null,\"queryString\":\"_encoding=UTF8&debug=widget\",\"referer\":null},\"pageType\":\"Landing\",\"language\":\"en_IN\",\"clientID\":\"260-0571512-9239130\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:52.0) Gecko/20100101 Firefox/52.0\",\"weblabs\":{\"DUMMY_COUPONS_PILOT_157489\":\"T1\"},\"subPageType\":\"hybrid-batch-btf\",\"marketplaceID\":\"A21TJRUUN4KGV\",\"customerID\":\"A1R5XU8Y9AG5YF\",\"sessionID\":\"260-8903651-0519450\",\"widgetParameters\":\"{\\\"widgetLogTag\\\":\\\"lpa-cpn-widget-food\\\", \\\"showStaging\\\":\\\"true\\\",  \\\"couponIdList\\\":[\\\"4f42f2ac-14c5-49a7-a6ef-25e4dc4320bf\\\",\\\"37bcab85-746c-46c1-86f3-84ebe535e44e\\\",\\\"4cdbab87-656a-473d-a572-48c4e9014759\\\",\\\"b892df5c-b2ea-4ca1-8f5a-6a66b94b0e99\\\",\\\"823197a5-dad7-44b5-8080-729f47dc2264\\\",\\\"2a2e0994-6804-4880-bfa2-898b60cb3730\\\",\\\"36da5ae8-2354-4124-ae31-c6ab93eb1269\\\",\\\"430946d2-ca0f-4497-85c8-0daf57b4fed5\\\"]}\"}";


    public static String sendPost() throws IOException {

        // this part is needed cause Lebocoin has invalid SSL certificate, that cannot be normally processed by Java
        TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null; // Not relevant.
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // Do nothing. Just allow them all.
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // Do nothing. Just allow them all.
                    }
                }
        };

        HostnameVerifier trustAllHostnames = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true; // Just allow them all.
            }
        };

        try {
            System.setProperty("jsse.enableSNIExtension", "false");
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
        } catch (GeneralSecurityException e) {
            Log.e(TAG, e.getMessage());
        }

        String url = "https://pahwv-rw-lf.amazon.in/h/coupons-widget";
        //url = "http://pahwv-rw-lf.amazon.in/h/coupons-widget";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");

        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            con.setRequestProperty(entry.getKey(), entry.getValue());
        }

        Log.i(TAG, "1. Sending 'POST' request to URL : " + url);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        Log.i(TAG, "1.5 Sending 'POST' request to URL : " + url);
        wr.writeBytes(body);
        wr.flush();
        wr.close();

        Log.i(TAG, "2. Sending 'POST' request to URL : " + url);

        int responseCode = con.getResponseCode();

        Log.i(TAG, "3. Sending 'POST' request to URL : " + url);
        Log.i(TAG, "Post parameters : " + body);
        Log.i(TAG, "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Log.i(TAG, "Response : " + response.toString());

        return response.toString();
    }
}