package com.unifiedtnc.belview_foods;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView textView, btnLogin, btnSigunp;
    ProgressDialog progressDialog;
    String userType;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userType = getIntent().getStringExtra("status");
         sharedPreferences=getSharedPreferences(getPackageName(),MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnlogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtpassword);
        btnSigunp = findViewById(R.id.btnsignUp);
        textView = findViewById(R.id.txt);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);

        btnSigunp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().toString().isEmpty() ||edtPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email or Password is Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user", userType);
                    editor.apply();
                    progressDialog.show();
                    if (getIntent().getStringExtra("status").equals("admin")) {
                        if (edtEmail.getText().toString().equals("admin@gmail.com") && edtPassword.getText().toString().equals("admin")) {
                            startActivity(new Intent(LoginActivity.this, AdminLandingActivity.class));
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Admin email or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                 code here for firebase login
                        LoginUser(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim());

                    }
                }

            }
        });
        if (getIntent().getStringExtra("status").equals("admin")) {

            btnSigunp.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);

        }
    }

    private void LoginUser(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
//                            if(user.isEmailVerified()) {
                            fetchUserData(user.getUid());
                            startActivity(new Intent(LoginActivity.this, Dashboard.class));
                            progressDialog.dismiss();
//                            }else{
//                                progressDialog.dismiss();
//                                Toast.makeText(LoginActivity.this, "Please Verified your email first", Toast.LENGTH_SHORT).show();
//                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }

    private void fetchUserData(String uuid) {

//        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Users").child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AppData.Key = snapshot.getKey();
                AppData.name = snapshot.child("name").getValue(String.class);
                AppData.email = snapshot.child("email").getValue(String.class);
                AppData.password = snapshot.child("password").getValue(String.class);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("email",  snapshot.child("email").getValue(String.class));
                editor.putString("password",  snapshot.child("password").getValue(String.class));
                editor.putString("name",  snapshot.child("name").getValue(String.class));
                editor.apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

}