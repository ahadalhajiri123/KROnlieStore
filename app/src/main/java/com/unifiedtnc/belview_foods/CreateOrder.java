package com.unifiedtnc.belview_foods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateOrder extends AppCompatActivity {
    EditText etAddress,etName,etPhoneNo,editCardNo,edtCardType,editCVV,editCountry;
    Button btnConfirm;
    RadioButton radioButton,radioButton1,imediatcheck,normalchek;
    TextView getMylocaionbtn,showLocation;
    String cardType="";
    String deliveryTye="";
    RadioGroup radioGroup,radioGroup2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //     etAddress=findViewById(R.id.etAddress);
        btnConfirm=findViewById(R.id.btnPay);
        etName=findViewById(R.id.etName);
        etPhoneNo=findViewById(R.id.etPhoneNo);
        etAddress=findViewById(R.id.etAddAdress);
        editCardNo=findViewById(R.id.editTextTextPersonName);
        edtCardType=findViewById(R.id.editTextTextPersonName2);
        editCVV=findViewById(R.id.editTextTextPersonName3);
        editCountry=findViewById(R.id.editTextTextPersonName4);
        getMylocaionbtn=findViewById(R.id.textView3);
        showLocation=findViewById(R.id.textView4);
        radioButton=findViewById(R.id.radio1);
        radioButton1=findViewById(R.id.radio2);
        imediatcheck=findViewById(R.id.imediate);
        normalchek=findViewById(R.id.normal);
        radioGroup=findViewById(R.id.radioGroup);

radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
       if (radioButton.isChecked()){
           editCardNo.setVisibility(View.VISIBLE);
           edtCardType.setVisibility(View.VISIBLE);
           editCVV.setVisibility(View.VISIBLE);
           editCountry.setVisibility(View.VISIBLE);
           cardType="Debit/Credit Card";
        }else
            if (radioButton1.isChecked()){
                editCardNo.setVisibility(View.GONE);
                edtCardType.setVisibility(View.GONE);
                editCVV.setVisibility(View.GONE);
                editCountry.setVisibility(View.GONE);
                cardType="Cash On Delivery";
       }
    }
});


        getMylocaionbtn.setVisibility(View.VISIBLE);
        showLocation.setVisibility(View.GONE);
        getMylocaionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsTracker gpsTracker = new GpsTracker(CreateOrder.this);
                if (gpsTracker.canGetLocation()) {
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    try {
                        String address=getAddress(latitude,longitude);
                        etAddress.setText(address);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    gpsTracker.showSettingsAlert();
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (normalchek.isChecked()){
                    deliveryTye="Normal";
                }else if (imediatcheck.isChecked()){
                    deliveryTye="Imediate";
                }
                if (cardType.equals( "Cash On Delivery")) {
                    if (etName.getText().toString().isEmpty()) {
                        Toast.makeText(CreateOrder.this, "Enter your name..!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (etPhoneNo.getText().toString().isEmpty()) {
                        Toast.makeText(CreateOrder.this, "Enter your No..!", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (etAddress.getText().toString().isEmpty()) {
                        Toast.makeText(CreateOrder.this, "Enter your address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                else {

                    if (editCardNo.getText().toString().isEmpty()) {
                        Toast.makeText(CreateOrder.this, "Enter your Credit Card No", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (edtCardType.getText().toString().isEmpty()) {
                        Toast.makeText(CreateOrder.this, "Enter your Payment Type..!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editCVV.getText().toString().isEmpty()) {
                        Toast.makeText(CreateOrder.this, "Enter your CVV Code", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editCountry.getText().toString().isEmpty()) {
                        Toast.makeText(CreateOrder.this, "Enter your country Name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                confirmOrder(getIntent().getStringArrayListExtra("list"), getIntent().getIntExtra("totalAmount", 0), etAddress.getText().toString());

            }
        });
    }

    private void confirmOrder(ArrayList<String> cartList, int totalAmount, String showLocation) {
        FirebaseUser firebaseAuth= FirebaseAuth.getInstance().getCurrentUser();
        OrderModel orderModel=new OrderModel(etName.getText().toString(),etPhoneNo.getText().toString(),firebaseAuth.getUid(),totalAmount+"",showLocation,"0",editCardNo.getText().toString(),edtCardType.getText().toString(),editCVV.getText().toString(),editCountry.getText().toString(),cardType,deliveryTye,cartList);
     //   OrderModel orderModel=new OrderModel(etName.getText().toString(),etPhoneNo.getText().toString(),firebaseAuth.getUid(),totalAmount+"",address,"0",cartList);
        FirebaseDatabase.getInstance().getReference("Order").push().setValue(orderModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Data Not saved");
                } else {
                    startActivity(new Intent(CreateOrder.this,AddCart.class));
                    updateData();
                    finish();
                    Toast.makeText(CreateOrder.this, "Order Created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void updateData() {
        Query editQuery = FirebaseDatabase.getInstance().getReference().child("Cart").orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot edtData: dataSnapshot.getChildren()){
                    edtData.getRef().child("cartStatus").setValue(1);
                }
                Toast.makeText(CreateOrder.this,"Data Edited",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreateOrder.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    String getAddress(double latitude, double longitude) throws IOException {
        if (Geocoder.isPresent()) {
            Geocoder myLocation = new Geocoder(CreateOrder.this, Locale.getDefault());
            List<Address> myList = myLocation.getFromLocation(latitude, longitude, 1);
            Address address = (Address) myList.get(0);
            String addressStr = "";
            addressStr += address.getAddressLine(0) + ", ";
            addressStr += address.getAddressLine(1) + ", ";
            addressStr += address.getAddressLine(2);
            return address.getAddressLine(0);
        } else {
            return "geoCoder not present";
        }
    }
}