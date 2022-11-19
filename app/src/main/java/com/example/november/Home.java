package com.example.november;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // hiding action bar
        getSupportActionBar().hide();

        // hiding status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        mAuth = FirebaseAuth.getInstance();

        // start another activity
        TextView textView = findViewById(R.id.StartPreparation);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLoggeddIn()){
                    Intent intent = new Intent(Home.this,Questions.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Home.this,Login.class);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean isLoggeddIn() {
        return mAuth.getCurrentUser() != null;
    }

    // disable backPress Button
    @Override
    public void onBackPressed() {
        Toast.makeText(Home.this,"Already in Home Screen",Toast.LENGTH_SHORT).show();
    }
}