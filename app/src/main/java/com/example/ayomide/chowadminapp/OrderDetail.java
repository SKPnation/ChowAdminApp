package com.example.ayomide.chowadminapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.ayomide.chowadminapp.Common.Common;
import com.example.ayomide.chowadminapp.Model.Order;
import com.example.ayomide.chowadminapp.Model.Request;
import com.example.ayomide.chowadminapp.ViewHolder.OrderDetailAdapter;
import com.example.ayomide.chowadminapp.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderDetail extends AppCompatActivity {

    TextView orderID, orderPhone, orderPriceTotal, orderAddress, orderComment;
    String orderIdValue = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_detail );

        requests = FirebaseDatabase.getInstance().getReference("Requests");

        orderID = findViewById( R.id.order_id );
        orderPhone = findViewById( R.id.order_phone );
        orderPriceTotal = findViewById( R.id.order_total_price );
        orderAddress = findViewById( R.id.order_address );
        orderComment = findViewById( R.id.order_comment );

        recyclerView = (RecyclerView) findViewById(R.id.list_foods);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null)
            orderIdValue = getIntent().getStringExtra( "OrderId" );

        //set values
        orderID.setText( orderIdValue );
        orderPhone.setText( Common.currentRequest.getPhone() );
        orderPriceTotal.setText( Common.currentRequest.getTotal() );
        orderAddress.setText( Common.currentRequest.getAddress() );
        orderComment.setText( Common.currentRequest.getComment() );

        OrderDetailAdapter adapter = new OrderDetailAdapter( Common.currentRequest.getFoods() );
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter( adapter );
    }
}
