package com.unifiedtnc.belview_foods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePassword extends AppCompatActivity {
EditText etEmail,etNewPass,etOldPass;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etEmail=findViewById(R.id.etEmail);
        etNewPass=findViewById(R.id.etNewPass);
        etOldPass=findViewById(R.id.etOldPass);
        toolbar = findViewById(R.id.toolbar2);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    void updatePassword(String email, String password, final String newPass){
         progressDialog.show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        senddata(user.getUid(),newPass);
                                        Toast.makeText(UpdatePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "Password updated");
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(UpdatePassword.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Log.d("TAG", "Error auth failed");
                        }
                    }
                });

    }

    public void resetPassword(View view) {

        if(etEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }else if(etNewPass.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show();
            return;
        }else if(etOldPass.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter old password", Toast.LENGTH_SHORT).show();
            return;
        }

        updatePassword(etEmail.getText().toString(),etOldPass.getText().toString(),etNewPass.getText().toString());
    }
    private void senddata(String uuid, String password)
    {


        FirebaseDatabase.getInstance().getReference("Users").child(uuid).child("password").setValue(password, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Data Not saved");

                } else {
                    Toast.makeText(UpdatePassword.this, "Profile  created successfully", Toast.LENGTH_SHORT).show();
                    Log.e("ss", "Data saved successfully");
//                    progressDialog.dismiss();
                }
            }
        });

    }

}