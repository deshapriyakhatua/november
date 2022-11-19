package com.example.november;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Questions extends AppCompatActivity {

    int questionNo;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // hiding action bar
        getSupportActionBar().hide();

        // hiding status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        questionNo = 0;
        requestQuestion(questionNo);

    }
    // request for question
    protected void requestQuestion(int questionNo){
        // firebse

        TextView question = findViewById(R.id.textView);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        Button previous = findViewById(R.id.buttonPrevious);
        Button next = findViewById(R.id.buttonNext);
        Button submit = findViewById(R.id.buttonSubmit);


        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://november-a9a10-default-rtdb.firebaseio.com/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot dataSnapshot = snapshot.child("Java").child(questionNo+"");

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
        if(pressedAns.equals(answer)){
            questionNo++;
            if(questionNo>4){
                Intent intent = new Intent(Questions.this,Home.class);
                startActivity(intent);
            }
            else{
                requestQuestion(questionNo);
            }
        }
        else{
            Toast.makeText(Questions.this,"Wrong answer",Toast.LENGTH_SHORT).show();
        }
    }

    // disable backPress Button
    @Override
    public void onBackPressed() {
        Toast.makeText(Questions.this,"For previous question press previous Button !",Toast.LENGTH_SHORT).show();
    }
}