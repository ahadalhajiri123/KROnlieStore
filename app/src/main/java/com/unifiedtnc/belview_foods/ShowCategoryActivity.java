package com.unifiedtnc.belview_foods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ShowCategoryAdapter adapter;
    ArrayList<CategoryModel> list_category;
    LinearLayoutManager manager;
    ProgressBar progressBar;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        recyclerView=findViewById(R.id.categoryRecy);
        progressBar=findViewById(R.id.recYBar1);
        manager=new GridLayoutManager(this,3);
        list_category=new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);

        recyclerView.setLayoutManager(manager);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchCategory();


    }
    public void OnClickBtn(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.lythome: {
                startActivity(new Intent(ShowCategoryActivity.this, Dashboard.class)
                        .putExtra("status", "user"));
                break;
            }
            case R.id.lytcategory: {
                startActivity(new Intent(ShowCategoryActivity.this, ShowCategoryActivity.class)
                        .putExtra("status", "user"));

                break;
            }
            case R.id.lytcart: {

                startActivity(new Intent(ShowCategoryActivity.this, AddCart.class));
                break;
            }
            default:
                Toast.makeText(this, "no ", Toast.LENGTH_SHORT).show();


        }

    }

    private void fetchCategory() {

         progressBar.setVisibility(View.VISIBLE);

        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();
        dbRef.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_category.clear();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String str_catgery = dataSnapshot.child("category").getValue(String.class);
                    String category_img = dataSnapshot.child("category_Image").getValue(String.class);
                    CategoryModel categoryModel=new CategoryModel(dataSnapshot.getKey(),str_catgery,category_img);
                    list_category.add(categoryModel);
                }
                adapter =new ShowCategoryAdapter(ShowCategoryActivity.this,list_category);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}