package com.sdsu.cs646.shameetha.assignment3new;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;


public class InstructorActivity extends ListActivity  {

    private InstructorListAdapter instructorListAdapter;
    private List<Instructor> instructorList = new ArrayList<Instructor>();
    private InstructorService instructorService;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        mRequestQueue =  Volley.newRequestQueue(this);

        instructorService = new InstructorService();
        refreshInstructorList(instructorService);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position,
                                   long id) {
        super.onListItemClick(listView, view, position, id);
        Instructor professor = (Instructor) listView.getItemAtPosition(position);
        int selectedProfessorId = professor.getId();

        Intent i = new Intent(getApplicationContext(), InstructorDetailsActivity.class);
        i.putExtra("selectedInstructorId",
                selectedProfessorId);
        startActivity(i);

    }

    private void refreshInstructorList(InstructorService professorService) {
        try {
            instructorList = professorService.getInstructorList();

            instructorListAdapter = new InstructorListAdapter(instructorList, this);
            setListAdapter(instructorListAdapter);
            instructorListAdapter.refreshList(instructorList);
            instructorListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("RateMyProfessor", e.getMessage(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
