package com.unifiedtnc.belview_foods;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailList extends AppCompatActivity {

    RecyclerView recyclerView;
    CartDetailAdaptor adapter;
    TextView tv_totalPrice;
    int totalPrice = 0;
    RelativeLayout btnCheckOut;
String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.recYIDforOrders);
        status=getIntent().getStringExtra("status");
        if(status.equals("admin")){
            fetchAdminCartData();
        }else{
            fetchCartData();

        }
    }

    private void fetchCartData() {

//        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
        dbRef.child("Cart").orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartModelArrayList.clear();
                totalPrice = 0;
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList = getIntent().getStringArrayListExtra("list");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("cartStatus").getValue(Integer.class) == 1) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).equals(dataSnapshot.getKey())) {
                                cartModelArrayList.add(
                                        new CartModel(
                                                dataSnapshot.getKey(),
                                                dataSnapshot.child("productName").getValue(String.class),
                                                dataSnapshot.child("productImage").getValue(String.class),
                                                dataSnapshot.child("productId").getValue(String.class),
                                                dataSnapshot.child("userId").getValue(String.class),
                                                dataSnapshot.child("quantity").getValue(Integer.class),
                                                dataSnapshot.child("price").getValue(Integer.class),
                                                dataSnapshot.child("cartStatus").getValue(Integer.class)
                                        )
                                );
                            }
                        }
                    }
                }
                LinearLayoutManager manager = new LinearLayoutManager(OrderDetailList.this);
                recyclerView.setLayoutManager(manager);
                adapter = new CartDetailAdaptor(OrderDetailList.this, cartModelArrayList, "admin");
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailList.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void fetchAdminCartData() {

//        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
        dbRef.child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartModelArrayList.clear();
                totalPrice = 0;
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList = getIntent().getStringArrayListExtra("list");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("cartStatus").getValue(Integer.class) == 1) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).equals(dataSnapshot.getKey())) {
                                cartModelArrayList.add(
                                        new CartModel(
                                                dataSnapshot.getKey(),
                                                dataSnapshot.child("productName").getValue(String.class),
                                                dataSnapshot.child("productImage").getValue(String.class),
                                                dataSnapshot.child("productId").getValue(String.class),
                                                dataSnapshot.child("userId").getValue(String.class),
                                                dataSnapshot.child("quantity").getValue(Integer.class),
                                                dataSnapshot.child("price").getValue(Integer.class),
                                                dataSnapshot.child("cartStatus").getValue(Integer.class)
                                        )
                                );
                            }
                        }
                    }
                }
                LinearLayoutManager manager = new LinearLayoutManager(OrderDetailList.this);
                recyclerView.setLayoutManager(manager);
                adapter = new CartDetailAdaptor(OrderDetailList.this, cartModelArrayList, "admin");
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailList.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


}