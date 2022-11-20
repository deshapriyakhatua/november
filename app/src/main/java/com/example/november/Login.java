package com.example.november;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button buttonLogIn;
    private Button buttonSignUp;
    private TextView signUp;
    private TextView logIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hiding action bar
        getSupportActionBar().hide();

        // hiding status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        signUp = findViewById(R.id.textViewSignUp);
        logIn = findViewById(R.id.textViewLogIn);
        mAuth = FirebaseAuth.getInstance();

        // log in
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(!emailText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    if(passwordText.length()>=8){

                        // sending data to firebase
                        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Login.this, "Signed in successfully", Toast.LENGTH_LONG).show();
                                        //startActivity(new Intent(Login.this, Home.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, "Signed in failed : "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                    else{ Toast.makeText(Login.this,"Password is invalid",Toast.LENGTH_SHORT).show(); password.setError("Password includes at least 8 characters"); }
                }
                else{ Toast.makeText(Login.this,"invalid Email Address",Toast.LENGTH_SHORT).show(); email.setError("Email should be valid !!"); }
            }
        });



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonLogIn.setVisibility(View.GONE);
                signUp.setVisibility(View.GONE);
                buttonSignUp.setVisibility(View.VISIBLE);
                logIn.setVisibility(View.VISIBLE);
            }
        });



        // sign up
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(!emailText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    if(passwordText.length()>=8){

                        // sending data to firebase
                        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        // send email
                                        mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(Login.this, "Signed up successfully and verification mail sent", Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Login.this, "Signed up successfully and verification mail not sent", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                        // creating user database
                                        String uid = mAuth.getCurrentUser().getUid().toString();
                                        User user = new User();

                                        DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://november-a9a10-default-rtdb.firebaseio.com/users/");
                                        database.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Login.this,"user database Successfully created",Toast.LENGTH_LONG).show();
                                                // start Home Activity
                                                //Intent intent = new Intent(Login.this,Home.class);
                                                //startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Login.this,"user database creation failed : "+e.getMessage().toString(),Toast.LENGTH_LONG).show();
                                            }
                                        });


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, "Sign up failed : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                    else{ Toast.makeText(Login.this,"Password is invalid",Toast.LENGTH_SHORT).show(); password.setError("Password includes at least 8 characters"); }
                }
                else{ Toast.makeText(Login.this,"invalid Email Address",Toast.LENGTH_SHORT).show(); email.setError("Email should be valid !!"); }

            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSignUp.setVisibility(View.GONE);
                logIn.setVisibility(View.GONE);
                buttonLogIn.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.VISIBLE);
            }
        });

    }


    // creating user database object class

    public class User{
        HashMap<String,Integer> submissions;
        HashMap<String,String> user;
        public User(){
            submissions = new HashMap<>();
            user = new HashMap<>();
        }
    }

}

