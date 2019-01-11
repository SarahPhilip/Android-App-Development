package com.sdsu.cs646.shameetha.assignment3new;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shameetha on 3/11/15.
 */
public class JSONToInstructorObject {

    public List<Instructor> jsonInstructorArrayToList(JSONArray jsonArray) {
        List<Instructor> instructorArrayList = new ArrayList<Instructor>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject instructorJsonObject = (JSONObject) jsonArray.get(i);
                Instructor instructor = convertJsonObjectToInstructor(instructorJsonObject);
                instructorArrayList.add(instructor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return instructorArrayList;
    }

    public Instructor convertJsonObjectToInstructor(JSONObject instructorJsonObject)
            throws JSONException {
        Instructor instructor = new Instructor();
        if (instructorJsonObject.getInt("id") != 0) {
            instructor.setId(instructorJsonObject.getInt("id"));
        }
        if (!instructorJsonObject.isNull("firstName")) {
            instructor.setFirstName(instructorJsonObject.getString("firstName"));
        }
        if (!instructorJsonObject.isNull("lastName")) {
            instructor.setLastName(instructorJsonObject.getString("lastName"));
        }
        if (!instructorJsonObject.isNull("office")) {
            instructor.setOffice(instructorJsonObject.getString("office"));
        }
        if (!instructorJsonObject.isNull("phone")) {
            instructor.setPhone(instructorJsonObject.getString("phone"));
        }
        if (!instructorJsonObject.isNull("email")) {
            instructor.setEmail(instructorJsonObject.getString("email"));
        }
        if (!instructorJsonObject.isNull("rating")) {
            JSONObject instructorSelectedRemarks = instructorJsonObject
                    .getJSONObject("rating");
            Float floatAverage = new Float(
                    instructorSelectedRemarks.getString("average"));
            instructor.setAverage(floatAverage);
            Integer intTotalRatings = new Integer(
                    instructorSelectedRemarks.getString("totalRatings"));
            instructor.setTotalRatings(intTotalRatings);
        }
        return instructor;
    }

    public List<Instructor> getSelectedInstructorCommentsArrayToList(
            JSONArray jsonArrayComments) throws JSONException {
        List<Instructor> instructorCommentsList = new ArrayList<Instructor>();

        JSONObject instructorCommentsObject = new JSONObject();
        for (int i = 0; i < jsonArrayComments.length(); i++) {
            Instructor instructorSelectedComments = new Instructor();
            instructorCommentsObject = jsonArrayComments.getJSONObject(i);
            instructorSelectedComments.setId(instructorCommentsObject
                    .getInt("id"));
            instructorSelectedComments.setComment(instructorCommentsObject
                    .getString("text"));
            instructorSelectedComments.setDate(instructorCommentsObject
                    .getString("date"));
            instructorCommentsList.add(instructorSelectedComments);
        }
        return instructorCommentsList;
    }

    public Instructor getRating(JSONObject jsonRating)
            throws NumberFormatException, JSONException {
        Instructor professorRating = new Instructor();
        Float fAverage = new Float(jsonRating.getString("average"));
        professorRating.setAverage(fAverage);
        Integer totalRatings = new Integer(jsonRating.getString("totalRatings"));
        professorRating.setTotalRatings(totalRatings);
        return professorRating;

    }
}
