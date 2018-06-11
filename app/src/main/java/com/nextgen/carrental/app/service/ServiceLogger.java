package com.nextgen.carrental.app.service;

import android.util.Log;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServiceLogger implements ClientHttpRequestInterceptor {
    private static final String TAG = ServiceLogger.class.getName();

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Log.d (TAG, "========== Request Body ========== ");
        if (body != null && body.length > 0) {
            String reqBody = new String(body, StandardCharsets.UTF_8);
            Log.d (TAG, reqBody);
        }

        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);
        } finally {
            Log.d (TAG, "========== Response Body ========== ");
            if (response == null) {
                Log.d(TAG, "Response is NULL !");
            } else {
                String respStr = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
                Log.d(TAG, respStr);
                /*final StringBuilder sb = new StringBuilder();
                final BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody()));
                String str;
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                Log.d(TAG, sb.toString());*/
            }
        }
        return response;
    }
}
