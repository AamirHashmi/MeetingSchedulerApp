package com.example.aamir.meetingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class exclusion_list extends ArrayAdapter {

    private List<TimeSlot> exs;
    private int resource;
    private Context context;

    public exclusion_list(Context context, int resource, List<TimeSlot> exclusions) {
        super(context, resource, exclusions);
        this.context = context;
        this.exs = exclusions;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TimeSlot eset = exs.get(position);

        View view = LayoutInflater.from(context).inflate(resource, parent, false);

        TextView pDay = view.findViewById(R.id.exDay);
        TextView pTime = view.findViewById(R.id.exTime);

        pDay.setText(eset.getDay());
        pTime.setText(eset.getTime());

        return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return exs.get(position);
    }
}
