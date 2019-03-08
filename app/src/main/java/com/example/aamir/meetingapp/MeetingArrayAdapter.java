package com.example.aamir.meetingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MeetingArrayAdapter extends ArrayAdapter {

    private List<Meeting> meetings;
    private int resource;
    private Context context;

    public MeetingArrayAdapter(Context context, int resource, List<Meeting> meetings) {
        super(context, resource, meetings);
        this.context = context;
        this.meetings = meetings;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        Meeting meetingP = meetings.get(position);

        View view = LayoutInflater.from(context).inflate(resource, parent, false);

        TextView meetingName = view.findViewById(R.id.meetingName);
        TextView meetingDesc = view.findViewById(R.id.meetingDesc);
        TextView meetingSlotDay = view.findViewById(R.id.timeSlotDay);
        TextView meetingSlotTime = view.findViewById(R.id.timeSlotTime);

        meetingName.setText(meetingP.getName());
        meetingDesc.setText(meetingP.getDescription());
        meetingSlotDay.setText(meetingP.getTimeSlot().getDay());
        meetingSlotTime.setText(meetingP.getTimeSlot().getTime());

       return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return meetings.get(position);
    }
}
