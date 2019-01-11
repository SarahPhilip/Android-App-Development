package com.sdsu.cs646.shameetha.assignment3New;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfessorListActivity extends ListActivity {

    private static final String TAG = ProfessorListActivity.class.getSimpleName();
    private static final String url = "http://bismarck.sdsu.edu/rateme/list";
    private ProgressDialog pDialog;
    private List<Professor> mProfessorList = new ArrayList<Professor>();
    private ListView listView;
    private CustomListAdapter adapter;
    String tag_professor = "professor_list_request";
    private DataBaseHelper professorHelper;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);
        listView = (ListView) findViewById(android.R.id.list);
        professorHelper = DataBaseHelper.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        if (isInternetPresent) {
            adapter = new CustomListAdapter(this, mProfessorList);
            listView.setAdapter(adapter);
            JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    Professor professor = new Professor();
                                    professor.setId(obj.getInt("id"));
                                    professor.setFirstName(obj.getString("firstName"));
                                    professor.setLastName(obj.getString("lastName"));
                                    mProfessorList.add(professor);
                                    professorHelper.insertProfessor(professor);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            hidePDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hidePDialog();
                }
            });
            AppController.getInstance().addToRequestQueue(movieReq, tag_professor);
        } else {
            mProfessorList = professorHelper.getAllProfessors();
            adapter = new CustomListAdapter(this, mProfessorList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            hidePDialog();
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position,long id) {

        super.onListItemClick(listView, view, position, id);
        Professor professor = (Professor) listView.getItemAtPosition(position);
        long selectedProfessorId = professor.getId();
        Intent i = new Intent(getApplicationContext(), ProfessorDetailsActivity.class);
        i.putExtra("selectedInstructorId", selectedProfessorId);
        startActivity(i);
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

}
