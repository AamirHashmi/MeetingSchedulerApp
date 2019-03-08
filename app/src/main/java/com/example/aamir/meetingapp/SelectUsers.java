package com.example.aamir.meetingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectUsers extends AppCompatActivity {

    private RecyclerView FindUsersRecyclerList;
    private DatabaseReference UsersRef;
    private List<String>invitees;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users);

        invitees = new ArrayList();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FindUsersRecyclerList = (RecyclerView) findViewById(R.id.find_users_recycler_list);
        FindUsersRecyclerList.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(UsersRef, Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts, FindUsersViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, FindUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindUsersViewHolder holder, final int position, @NonNull Contacts model) {
                holder.userName.setText(model.getName());
                holder.userStatus.setText(model.getStatus());
                Picasso.get().load(model.getImage()).into(holder.profileImage);

                final String toastName = model.getName();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();
                        //invitees.add(visit_user_id);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", visit_user_id);
                        setResult(RESULT_OK, returnIntent);
                        Toast.makeText(SelectUsers.this, "" + toastName + " Invited", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
            }

            @NonNull
            @Override
            public FindUsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                FindUsersViewHolder viewHolder = new FindUsersViewHolder(view);
                return viewHolder;
            }
        };

        FindUsersRecyclerList.setAdapter(adapter);

        adapter.startListening();
    }


    public static class FindUsersViewHolder extends RecyclerView.ViewHolder{
        TextView userName, userStatus;
        CircleImageView profileImage;


        public FindUsersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }
}
