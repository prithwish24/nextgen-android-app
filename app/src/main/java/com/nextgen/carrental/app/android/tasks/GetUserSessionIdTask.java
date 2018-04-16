package com.nextgen.carrental.app.android.tasks;

import android.os.AsyncTask;

import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

/**
 * Created by prith on 4/17/2018.
 */

public class GetUserSessionIdTask extends AsyncTask <AIRequest,Void,AIResponse> {

    protected AIResponse doInBackground(AIRequest... requests) {
        /*final AIRequest request = requests[0];
        try {

            final AIResponse response = aiButton.getAIService().textRequest(request);
            return response;
        } catch (AIServiceException e) {
        }*/
        return null;
    }


    protected void onPostExecute(AIResponse aiResponse) {
        /*if (aiResponse != null) {
            String sessionid = "";
            for (AIOutputContext a : aiResponse.getResult().getContexts()) {
                if (a.getName().equals("carrental")) {
                    sessionid = a.getParameters().get("sessionId").getAsString();
                }
            }

            try {
                BaseResponse resp = RestClient.INSTANCE.getRequest(initialUrl, ZipCodeResponse.class,sessionid,"63001");
                System.out.println(resp);
            } catch (Exception e) {

            }
        }*/
    }


}
