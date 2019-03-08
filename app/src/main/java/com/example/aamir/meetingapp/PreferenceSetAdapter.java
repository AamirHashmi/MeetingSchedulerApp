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

public class PreferenceSetAdapter extends ArrayAdapter {

    private List<TimeSlot> prefs;
    private int resource;
    private Context context;

    public PreferenceSetAdapter(Context context, int resource, List<TimeSlot> prefs) {
        super(context, resource, prefs);
        this.context = context;
        this.prefs = prefs;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        TimeSlot pset = prefs.get(position);

        View view = LayoutInflater.from(context).inflate(resource, parent, false);

        TextView pDay = view.findViewById(R.id.prefDay);
        TextView pTime = view.findViewById(R.id.prefTime);
        TextView isP = view.findViewById(R.id.isPref);

        pDay.setText(pset.getDay());
        pTime.setText(pset.getTime());
        isP.setText(pset.getSPref());

        return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return prefs.get(position);
    }
}
