package com.sdsu.cs646.shameetha.assignment3New;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;


public class ReviewActivity extends ActionBarActivity {

    private static final String TAG = ProfessorDetailsActivity.class.getSimpleName();
    private RatingBar ratingBar;
    private EditText commentsText;
    private Context context;
    private LayoutInflater inflater;
    private long selectedInstructorId;
    String tag_post_rating = "instructor_rating_post";
    String url;
    Boolean ratingResponse = true;
    Boolean commentsResponse = true;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            commentsText = (EditText) findViewById(R.id.commentsText);
            context = getApplicationContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            inflater = (LayoutInflater) context
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
        } else {
            Toast.makeText(this, "Sorry, No Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }
        Bundle details = getIntent().getExtras();
        selectedInstructorId = details.getLong("selectedInstructorId");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void submitRatingComments(View click) {
        try {
            int inputRating = (int) ratingBar.getRating();
            String rating = new Integer(inputRating).toString();
            String comments = commentsText.getText().toString();
            if (rating.equals("0") && (comments.equals(""))) {
                Toast.makeText(this, "Enter rating and comments",
                        Toast.LENGTH_SHORT).show();
            } else if (rating.equals("0") && (!comments.equals(""))) {
                Toast.makeText(this, "Enter a rating", Toast.LENGTH_SHORT)
                        .show();
            } else {
                submitRatings(rating);
                submitComments(comments);
                if (ratingResponse && commentsResponse) {
                    submit_success();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this,
                    "Sorry for the inconvenience. Try again later.",
                    Toast.LENGTH_SHORT).show();
            Log.e("RateMyProfessor", e.getMessage(), e);
        }
    }

    public void submitRatings(String rating) {
        Bundle details = getIntent().getExtras();
        selectedInstructorId = details.getLong("selectedInstructorId");
        url = "http://bismarck.sdsu.edu/rateme/rating/" + selectedInstructorId + "/" + rating;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        ratingResponse = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        ratingResponse = false;
                    }
                });
        AppController.getInstance().addToRequestQueue(postRequest, tag_post_rating);
    }

    public void submitComments(final String comments) {
        Bundle details = getIntent().getExtras();
        selectedInstructorId = details.getLong("selectedInstructorId");
        url = "http://bismarck.sdsu.edu/rateme/comment/" + selectedInstructorId;
        String tag_json_obj = "json_obj_req";
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        commentsResponse = true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                commentsResponse = false;
            }
        })
        {
            public byte[] getBody() throws AuthFailureError {
                if ((getMethod() == Method.POST) && (comments != null)) {
                    return comments.getBytes();
                } else {
                    return super.getBody();
                }
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void submit_success() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
        builder.setMessage("Submitted!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ReviewActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
