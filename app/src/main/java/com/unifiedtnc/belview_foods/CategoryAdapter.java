package com.unifiedtnc.belview_foods;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.io.Serializable;
import java.util.List;

class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> list;

    public CategoryAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_layout1,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CategoryModel categoryModel=list.get(position);

        holder.categoryName.setText(categoryModel.category);
        Glide.with(context).load(categoryModel.category_Image).into(holder.imageView);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete Category");
                builder.setMessage("Are you sure...!");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       FirebaseDatabase.getInstance().getReference().child("category").child(categoryModel.cat_key).removeValue(new DatabaseReference.CompletionListener() {
                           @Override
                           public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                               Toast.makeText(context, "Category Deleted...!", Toast.LENGTH_SHORT).show();
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

                  CategoryModel categoryModel1=list.get(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable("categoryData", (Serializable) categoryModel1);
                Intent intent=new Intent(context, CategoryUpdateActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView categoryName;
       //  Button btnDelete,imageViewbtnupdate;
         TextView  btnDelete,updatebtn;
         ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName=itemView.findViewById(R.id.txtcagory);
            btnDelete=itemView.findViewById(R.id.button2);
            imageView=itemView.findViewById(R.id.categoryImage);
            updatebtn=itemView.findViewById(R.id.button);

        }
    }
}
