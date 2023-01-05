package com.unifiedtnc.belview_foods;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText etUsername, etPassword, etEmail, etName, etConfirmPass;
    TextView btnSignUp, txtLogin;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        getSupportActionBar().setTitle("Sign Up");
        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnSignUp = findViewById(R.id.btnsignUp);
        etConfirmPass = findViewById(R.id.edtConpassword);
        txtLogin = findViewById(R.id.txtLogin);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class).putExtra("status", "user"));
                finish();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Enter your name");
                    return;
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Enter password");
                    return;
                } else if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Enter Email");
                    return;
                } else if (!(etConfirmPass.getText().toString()).equals(etPassword.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this, "Password does not Matched", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                saveData(etPassword.getText().toString(), etEmail.getText().toString());


            }
        });

    }

    private void saveData(String password, final String email) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String TAG = "";
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // email sent
                                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                                senddata(currentUser.getUid(),
                                                        etName.getText().toString().trim(),
                                                        etEmail.getText().toString().trim(),
                                                        etPassword.getText().toString().trim());
                                                // after email is sent just logout the user and finish this activity
//                                                                       FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class).putExtra("status", "user"));
                                                finish();
                                            } else {
                                                // email not sent, so display message and restart the activity or do whatever you wish to do

                                                //restart this activity
                                                overridePendingTransition(0, 0);
                                                finish();
                                                overridePendingTransition(0, 0);
                                                startActivity(getIntent());

                                            }

                                        }
                                    });


//                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    private void senddata(String uuid, String name, String email, String password) {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        UserProfileModel userProfileModel = new UserProfileModel(
                name,
                password,
                email
        );

        FirebaseDatabase.getInstance().getReference("Users").child(uuid).setValue(userProfileModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Data Not saved");
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(SignUpActivity.this, "Profile  created successfully", Toast.LENGTH_SHORT).show();
                    Log.e("ss", "Data saved successfully");
//                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

}