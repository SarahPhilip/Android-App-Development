package com.sdsu.cs646.shameetha.assignment3new;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

/**
 * Created by Shameetha on 3/11/15.
 */
public class GETNetworkConnection extends AsyncTask<String, Void, String> {

    public String makeGetRequest(String url) {
//        HttpClient httpClient = new DefaultHttpClient();
        HttpClient httpClient = AndroidHttpClient.newInstance(null);
        HttpGet getMethod = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responsebody = null;
        try {
            responsebody = httpClient.execute(getMethod, responseHandler);
        } catch (Exception e) {
            Log.e("RateMyProfessor", e.getMessage(), e);
        }
        httpClient.getConnectionManager().shutdown();
        return responsebody;
    }

    @Override
    protected String doInBackground(String... url) {
        return makeGetRequest(url[0]);
    }

}
//Get Example with JSON
//51
//        http://www.eli.sdsu.edu/courses/fall09/cs696/examples/names.json
//        [{"firstname":"Roger","lastname":"Whitney"},
//        {"firstname":"Robert","lastname":"Edwards"},
//        {"firstname":"Kris","lastname":"Stewart"}]

//No Change in doInBackground
//
//class HttpClientTask extends AsyncTask<String, Void, String> {
//    @Override
//    protected String doInBackground(String... urls) {
//        try {
//            ResponseHandler<String> responseHandler =
//                    new BasicResponseHandler();
//            HttpGet getMethod = new HttpGet(urls[0]);
//            String responseBody = httpclient.execute(getMethod,
//                    responseHandler);
//            return responseBody;
//        } catch (Throwable t) {
//            Log.i("rew","did not work", t);
//        }
//        return null;
//    }

//Handing JSON in onPostExecute

//public void onPostExecute(String jsonString) {
//        try {
//        JSONArray data = new JSONArray(jsonString);
//        JSONObject firstPerson = (JSONObject) data.get(0);
//        String firstName = firstPerson.getString("firstname");
//        String lastName = firstPerson.getString("lastname");
//        Log.i("rew", firstName + " " + lastName);
//        } catch (JSONException e) {
//        e.printStackTrace();
//        }
//        }


//Giving the task the URL
//        54
//public void onResume() {
//        super.onResume();
//        String userAgent = null;
//        httpclient = AndroidHttpClient.newInstance(userAgent);
//        HttpClientTask task = new HttpClientTask();
//        String url = "http://www.eli.sdsu.edu/courses/fall09/cs696/examples/
//        names.json";
//        task.execute(url);
//        }