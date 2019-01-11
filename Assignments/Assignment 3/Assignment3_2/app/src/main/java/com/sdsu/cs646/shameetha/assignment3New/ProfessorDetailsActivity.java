package com.sdsu.cs646.shameetha.assignment3New;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfessorDetailsActivity extends ListActivity {

    private static final String TAG = ProfessorDetailsActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Professor> detailsList = new ArrayList<Professor>();
    private ListView listView;
    private CustomListAdapter adapter;
    List<Comments> rowItems;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView officeTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView averageTextView;
    private TextView totalRatingsTextView;
    private long selectedInstructorId;
    String tag_instructor_details = "instructor_details_request";
    String tag_instructor_comments = "instructor_comments_request";
    private DataBaseHelper professorHelper;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_details);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            Toast.makeText(this, "You have Internet Connection",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }

        firstNameTextView = (TextView) findViewById(R.id.firstName);
        lastNameTextView = (TextView) findViewById(R.id.lastName);
        officeTextView = (TextView) findViewById(R.id.office);
        phoneTextView = (TextView) findViewById(R.id.phone);
        emailTextView = (TextView) findViewById(R.id.email);
        averageTextView = (TextView) findViewById(R.id.average);
        totalRatingsTextView = (TextView) findViewById(R.id.totalRatings);
        Bundle details = getIntent().getExtras();
        selectedInstructorId = details.getLong("selectedInstructorId");
        professorHelper = DataBaseHelper.getInstance(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Bundle details = getIntent().getExtras();
        selectedInstructorId = details.getLong("selectedInstructorId");
        if (isInternetPresent) {
            refreshProfessorComments(selectedInstructorId);
            refreshProfessorDetails(selectedInstructorId);
        } else {
            refreshProfessorDetailsFromDatabase(selectedInstructorId);
            refreshProfessorCommentsFromDatabase(selectedInstructorId);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void rateInstructor(View click) {
        Intent rateInstructor = new Intent(getApplicationContext(), ReviewActivity.class);
        rateInstructor.putExtra("selectedInstructorId", selectedInstructorId);
        startActivity(rateInstructor);
    }

    public void refreshProfessorDetails(long selectedInstructorId) {
        String url = "http://bismarck.sdsu.edu/rateme/instructor/"
                + selectedInstructorId;
        JsonObjectRequest detailsRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                try {

                                    Professor professor = new Professor();
                                    if (response.getInt("id") != 0) {
                                        professor.setId(response.getInt("id"));
                                    }
                                    if (!response.isNull("firstName")) {
                                        professor.setFirstName(response.getString("firstName"));
                                    }
                                    if (!response.isNull("lastName")) {
                                        professor.setLastName(response.getString("lastName"));
                                    }
                                    if (!response.isNull("office")) {
                                        professor.setOffice(response.getString("office"));
                                    }
                                    if (!response.isNull("phone")) {
                                        professor.setPhone(response.getString("phone"));
                                    }
                                    if (!response.isNull("email")) {
                                        professor.setEmail(response.getString("email"));
                                    }
                                    if (!response.isNull("rating")) {
                                        JSONObject instructorSelectedRemarks = response
                                                .getJSONObject("rating");
                                        Float floatAverage = Float.valueOf(
                                                instructorSelectedRemarks.getString("average"));
                                        professor.setAverage(floatAverage);
                                        Integer intTotalRatings = Integer.valueOf(
                                                instructorSelectedRemarks.getString("totalRatings"));
                                        professor.setTotalRatings(intTotalRatings);
                                    }
                                    detailsList.add(professor);
                                    professorHelper.insertProfessorDetails(professor);
                                    setProfessorToView(professor);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(detailsRequest, tag_instructor_details);
    }

    public void refreshProfessorDetailsFromDatabase(long selectedInstructorId) {
        Professor professor = new Professor();
        professor = professorHelper.getProfessorDetails(selectedInstructorId);
        setProfessorToView(professor);
        hidePDialog();
    }

    public void refreshProfessorComments(final long selectedInstructorId) {

        String url = "http://bismarck.sdsu.edu/rateme/comments/"
                + selectedInstructorId;
        rowItems = new ArrayList<Comments>();
        JsonArrayRequest commentsRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Comments item = new Comments();
                                item.setText(obj.getString("text"));
                                item.setDate(obj.getString("date"));
                                rowItems.add(item);
                                item.setProfessorId(selectedInstructorId);
                                professorHelper.insertComments(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                hidePDialog();
                            }
                        }
                        listView = (ListView) findViewById(android.R.id.list);
                        ProfessorCommentsAdapter commentsAdapter = new ProfessorCommentsAdapter(getApplicationContext(), rowItems);
                        listView.setAdapter(commentsAdapter);
                        commentsAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });
        AppController.getInstance().addToRequestQueue(commentsRequest, tag_instructor_comments);
    }

    public void refreshProfessorCommentsFromDatabase(final long selectedInstructorId) {
        rowItems = professorHelper.getAllCommentsByProfessor(selectedInstructorId);
        listView = (ListView) findViewById(android.R.id.list);
        ProfessorCommentsAdapter professorCommentsAdapter = new ProfessorCommentsAdapter(getApplicationContext(), rowItems);
        listView.setAdapter(professorCommentsAdapter);
        professorCommentsAdapter.notifyDataSetChanged();
        hidePDialog();
    }

    public void setProfessorToView(Professor professor) {
        firstNameTextView.setText(professor.getFirstName());
        lastNameTextView.setText(professor.getLastName());
        officeTextView.setText(professor.getOffice());
        phoneTextView.setText(professor.getPhone());
        emailTextView.setText(professor.getEmail());
        averageTextView.setText(Float.toString(professor.getAverage()));
        totalRatingsTextView.setText(Integer.toString(professor.getTotalRatings()));

    }


}
