package com.sdsu.cs646.shameetha.assignment3new;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class InstructorDetailsActivity extends ListActivity {

    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView officeTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView averageTextView;
    private TextView totalRatingsTextView;
    private InstructorService instructorService;
    private int selectedInstructorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);

        firstNameTextView = (TextView) findViewById(R.id.firstName);
        lastNameTextView = (TextView) findViewById(R.id.lastName);
        officeTextView = (TextView) findViewById(R.id.office);
        phoneTextView = (TextView) findViewById(R.id.phone);
        emailTextView = (TextView) findViewById(R.id.email);
        averageTextView = (TextView) findViewById(R.id.average);
        totalRatingsTextView = (TextView) findViewById(R.id.totalRatings);

        Bundle details = getIntent().getExtras();
        selectedInstructorId = details.getInt("selectedInstructorId");
        instructorService = new InstructorService();
    }


    public void rateInstructor(View click) {
        Bundle details = getIntent().getExtras();
        int selectedInstructorId = details.getInt("selectedInstructorId");
        Intent rateInstructor = new Intent(getApplicationContext(), ReviewActivity.class);
        rateInstructor.putExtra("selectedInstructorId", selectedInstructorId);
        startActivity(rateInstructor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProfessorRatings();
        refreshInstructorCommentsList();
    }

    private void refreshInstructorCommentsList() {
        List<Instructor> instructorComments;
        try {
            instructorComments = instructorService
                    .getInstructorComments(selectedInstructorId);
            InstructorCommentsAdapter instructorCommentsAdapter = new InstructorCommentsAdapter(
                    instructorComments, this);
            setListAdapter(instructorCommentsAdapter);
            instructorCommentsAdapter.refreshList(instructorComments);
            instructorCommentsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("RateMyProfessor", e.getMessage(), e);
        }
    }

    private void refreshProfessorRatings() {
        Instructor professorDetails;
        try {
            professorDetails = instructorService
                    .getInstructorDetails(selectedInstructorId);

            firstNameTextView.setText(professorDetails.getFirstName());
            lastNameTextView.setText(professorDetails.getLastName());
            officeTextView.setText(professorDetails.getOffice());
            phoneTextView.setText(professorDetails.getPhone());
            emailTextView.setText(professorDetails.getEmail());
            float average2 = professorDetails.getAverage();
            Float faverage = new Float(average2);
            String straverage = faverage.toString();
            averageTextView.setText(straverage);
            int iTotalRatings = professorDetails.getTotalRatings();
            Integer totalRatingInt = new Integer(iTotalRatings);
            String totalRating = totalRatingInt.toString();
            totalRatingsTextView.setText(totalRating);
        } catch (Exception e) {
            Log.e("RateMyProfessor", e.getMessage(), e);
        }
    }
}
