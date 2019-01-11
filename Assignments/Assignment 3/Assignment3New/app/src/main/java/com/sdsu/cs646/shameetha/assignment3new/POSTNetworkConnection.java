package com.sdsu.cs646.shameetha.assignment3new;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Shameetha on 3/11/15.
 */
public class POSTNetworkConnection extends
        AsyncTask<String, Void, HttpResponse> {

    private StringEntity postRateComments;
    private HttpResponse response;

    public HttpResponse postNetworkConnection(String url, String comments) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postMethod = new HttpPost(url);

        try {
            postRateComments = new StringEntity(comments);

            postMethod.setHeader("Content-Type",
                    "application/jason;charset =UTF-8");
            postMethod.setEntity(postRateComments);
            response = httpClient.execute(postMethod);
        } catch (Exception e) {
            Log.e("RateMyProfessor", e.getMessage(), e);
        }
        httpClient.getConnectionManager().shutdown();
        return response;
    }

    @Override
    protected HttpResponse doInBackground(String... params) {
        return postNetworkConnection(params[0], params[1]);
    }

}
