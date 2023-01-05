package com.unifiedtnc.belview_foods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

class OrderDetailAdaptor extends RecyclerView.Adapter<OrderDetailAdaptor.ViewHolder> {

         Context context;
         List<OrderModel> list;
         String status;
//    double latitude;
//    double longitude;

    public OrderDetailAdaptor(Context context, List<OrderModel> list, String status) {
        this.context = context;
        this.list = list;
        this.status=status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_detail_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final OrderModel orderModel=list.get(position);
       holder.tv_name.setText(orderModel.userName);
       holder.tv_price.setText(orderModel.totalPrice);
       holder.tv_phone.setText(orderModel.PhoneNo);
       holder.tv_address.setText(orderModel.address);
     //  holder.tv_address.setMovementMethod(LinkMovementMethod.getInstance());

       holder.tv_price.setText("£ "+orderModel.totalPrice);
       holder.creditCardNo.setText(orderModel.cardNo);
       holder.creditType.setText(orderModel.cardType);
       holder.cVVCode.setText(orderModel.cardCVV);
       holder.country.setText(orderModel.country);
       holder.payedby.setText(orderModel.radioCardType);
       holder.delivryTpe.setText(orderModel.deliveryTye);

      holder.tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            String uri = String.format(Locale.ENGLISH,"http://maps.google.co.in/maps?q=" + orderModel.address);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            context.startActivity(intent);

    }
});

                if (orderModel.radioCardType.equals("Cash On Delivery")){

                    holder.l1.setVisibility(View.GONE);
                    holder.l2.setVisibility(View.GONE);
                    holder.l3.setVisibility(View.GONE);
                    holder.l4.setVisibility(View.GONE);
              //      Log.d("not show",orderModel.radioCardType);
                }else
                    {
                    holder.l1.setVisibility(View.VISIBLE);
                    holder.l2.setVisibility(View.VISIBLE);
                    holder.l3.setVisibility(View.VISIBLE);
                    holder.l3.setVisibility(View.VISIBLE);
                    //    Toast.makeText(context, "Not show", Toast.LENGTH_SHORT).show();

//Log.d("show",orderModel.radioCardType);
                    }



       if(status.equals("admin")){
           if(orderModel.orderStatus.equals("0")){
               holder.tv_status.setText("Pending ");
               holder.orderCancel.setVisibility(View.VISIBLE);
               holder.orderConf.setVisibility(View.VISIBLE);
               holder.orderDeliver.setVisibility(View.VISIBLE);
           }else if(orderModel.orderStatus.equals("1")){
               holder.orderCancel.setVisibility(View.GONE);
               holder.orderConf.setVisibility(View.GONE);
               holder.orderDeliver.setVisibility(View.VISIBLE);
               holder.tv_status.setText(" Accepted ");

           } else if(orderModel.orderStatus.equals("2")){
               holder.orderCancel.setVisibility(View.GONE);
               holder.orderConf.setVisibility(View.GONE);
               holder.orderDeliver.setVisibility(View.GONE);
               holder.tv_status.setText(" Delivered");

           }else{
               holder.orderCancel.setVisibility(View.GONE);
               holder.orderConf.setVisibility(View.GONE);
               holder.orderDeliver.setVisibility(View.GONE);
               holder.tv_status.setText(" Canceled");
           }

       }else{
           holder.orderConf.setVisibility(View.GONE);
           holder.orderDeliver.setVisibility(View.GONE);
           holder.orderCancel.setVisibility(View.GONE);
           if(orderModel.orderStatus.equals("0")){
               holder.tv_status.setText("Pending ");

           }else if(orderModel.orderStatus.equals("1")){

               holder.tv_status.setText(" Accepted ");

           } else if(orderModel.orderStatus.equals("2")){

               holder.tv_status.setText(" Delivered");

           }else{
               holder.tv_status.setText(" Canceled");
           }
       }

       holder.btnOrderDetail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                context.startActivity(new Intent(context,OrderDetailList.class).putExtra("list",orderModel.arrayList)
                .putExtra("status",status));

           }
       });

       holder.orderConf.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(orderModel.orderStatus.equals("0")) {
                   OrderModel orderModel1 = new OrderModel(
                           orderModel.userName,
                           orderModel.PhoneNo,
                           orderModel.userId,
                           orderModel.totalPrice,
                           orderModel.address,
                           "1",
                           orderModel.cardNo,
                           orderModel.cardType,
                           orderModel.cardCVV,
                           orderModel.country,
                           orderModel.radioCardType,
                           orderModel.deliveryTye,
                           orderModel.arrayList
                   );
                   updateOrder(orderModel1, orderModel.orderId);
               }else{
                   Toast.makeText(context, "you have already confirm order", Toast.LENGTH_SHORT).show();
               }
           }
       });
        holder.orderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderModel.orderStatus.equals("0")) {
                    OrderModel orderModel1 = new OrderModel(
                            orderModel.userName,
                            orderModel.PhoneNo,
                            orderModel.userId,
                            orderModel.totalPrice,
                            orderModel.address,
                            "3",
                            orderModel.cardNo,
                            orderModel.cardType,
                            orderModel.cardCVV,
                            orderModel.country,
                            orderModel.radioCardType,
                            orderModel.deliveryTye,
                            orderModel.arrayList
                    );
                    updateOrder(orderModel1, orderModel.orderId);
                }else{
//                    Toast.makeText(context, "you have already confirm order", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.orderDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderModel.orderStatus.equals("1")) {
                    OrderModel orderModel1 = new OrderModel(
                            orderModel.userName,
                            orderModel.PhoneNo,
                            orderModel.userId,
                            orderModel.totalPrice,
                            orderModel.address,
                            "2",
                            orderModel.cardNo,
                            orderModel.cardType,
                            orderModel.cardCVV,
                            orderModel.country,
                            orderModel.radioCardType,
                            orderModel.deliveryTye,
                            orderModel.arrayList
                    );
                    updateOrder(orderModel1, orderModel.orderId);
                }else{
                    Toast.makeText(context, "you have already delivered order", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_price,tv_address,tv_phone,tv_status,creditCardNo,creditType,cVVCode,country,payedby,delivryTpe;
        TextView btnOrderDetail,orderConf,orderCancel,orderDeliver;

        LinearLayout l1,l2,l3,l4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_c_name);
            tv_address=itemView.findViewById(R.id.tv_address);
            tv_price=itemView.findViewById(R.id.tv_total_amount);
            tv_phone=itemView.findViewById(R.id.tv_phone);
            tv_status=itemView.findViewById(R.id.tv_status);
            btnOrderDetail=itemView.findViewById(R.id.btnOrderDetail);
            creditCardNo=itemView.findViewById(R.id.creditCardNo1);
            creditType=itemView.findViewById(R.id.crditTyp1);
            cVVCode=itemView.findViewById(R.id.cVVCode1);
            country=itemView.findViewById(R.id.country1);
            payedby=itemView.findViewById(R.id.payemntBy1);
            orderConf=itemView.findViewById(R.id.orderAccpet);
            orderCancel=itemView.findViewById(R.id.orderCancel);
            orderDeliver=itemView.findViewById(R.id.orderDeliver);
            delivryTpe=itemView.findViewById(R.id.delivryTpe);
            l1=itemView.findViewById(R.id.l1);
            l2=itemView.findViewById(R.id.l2);
            l3=itemView.findViewById(R.id.l3);
            l4=itemView.findViewById(R.id.l4);





        }
    }

    private void updateOrder(OrderModel orderModel,String orderId) {
        FirebaseDatabase.getInstance().getReference("Order").child(orderId).setValue(orderModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Data Not saved");

                } else {
//                    notifyDataSetChanged();
//                    Toast.makeText(context, "Order updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
