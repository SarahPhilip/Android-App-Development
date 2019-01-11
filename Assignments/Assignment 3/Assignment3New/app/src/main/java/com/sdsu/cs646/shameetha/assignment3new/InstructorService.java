package com.sdsu.cs646.shameetha.assignment3new;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Shameetha on 3/12/15.
 */
public class InstructorService {
    private JSONToInstructorObject JSONInstructor;

    public InstructorService() {
        JSONInstructor = new JSONToInstructorObject();
    }

    public List<Instructor> getInstructorList() throws InterruptedException,
            ExecutionException {
        List<Instructor> instructorArrayList = new ArrayList<Instructor>();
        String url = "http://bismarck.sdsu.edu/rateme/list";
        GETNetworkConnection networkConnection = new GETNetworkConnection();
        String responseBody = networkConnection.execute(url).get();
        if (responseBody != null) {
            JSONArray jsonInstructorArray;
            try {
                jsonInstructorArray = new JSONArray(responseBody);
                instructorArrayList.addAll(JSONInstructor
                        .jsonInstructorArrayToList(jsonInstructorArray));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instructorArrayList;
    }

    public Instructor getInstructorDetails(int selectedInstructorId)
            throws JSONException, InterruptedException, ExecutionException {
        String url = "http://bismarck.sdsu.edu/rateme/instructor/"
                + selectedInstructorId;
        GETNetworkConnection networkConnection = new GETNetworkConnection();
        String responseBody = networkConnection.execute(url).get();
        JSONObject jsonObject = new JSONObject(responseBody);
        Instructor instructorDetails = new Instructor();
        instructorDetails = JSONInstructor
                .convertJsonObjectToInstructor(jsonObject);
        return instructorDetails;
    }

    public List<Instructor> getInstructorComments(int selectedInstructorId)
            throws InterruptedException, ExecutionException, JSONException {
        String url = "http://bismarck.sdsu.edu/rateme/comments/"
                + selectedInstructorId;
        GETNetworkConnection networkConnection = new GETNetworkConnection();
        String responseBody = networkConnection.execute(url).get();
        JSONArray jsonArrayComments = new JSONArray(responseBody);
        JSONToInstructorObject instructorObject = new JSONToInstructorObject();
        List<Instructor> commentsList = new ArrayList<Instructor>();
        commentsList.addAll(instructorObject
                .getSelectedInstructorCommentsArrayToList(jsonArrayComments));
        return commentsList;
    }

    public HttpResponse submitInstructorComments(int selectedInstructorId,
                                                 String comments) throws InterruptedException, ExecutionException {
        String url = "http://bismarck.sdsu.edu/rateme/comment/"
                + selectedInstructorId;
        POSTNetworkConnection netowrkConnection = new POSTNetworkConnection();
        HttpResponse httpResponse = netowrkConnection.execute(url, comments)
                .get();
        return httpResponse;
    }

    public HttpResponse submitInstructorRating(int selectedInstructorId,
                                               String rating) throws InterruptedException, ExecutionException {
        String url = "http://bismarck.sdsu.edu/rateme/rating/"
                + selectedInstructorId + "/" + rating;
        POSTNetworkConnection networkConnection = new POSTNetworkConnection();
        HttpResponse httpResponse = networkConnection.execute(url, rating)
                .get();
        return httpResponse;

    }

    public Instructor getInstructorRating(int selectedInstructorId, String rating)
            throws InterruptedException, ExecutionException, JSONException {
        String url = "http://bismarck.sdsu.edu/rateme/rating/"
                + selectedInstructorId + "/" + rating;
        GETNetworkConnection networkConnection = new GETNetworkConnection();
        String responseBody = networkConnection.execute(url).get();
        JSONObject jsonObject = new JSONObject(responseBody);
        Instructor instructorDetails = new Instructor();
        instructorDetails = JSONInstructor.getRating(jsonObject);
        return instructorDetails;
    }
}
