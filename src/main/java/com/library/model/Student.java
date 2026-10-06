package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private String studentId;
    private String name;
    private String email;
    private List<String> bookIds;

    // Constructor
    public Student(String studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.bookIds = new ArrayList<>();
    }

    // Getters

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getBookIds() {
        return bookIds;
    }

    // Setters

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBookIds(List<String> bookIds) {
        this.bookIds = bookIds;
    }
}


