package com.unifiedtnc.belview_foods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

class ShowProductAdapter extends RecyclerView.Adapter<ShowProductAdapter.ViewHolder> {

    Context context;
    List<ProductModel> list;

    public ShowProductAdapter(Context context, List<ProductModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.lyt_category_main,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ProductModel productModel=list.get(position);

        holder.productName.setText(productModel.name);
        String priceValue=new StringConvertingDouble().covertValue(productModel.price);
        holder.productPrice.setText("£ "+priceValue);
        Glide.with(context)
                .load(productModel.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_add_shopping_cart)
                .into(holder.imageView);
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductModel productModel1=list.get(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable("MYDATA", (Serializable) productModel1);
                Intent intent=new Intent(context,ProductDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });


    }

    private void addToCart(String name, String imageUrl, String product_key, String uid, int i, int i1) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView productName;
         TextView productPrice;
         ConstraintLayout layoutMain;
         ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName=itemView.findViewById(R.id.tv_product);
            layoutMain=itemView.findViewById(R.id.lytMain);
            imageView=itemView.findViewById(R.id.imgcategory);
            productPrice=itemView.findViewById(R.id.txtPrice);

        }
    }

}
