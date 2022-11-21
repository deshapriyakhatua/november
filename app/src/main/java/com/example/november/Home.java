package com.example.november;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView textView;
    TextView textViewNIC;
    ImageView imageViewNIC;
    TextView textViewTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // hiding action bar
        getSupportActionBar().hide();

        // hiding status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        textView = findViewById(R.id.StartPreparation);
        textViewNIC = findViewById(R.id.textViewNIC);
        imageViewNIC = findViewById(R.id.imageViewNIC);
        textViewTryAgain = findViewById(R.id.textViewTryAgain);

        // checking internet connection
        if(isNetworkAvailable()){
            textView.setVisibility(View.VISIBLE);
            imageViewNIC.setVisibility(View.GONE);
            textViewNIC.setVisibility(View.GONE);
            textViewTryAgain.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.GONE);
            imageViewNIC.setVisibility(View.VISIBLE);
            textViewNIC.setVisibility(View.VISIBLE);
            textViewTryAgain.setVisibility(View.VISIBLE);
        }


        // try again button on click
        textViewTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()){
                    textView.setVisibility(View.VISIBLE);
                    imageViewNIC.setVisibility(View.GONE);
                    textViewNIC.setVisibility(View.GONE);
                    textViewTryAgain.setVisibility(View.GONE);
                }else{
                    textView.setVisibility(View.GONE);
                    imageViewNIC.setVisibility(View.VISIBLE);
                    textViewNIC.setVisibility(View.VISIBLE);
                    textViewTryAgain.setVisibility(View.VISIBLE);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        // checking if logged in
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

    // checking if user logged in
    private boolean isLoggeddIn() {
        return mAuth.getCurrentUser() != null;
    }

    // disable backPress Button
    @Override
    public void onBackPressed() {
        Toast.makeText(Home.this,"Already in Home Screen",Toast.LENGTH_SHORT).show();
    }

    // checking internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}