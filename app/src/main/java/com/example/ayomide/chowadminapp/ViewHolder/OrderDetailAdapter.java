package com.example.ayomide.chowadminapp.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayomide.chowadminapp.Model.Order;
import com.example.ayomide.chowadminapp.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{

    List<Order> myOrders;


    public OrderDetailAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.order_details_layout,viewGroup,false);
        return new ViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Order order =  myOrders.get( i );
        viewHolder.name.setText( String.format( "Name : %s", order.getProductName() ) );
        viewHolder.qty.setText( String.format( "Quantity : %s", order.getQuantity() ) );
        viewHolder.price.setText( String.format( "Price : %s", order.getPrice() ) );
        viewHolder.discount.setText( String.format( "Discount : %s", order.getDiscount() ) );
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView name, qty, price, discount;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            name = itemView.findViewById( R.id.product_name );
            qty = itemView.findViewById( R.id.product_qty );
            price = itemView.findViewById( R.id.product_price );
            discount = itemView.findViewById( R.id.product_discount );
        }
    }
}
