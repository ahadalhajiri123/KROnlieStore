package com.unifiedtnc.belview_foods;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView productName, category, price, totalAmount, detail, date, txtqty;
    int quantity = 1;
    int totalAmounts = 0;
    Toolbar toolbar;
    ProductModel productModel;
    int totalAmountItem=0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView = findViewById(R.id.imgIndicator);
        productName = findViewById(R.id.txtname);
        toolbar = findViewById(R.id.toolbar);
        price = findViewById(R.id.txtprice);
        date = findViewById(R.id.txtDate);
        category = findViewById(R.id.txtCategory);
        totalAmount = findViewById(R.id.totalamount);
        detail = findViewById(R.id.txtDescription);
        txtqty = findViewById(R.id.txtqty);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Bundle bundle = getIntent().getExtras();
        productModel = (ProductModel) bundle.getSerializable("MYDATA");

        //  ProductModel productModel =  (ProductModel) getIntent().getExtras().getSerializable("PRODUCT1");

        // imageView.setImageBitmap(productModel.imageUrl);

        Glide.with(this).load(productModel.imageUrl).into(imageView);
        productName.setText(productModel.name);
        category.setText(productModel.category);
        date.setText(productModel.current_date);
        String priceData=new StringConvertingDouble().covertValue(productModel.price);

        price.setText(priceData);
        detail.setText(productModel.description);
        txtqty.setText("1");
        totalAmounts = Integer.parseInt(productModel.price);
        totalAmountItem=totalAmounts;
        String priceValue=new StringConvertingDouble().covertValue(productModel.price);
        totalAmount.setText(priceValue);


    }

    private void addToCart(CartModel cartModel) {
        FirebaseDatabase.getInstance().getReference("Cart").push().setValue(cartModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Data Not saved");

                } else {
                    startActivity(new Intent(ProductDetailActivity.this, AddCart.class));
                    finish();
                    Toast.makeText(ProductDetailActivity.this, "Product add to cart successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addQuantity(View view) {
        quantity = quantity + 1;
        txtqty.setText(quantity + "");
        totalAmountItem=(totalAmounts * quantity);
        String priceValue=new StringConvertingDouble().covertValue(totalAmountItem+"");


        totalAmount.setText(priceValue);
    }

    public void minsQuantity(View view) {
        if (quantity <= 1) {

        } else {
            quantity = quantity - 1;

            txtqty.setText(quantity + "");
            totalAmountItem = totalAmounts * quantity;

            String priceValue=new StringConvertingDouble().covertValue(totalAmountItem+"");
            totalAmount.setText(priceValue);


        }


    }

    public void AddCart(View view) {


        CartModel cartModel = new CartModel(productModel.name, productModel.imageUrl, productModel.product_key, FirebaseAuth.getInstance().getCurrentUser().getUid(), quantity,totalAmountItem,0);
        addToCart(cartModel);


    }
}