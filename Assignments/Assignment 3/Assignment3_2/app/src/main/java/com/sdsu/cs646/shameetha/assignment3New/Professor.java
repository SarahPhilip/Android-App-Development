package com.sdsu.cs646.shameetha.assignment3New;

import java.io.Serializable;

/**
 * Created by Shameetha on 3/12/15.
 */
public class Professor implements Serializable {

    public static final String TAG = "Professor";
    private static final long serialVersionUID = -7406082437623008161L;

    private long id;
    private String firstName;
    private String lastName;
    private String office;
    private String email;
    private String phone;
    private String rating;
    private Float average;
    private int totalRatings;
    private String comment;
    private String date;

    public Professor(long id, String firstName, String lastName, String office, String email, String phone, Float average, int totalRatings) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.office = office;
        this.email = email;
        this.phone = phone;
        this.average = average;
        this.totalRatings = totalRatings;
    }

    public Professor() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
