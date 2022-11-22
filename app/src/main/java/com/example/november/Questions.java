package com.example.november;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Questions extends AppCompatActivity {

    int questionNo;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    int solved = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // hiding action bar
        getSupportActionBar().hide();

        // hiding status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://november-a9a10-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();

        questionNo = 0;
        requestQuestion(questionNo);

    }
    // request for question
    protected void requestQuestion(int questionNo){
        // firebse

        TextView question = findViewById(R.id.textView);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        Button previous = findViewById(R.id.buttonPrevious);
        Button next = findViewById(R.id.buttonNext);
        Button submit = findViewById(R.id.buttonSubmit);



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot dataSnapshot = snapshot.child("Java").child(questionNo+"");

                progressBar.setVisibility(View.GONE);
                question.setText(dataSnapshot.child("Question").getValue(String.class));
                String answer = dataSnapshot.child("Answer").getValue(String.class);

                radioGroup.removeAllViews();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.child("options").getChildren()){
                    RadioButton qs = new RadioButton(Questions.this);
                    qs.setPadding(20,20,20,20);
                    qs.setText(dataSnapshot1.getValue(String.class));
                    radioGroup.addView(qs);
                }


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (radioGroup.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(Questions.this,"Please select any answer",Toast.LENGTH_SHORT).show();
                        } else {
                            RadioButton pressedBtn = findViewById(radioGroup.getCheckedRadioButtonId());
                            String pressedAns = pressedBtn.getText().toString();
                            onSubmitButton(pressedAns,answer);
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Questions.this,"Check your internet connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    // submit button
    protected void onSubmitButton(String pressedAns, String answer){


        questionNo++;

        String userUID = mAuth.getCurrentUser().getUid().toString();

        if(pressedAns.equals(answer)){
            solved++;
            databaseReference.child("users").child(userUID).child("submissions").child(questionNo-1+"").setValue(1);
        }
        else{
            databaseReference.child("users").child(userUID).child("submissions").child(questionNo-1+"").setValue(0);
        }
        if(questionNo>4){
            Intent intent = new Intent(Questions.this,Result.class);
            intent.putExtra("total",5);
            intent.putExtra("solved",solved);
            startActivity(intent);
        } else {
            requestQuestion(questionNo);
        }

    }

    // disable backPress Button
    @Override
    public void onBackPressed() {
        Toast.makeText(Questions.this,"For previous question press previous Button !",Toast.LENGTH_SHORT).show();
    }
}