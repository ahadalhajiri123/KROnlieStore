package com.unifiedtnc.belview_foods;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddCart extends AppCompatActivity {

    RecyclerView recyclerView;
    CartDetailAdaptor adapter;
    TextView tv_totalPrice;
    int totalPrice = 0;
    RelativeLayout btnCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar=findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(R.drawable.white_back);
            toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
        recyclerView = findViewById(R.id.cartrecycleview);
        tv_totalPrice = findViewById(R.id.txtsubtotal);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        fetchCartData();
    }

    private void fetchCartData() {

//        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
        final ArrayList<String> list = new ArrayList<>();
        dbRef.child("Cart").orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartModelArrayList.clear();
                totalPrice = 0;
                String cartKey = "";

//                progressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if( dataSnapshot.child("cartStatus").getValue(Integer.class)==0) {
                        list.add(dataSnapshot.getKey());
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

                        totalPrice += dataSnapshot.child("price").getValue(Integer.class);
                    }
                }
                String priceData=new StringConvertingDouble().covertValue(totalPrice+"");

                tv_totalPrice.setText("£ "+priceData);
                setOrder(totalPrice, list);
                LinearLayoutManager manager = new LinearLayoutManager(AddCart.this);
                recyclerView.setLayoutManager(manager);
                adapter = new CartDetailAdaptor(AddCart.this, cartModelArrayList,"user");
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddCart.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setOrder(final int totalPrice, final ArrayList<String> list) {

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                 if (!(totalPrice ==0)) {

                     startActivity(new Intent(AddCart.this, CreateOrder.class)
                             .putExtra("totalAmount", totalPrice)
                             .putExtra("list", list)
                     );
                 }else
                 {
                     Toast.makeText(AddCart.this, "Empty Cart...!", Toast.LENGTH_SHORT).show();
                 }
            }
        });

    }

}