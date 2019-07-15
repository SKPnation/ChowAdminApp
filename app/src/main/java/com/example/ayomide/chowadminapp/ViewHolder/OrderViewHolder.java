package com.example.ayomide.chowadminapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ayomide.chowadminapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView tvOrderId, tvOrderDate, tvOrderStatus, tvOrderPhone, tvOrderAddress;
    public Button btnEdit, btnRemove, btnDetails, btnDirection;

    public OrderViewHolder(@NonNull View itemView) {
        super( itemView );
        tvOrderId = itemView.findViewById(R.id.order_id);
        tvOrderDate = itemView.findViewById( R.id.order_date );
        tvOrderStatus = itemView.findViewById(R.id.order_status);
        tvOrderPhone = itemView.findViewById(R.id.order_phone);
        tvOrderAddress = itemView.findViewById(R.id.order_address);

        btnEdit = itemView.findViewById( R.id.btnEdit );
        btnRemove = itemView.findViewById( R.id.btnRemove );
        btnDetails = itemView.findViewById( R.id.btnDetails );
        btnDirection = itemView.findViewById( R.id.btnDirection );
    }

}
