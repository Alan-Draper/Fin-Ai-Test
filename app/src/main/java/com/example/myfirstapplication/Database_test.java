package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database_test extends AppCompatActivity {


    TextView mtv;
    Button mb1, mb2, mb3;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mtvRef = mRootRef.child("Condition");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        mtv = findViewById(R.id.tv);
        mb1 = findViewById(R.id.b);
        mb2 = findViewById(R.id.b2);
    }

    protected void onStart() {
        super.onStart();

        mtvRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mtv.setText(text);
                Toast.makeText(Database_test.this, "Database Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtvRef.setValue(mb1.getText());
            }
        });

        mb2.setOnClickListener(v -> {
            mtvRef.setValue(mb2.getText());
        });

    }

}