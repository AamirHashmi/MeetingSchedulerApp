package com.example.aamir.meetingapp;

import java.sql.Time;
import java.util.Date;

public class Meeting {

  private String name;
  private TimeSlot timeSlot;
  private String description;

    public Meeting(){}

    public Meeting(String name, String description, TimeSlot tm){
        this.name = name;
        this.description = description;
        this.timeSlot = tm;

        }



    public void setName(String newName){
        this.name = newName;
    }

    public String getName(){
        return name;
    }


    public void setDescription(String newDesc){
        this.description = newDesc;
    }

    public String getDescription(){
        return description;
    }

    public void setTimeSlot(TimeSlot newTm){
        this.timeSlot = newTm;
    }

    public TimeSlot getTimeSlot(){
        return this.timeSlot;
    }


}
