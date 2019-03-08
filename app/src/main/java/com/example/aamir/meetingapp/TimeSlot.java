package com.example.aamir.meetingapp;



public class TimeSlot {

    private String day;
    private String time;
    private boolean Pref;

    public TimeSlot(String day, String time){
        this.day = day;
        this.time = time;
    }

    public void setDay(String newDay){
        this.day = newDay;
    }

    public String getDay(){
        return day;
    }

    public void setTime(String newTime){
        this.time = newTime;
    }

    public String getTime(){
        return time;
    }

    public void setPref(boolean b){
        this.Pref = b;
    }

    public boolean isPref() {
        return Pref;
    }
    public String getSPref(){
        if(Pref == true) {
            return "Preference";
        }
        else{
            return "";
        }
    }
}
