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


public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView textViewStartPrep;
    TextView name;
    TextView email;
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

        textViewStartPrep = findViewById(R.id.StartPreparation);
        name = findViewById(R.id.textViewName);
        email = findViewById(R.id.textViewEmail);
        textViewNIC = findViewById(R.id.textViewNIC);
        imageViewNIC = findViewById(R.id.imageViewNIC);
        textViewTryAgain = findViewById(R.id.textViewTryAgain);

        // checking internet connection
        isNetworkAvailable();

        // try again button on click
        textViewTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkAvailable();
            }
        });


        // start preparation
        textViewStartPrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Questions.class);
                startActivity(intent);
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
    private void isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        boolean bool = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(bool){
            //name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            imageViewNIC.setVisibility(View.GONE);
            textViewNIC.setVisibility(View.GONE);
            textViewTryAgain.setVisibility(View.GONE);

            mAuth = FirebaseAuth.getInstance();

            if(isLoggeddIn()){
                textViewStartPrep.setVisibility(View.VISIBLE);
                email.setText("Name : "+mAuth.getCurrentUser().getEmail());
            }
            else{
                startActivity(new Intent(Home.this,Login.class));
            }
        }else{
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            textViewStartPrep.setVisibility(View.GONE);
            imageViewNIC.setVisibility(View.VISIBLE);
            textViewNIC.setVisibility(View.VISIBLE);
            textViewTryAgain.setVisibility(View.VISIBLE);
        }
    }
}