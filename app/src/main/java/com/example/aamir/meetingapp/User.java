package com.example.aamir.meetingapp;

import java.util.ArrayList;

public class User {
    private String name;
    private String status;
    private String uid;
    private String image;
    private ArrayList<Meeting> meetings;
    private ArrayList<TimeSlot> preferenceSet;
    private ArrayList<TimeSlot> exclusionSet;
    
    public User(String name){

        this.name = name;
    }
}
