package com.unifiedtnc.belview_foods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {
 RecyclerView recyclerView;
 OrderDetailAdaptor orderDetailAdaptor;
 Toolbar toolbar;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView=findViewById(R.id.recYIDforOrders);
        status=getIntent().getStringExtra("status");
        toolbar = findViewById(R.id.toolbar2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        if(status.equals("admin")){
            fetchOrder();
        }else {
            fetchUserOrder();
        }


    }
    private void fetchOrder() {

//        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final ArrayList<OrderModel> cartModelArrayList = new ArrayList<>();
        final ArrayList<String> list = new ArrayList<>();
        dbRef.child("Order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartModelArrayList.clear();

//                progressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(dataSnapshot.getKey());
                        ArrayList<String> arrayList= (ArrayList<String>) dataSnapshot.child("arrayList").getValue();
                        cartModelArrayList.add(
                                new OrderModel(
                                        dataSnapshot.getKey(),
                                        dataSnapshot.child("userName").getValue(String.class),
                                        dataSnapshot.child("PhoneNo").getValue(String.class),
                                        dataSnapshot.child("userId").getValue(String.class),
                                        dataSnapshot.child("totalPrice").getValue(String.class),
                                        dataSnapshot.child("address").getValue(String.class),
                                        dataSnapshot.child("orderStatus").getValue(String.class),
                                        dataSnapshot.child("cardNo").getValue(String.class),
                                        dataSnapshot.child("cardType").getValue(String.class),
                                        dataSnapshot.child("cardCVV").getValue(String.class),
                                        dataSnapshot.child("country").getValue(String.class),
                                        dataSnapshot.child("radioCardType").getValue(String.class),
                                        dataSnapshot.child("deliveryTye").getValue(String.class),
                                        arrayList
                                )
                        );

                    }

                LinearLayoutManager manager = new LinearLayoutManager(OrderDetailActivity.this);
                recyclerView.setLayoutManager(manager);
                orderDetailAdaptor = new OrderDetailAdaptor(OrderDetailActivity.this, cartModelArrayList,status);
                recyclerView.setAdapter(orderDetailAdaptor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void fetchUserOrder() {

//        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final ArrayList<OrderModel> cartModelArrayList = new ArrayList<>();
        final ArrayList<String> list = new ArrayList<>();
        dbRef.child("Order").orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartModelArrayList.clear();

//                progressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.getKey());
                    ArrayList<String> arrayList= (ArrayList<String>) dataSnapshot.child("arrayList").getValue();
                    cartModelArrayList.add(
                            new OrderModel(
                                    dataSnapshot.getKey(),
                                    dataSnapshot.child("userName").getValue(String.class),
                                    dataSnapshot.child("PhoneNo").getValue(String.class),
                                    dataSnapshot.child("userId").getValue(String.class),
                                    dataSnapshot.child("totalPrice").getValue(String.class),
                                    dataSnapshot.child("address").getValue(String.class),
                                    dataSnapshot.child("orderStatus").getValue(String.class),
                                    dataSnapshot.child("cardNo").getValue(String.class),
                                    dataSnapshot.child("cardType").getValue(String.class),
                                    dataSnapshot.child("cardCVV").getValue(String.class),
                                    dataSnapshot.child("country").getValue(String.class),
                                    dataSnapshot.child("radioCardType").getValue(String.class),
                                    dataSnapshot.child("deliveryTye").getValue(String.class),
                                    arrayList
                            )
                    );

                }

                LinearLayoutManager manager = new LinearLayoutManager(OrderDetailActivity.this);
                recyclerView.setLayoutManager(manager);
                orderDetailAdaptor = new OrderDetailAdaptor(OrderDetailActivity.this, cartModelArrayList, status);
                recyclerView.setAdapter(orderDetailAdaptor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}