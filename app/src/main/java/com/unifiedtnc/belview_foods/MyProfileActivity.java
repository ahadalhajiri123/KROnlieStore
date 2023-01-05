package com.unifiedtnc.belview_foods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {

    TextView etUsername,etPassword,etEmail,etName,etConfirmPass;
    Toolbar toolbar;
    TextView btnSignUp;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        etName=findViewById(R.id.etName);
        toolbar = findViewById(R.id.toolbar2);
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        etEmail=findViewById(R.id.etEmail);
        btnSignUp=findViewById(R.id.btnsignUp);
        etConfirmPass=findViewById(R.id.edtConpassword);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchProfiledata();

    }

    private void fetchProfiledata() {
progressDialog.setTitle("loading data");
progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.getKey().equals(AppData.Key)){

                        String name=dataSnapshot.child("name").getValue(String.class);
                        String email=dataSnapshot.child("email").getValue(String.class);
                        String passwoerd=dataSnapshot.child("password").getValue(String.class);
                          etName.setText(name);
                          etEmail.setText(email);
                          etPassword.setText(passwoerd);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void back1(View view) {
        startActivity(new Intent(MyProfileActivity.this,Dashboard.class));
        finish();
    }
}