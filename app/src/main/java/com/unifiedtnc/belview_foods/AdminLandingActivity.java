package com.unifiedtnc.belview_foods;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminLandingActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void btnAddCategory(View view) {

startActivity(new Intent(AdminLandingActivity.this,ShowCategory.class));
    }

    public void btnAddProduct(View view) {
       startActivity(new Intent(AdminLandingActivity.this,ShowProductActivity.class));
    }

    public void btnOrders(View view) {
        startActivity(new Intent(AdminLandingActivity.this,OrderDetailActivity.class).putExtra("status","admin"));
    }
}