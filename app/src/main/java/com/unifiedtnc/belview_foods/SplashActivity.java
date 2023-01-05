package com.unifiedtnc.belview_foods;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
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

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//     statusBar removal
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

//        textView=findViewById(R.id.text);
         sharedPreferences=getSharedPreferences(getPackageName(),MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*3);
                   if(sharedPreferences.contains("user")){
                       if(sharedPreferences.getString("user","user").equals("admin")){
                           startActivity(new Intent(SplashActivity.this, AdminLandingActivity.class));
                       }else{
                           if(sharedPreferences.contains("email")) {
                               LoginUser(sharedPreferences.getString("email", null), sharedPreferences.getString("password", null));
                           }else{
                               startActivity(new Intent(SplashActivity.this,LoginActivity.class).putExtra("status","user"));
                               finish();
                           }
                       }

                   }else {
                       startActivity(new Intent(SplashActivity.this, MainActivity.class));
                       finish();
                   }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
    private void LoginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            fetchUserData(user.getUid());
                            startActivity(new Intent(SplashActivity.this, Dashboard.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SplashActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

}