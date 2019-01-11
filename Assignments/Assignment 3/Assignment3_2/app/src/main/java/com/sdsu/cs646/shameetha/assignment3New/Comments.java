package com.sdsu.cs646.shameetha.assignment3New;

/**
 * Created by Shameetha on 3/14/15.
 */
public class Comments {
    private long id;
    private String text;
    private String date;
    private long professorId;

    public Comments() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(long professorId) {
        this.professorId = professorId;
    }

    @Override
    public String toString() {
        return date + "\n" + text;
    }
}
