package com.example.ayomide.chowadminapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ayomide.chowadminapp.R;

public class ShipperViewHolder extends RecyclerView.ViewHolder {

    public TextView shipper_name, shipper_phone;
    public Button btn_edit, btn_remove;

    public ShipperViewHolder(@NonNull View itemView) {
        super( itemView );

        shipper_name = itemView.findViewById(R.id.shipper_name);
        shipper_phone = itemView.findViewById(R.id.shipper_phone);
        btn_edit = itemView.findViewById( R.id.btnEdit );
        btn_remove = itemView.findViewById( R.id.btnRemove );

    }
}
