package com.inocen.gojepang.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inocen.gojepang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private final String TAG = "Register";
    private EditText editTextUsername, editTextEmail, editTextPhone, editTextPasswordConfirmation, editTextAge, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        registerFunction();
    }


    /**
     * Register using firebase
     * success go to MenuActivity
     * failed show toast
     */
    public void registerFunction() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirmation = findViewById(R.id.editTextPasswordConfirmation);
        editTextAge = findViewById(R.id.editTextAge);

        Button login = findViewById(R.id.buttonRegister);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String passwordConfirmation = editTextPasswordConfirmation.getText().toString().trim();
                int age=0;
                String ageCheck=editTextAge.getText().toString();
                if(!ageCheck.isEmpty())
                    age = Integer.parseInt(ageCheck);

                final User user = new User(username, email, phone, age);

                //creating user in firebase database, checking empty values
                if (email.isEmpty() || password.isEmpty() || age==0)
                    Toast.makeText(RegisterActivity.this, getString(R.string.fill_data),
                        Toast.LENGTH_SHORT).show();
                else if(phone.length()<9){
                    Toast.makeText(RegisterActivity.this, getString(R.string.phone_more_9),
                            Toast.LENGTH_SHORT).show();
                }
                else if (password.equals(passwordConfirmation)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                        createNewUser(task.getResult().getUser(), user);

                                        startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
                                        finish();
                                    } else {
                                        Log.e(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else Toast.makeText(RegisterActivity.this, getString(R.string.password_mismatch),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * @param firebaseUser
     * @param user
     * add user to database for additional values like phone number and age
     */
    private void createNewUser(FirebaseUser firebaseUser, User user) {
        String userId = firebaseUser.getUid();
        mDatabase.child("users").child(userId).setValue(user);
    }
}
