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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        checkUserLogin();
        loginFunction();
        createAccount();
    }


    /**
     * checks if user have login in before
     */
    private void checkUserLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
//            Toast.makeText(LoginActivity.this, getString(R.string.login_with) +" "+ user.getEmail(),
//                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }


    /**
     * Login using firebase
     * success go to main
     * failed show message
     */
    public void loginFunction() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty())
                    Toast.makeText(LoginActivity.this, getString(R.string.fill_data),
                            Toast.LENGTH_SHORT).show();
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
//                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        Toast.makeText(LoginActivity.this, getString(R.string.login_with) +" "+ user.getEmail(),
                                                Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display wordsArray message to the user.
                                        Log.e(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void createAccount() {
        Button create = findViewById(R.id.buttonIntentRegisterActivity);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();

            }
        });
    }

}
