package com.unifiedtnc.belview_foods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

class CartDetailAdaptor extends RecyclerView.Adapter<CartDetailAdaptor.ViewHolder> {

    Context context;
    List<CartModel> list;
    String status;

    public CartDetailAdaptor(Context context, List<CartModel> list, String status) {
        this.context = context;
        this.list = list;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lyt_cartlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CartModel cartModel = list.get(position);
        holder.tv_name.setText(cartModel.productName);
        String priceValue=new StringConvertingDouble().covertValue(cartModel.price+"");
        holder.tv_price.setText(priceValue+"");
        holder.tv_quantity.setText(cartModel.quantity+"");
//       holder.imgproduct.setImageUrl(cartModel.productImage,null);
        Glide.with(context).load(cartModel.productImage).into(holder.imgproduct);
        if (status.equals("user")) {
            holder.imgdelete.setVisibility(View.VISIBLE);
        } else {

            holder.imgdelete.setVisibility(View.GONE);
        }
//       holder.tv.setText(cartModel.quantity);
        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("Cart").child(cartModel.cartId).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(context, "product Deleted...!", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_price, tv_quantity;
        ImageView imgproduct, imgdelete;
        ImageButton btnaddqty, btnminusqty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.imgproduct);
            tv_name = itemView.findViewById(R.id.txtproductname);
            tv_price = itemView.findViewById(R.id.txtprice);
            tv_quantity = itemView.findViewById(R.id.txtQuantity);
            btnaddqty = itemView.findViewById(R.id.btnaddqty);
            btnminusqty = itemView.findViewById(R.id.btnminusqty);
            imgdelete = itemView.findViewById(R.id.imgdelete);
        }
    }
}
