package com.unifiedtnc.belview_foods;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShowProductAdapter adapter;
    ProgressBar progressBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("category"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchProducts();


    }

    private void fetchProducts() {

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
        dbRef.child("Product").orderByChild("category").equalTo(getIntent().getStringExtra("category")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelArrayList.clear();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    productModelArrayList.add(
                            new ProductModel(
                                    dataSnapshot.getKey(),
                                    dataSnapshot.child("name").getValue(String.class),
                                    dataSnapshot.child("price").getValue(String.class),
                                    dataSnapshot.child("description").getValue(String.class),
                                    dataSnapshot.child("category").getValue(String.class),
                                    dataSnapshot.child("imageUrl").getValue(String.class)
                            )
                    );
                }

                LinearLayoutManager manager = new GridLayoutManager(ProductListActivity.this, 3);
                recyclerView.setLayoutManager(manager);
                adapter = new ShowProductAdapter(ProductListActivity.this, productModelArrayList);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}