package com.example.november;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // hiding action bar
        getSupportActionBar().hide();

        // hiding status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        Intent intent = getIntent();
        int total = intent.getIntExtra("total",0);
        int solved = intent.getIntExtra("solved",0);

        TextView result = findViewById(R.id.textViewResult);
        result.setText(solved+" / "+total);

        Button home = findViewById(R.id.buttonHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Result.this,Home.class));
            }
        });
    }
}