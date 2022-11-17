package com.example.november;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Questions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // hiding action bar
        getSupportActionBar().hide();

        // hiding status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        // firebse
        TextView question = findViewById(R.id.textView);
        RadioButton qs1 = findViewById(R.id.radioButton);
        RadioButton qs2 = findViewById(R.id.radioButton2);
        RadioButton qs3 = findViewById(R.id.radioButton3);
        RadioButton qs4 = findViewById(R.id.radioButton4);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://november-a9a10-default-rtdb.firebaseio.com/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.child("Java").getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    // disable backPress Button
    @Override
    public void onBackPressed() {
        Toast.makeText(Questions.this,"For previous question press previous Button !",Toast.LENGTH_SHORT).show();
    }
}