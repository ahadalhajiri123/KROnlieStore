package com.unifiedtnc.belview_foods;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHlder> {
    Context context;
    List<ProductModel> list;

    public ProductAdapter(Context context, List<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_product_layout, parent, false);
        return new ViewHlder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHlder holder, int position) {

        final ProductModel productModel = list.get(position);
        holder.name.setText(productModel.name);
        holder.price.setText("£ "+productModel.price);
        holder.category.setText(productModel.category);
       // holder.date.setText(productModel.current_date);
        holder.description.setText(productModel.description);

        //     Glide.with(context).load(productModel.imageUrl).into(holder.imageView);
        Glide.with(context).load(productModel.imageUrl).into(holder.imageView);


/////////////////////////////////// delete product   /////////////////////////////////////
        holder.deletbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Product");
                builder.setMessage("Are you sure...!");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference().child("Product").child(productModel.product_key).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(context, "Product Deleted...!", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });

        holder.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("PRODUCT", productModel);
                context.startActivity(intent);
                // putExtra("status","admin")

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHlder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView deletbtn, updatebtn;
        TextView name, price, date, description, category;

        public ViewHlder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imagProoductId);
            name = itemView.findViewById(R.id.nameProductId);
            price = itemView.findViewById(R.id.priceProductId);

            description = itemView.findViewById(R.id.descriptionProductId);
            category = itemView.findViewById(R.id.categoryProductId);
            deletbtn = itemView.findViewById(R.id.deletbtn);
            updatebtn = itemView.findViewById(R.id.updatebtn);


        }
    }
}
