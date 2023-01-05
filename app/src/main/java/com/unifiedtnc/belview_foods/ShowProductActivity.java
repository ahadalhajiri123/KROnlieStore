package com.unifiedtnc.belview_foods;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter   productAdapter;
    LinearLayoutManager manager;
    ArrayList<ProductModel> product_list;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView=findViewById(R.id.prodRecyId);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        manager=new LinearLayoutManager(this);
        product_list=new ArrayList<>();
        recyclerView.setLayoutManager(manager);

       fetchProductdata();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowProductActivity.this,AddProductActivity.class));
            }
        });
    }

    private void fetchProductdata() {

        DatabaseReference deRef= FirebaseDatabase.getInstance().getReference();
        deRef.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          product_list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    String name=dataSnapshot1.child("name").getValue(String.class);
                    String price=dataSnapshot1.child("price").getValue(String.class);
                    String description=dataSnapshot1.child("description").getValue(String.class);
                    String category=dataSnapshot1.child("category").getValue(String.class);
                    String imageView=dataSnapshot1.child("imageUrl").getValue(String.class);

                    ProductModel productModel=new ProductModel(dataSnapshot1.getKey(),name,price,category,description,imageView);
                    product_list.add(productModel);

                }

                productAdapter=new ProductAdapter(ShowProductActivity.this,product_list);
                recyclerView.setAdapter(productAdapter);
             productAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowProductActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }
        });


    }
}