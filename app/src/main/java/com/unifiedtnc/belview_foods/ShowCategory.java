package com.unifiedtnc.belview_foods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowCategory extends AppCompatActivity {
    RecyclerView recyclerView;
    CategoryAdapter adapter;
    ArrayList<CategoryModel> list_category;
    LinearLayoutManager manager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView=findViewById(R.id.categoryRecyid);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        manager=new GridLayoutManager(ShowCategory.this,3);
        list_category=new ArrayList<>();
        recyclerView.setLayoutManager(manager);
        fetchCategory();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowCategory.this,AddCategory.class));
            }
        });
    }


    private void fetchCategory() {

//        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();
        dbRef.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_category.clear();
//                progressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    String str_catgery = dataSnapshot.child("category").getValue(String.class);
                    String imageView=dataSnapshot.child("category_Image").getValue(String.class);


                   CategoryModel categoryModel=new CategoryModel(dataSnapshot.getKey(),str_catgery,imageView);
                    list_category.add(categoryModel);
                }
                adapter =new CategoryAdapter(ShowCategory.this,list_category);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowCategory.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}