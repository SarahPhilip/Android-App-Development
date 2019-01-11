package com.sdsu.cs646.shameetha.assignment3new;

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

import org.apache.http.HttpResponse;


public class ReviewActivity extends ActionBarActivity {

    private RatingBar ratingBar;
    private EditText commentsText;
    private InstructorService instructorService;
//    private TextView averageTextView;
//    private TextView totalRatingTextView;
    private AlertDialog.Builder builder;
    private Context context;
    private LayoutInflater inflater;
    private View layout;
    private AlertDialog alertDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        commentsText = (EditText) findViewById(R.id.commentstext);
        instructorService = new InstructorService();
        context = getApplicationContext();
        builder = new AlertDialog.Builder(this);
        inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
//

    }

    public void submitRatingComments(View click) {
        try {
            Bundle details = getIntent().getExtras();
            final int selectedProfessorId = details.getInt("selectedInstructorId");
            int iRating = (int) ratingBar.getRating();

            String rating = new Integer(iRating).toString();
            String comments = commentsText.getText().toString();

            if (rating.equals("0") && (comments.equals(""))) {
                Toast.makeText(this, "Enter rating and comments",
                        Toast.LENGTH_SHORT).show();
            } else if (rating.equals("0") && (!comments.equals(""))) {
                Toast.makeText(this, "Enter a rating", Toast.LENGTH_SHORT)
                        .show();
            } else {
                HttpResponse instructorCommentsResponse = instructorService
                        .submitInstructorComments(selectedProfessorId, comments);
                HttpResponse professorRatingResponse = instructorService
                        .submitInstructorRating(selectedProfessorId, rating);

                if ((instructorCommentsResponse.getStatusLine().getStatusCode()) == 200
                        && (professorRatingResponse.getStatusLine()
                        .getStatusCode()) == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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


                if ((instructorCommentsResponse.getStatusLine().getStatusCode()) == 501
                        && (professorRatingResponse.getStatusLine()
                        .getStatusCode() == 501)) {
                    Toast.makeText(
                            this,
                            "Sorry unable to connect to the network. Try again later.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            Toast.makeText(this,
                    "Sorry for the inconvenience. Try again later.",
                    Toast.LENGTH_SHORT).show();
            Log.e("RateMyProfessor", e.getMessage(), e);
        }
    }

}
