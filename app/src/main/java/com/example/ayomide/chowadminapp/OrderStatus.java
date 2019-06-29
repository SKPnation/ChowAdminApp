package com.example.ayomide.chowadminapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ayomide.chowadminapp.Common.Common;
import com.example.ayomide.chowadminapp.Interface.ItemClickListener;
import com.example.ayomide.chowadminapp.Model.MyResponse;
import com.example.ayomide.chowadminapp.Model.Notification;
import com.example.ayomide.chowadminapp.Model.Request;
import com.example.ayomide.chowadminapp.Model.Sender;
import com.example.ayomide.chowadminapp.Model.Token;
import com.example.ayomide.chowadminapp.Remote.APIService;
import com.example.ayomide.chowadminapp.Remote.FCMRetrofitClient;
import com.example.ayomide.chowadminapp.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    DatabaseReference requests;

    MaterialSpinner spinner;

    FirebaseDatabase db;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_status );

        mService = Common.getFCMClient();

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(); //load all orders
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, final int position) {
                viewHolder.tvOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.tvOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.tvOrderPhone.setText(model.getPhone());
                viewHolder.tvOrderAddress.setText(model.getAddress());

                viewHolder.btnEdit.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });

                viewHolder.btnRemove.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });

                viewHolder.btnDetails.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent orderDetail = new Intent( OrderStatus.this, OrderDetail.class );
                        Common.currentRequest = model;
                        orderDetail.putExtra( "OrderId", adapter.getRef( position ).getKey() );
                        startActivity( orderDetail );
                    }
                } );
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    private void deleteOrder(String key)
    {
        requests.child(key).removeValue();
        Toast.makeText(OrderStatus.this, "Order deleted!!!", Toast.LENGTH_LONG ).show();
        adapter.notifyDataSetChanged();
    }

    private void showUpdateDialog(final String key, final Request item)
    {
        //just copy code from showDialog and modify
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Choose new status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = (MaterialSpinner) view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed", "On my way", "Delivered");

        alertDialog.setView(view);

        final String localKey = key;
        //set button
        alertDialog.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                requests.child(localKey).setValue(item);
                adapter.notifyDataSetChanged(); //add to update item size

                sendOrderStatusToUser(key, item);
            }
        });

        alertDialog.setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void sendOrderStatusToUser(final String key, Request item) {
        DatabaseReference tokens = db.getReference("Tokens");
        tokens.orderByKey().equalTo( item.getPhone() )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            Token token = postSnapshot.getValue(Token.class);

                            //Make raw payload
                            Notification notification = new Notification( "CHOW", "Your order "+key+" was updated" );
                            Sender content = new Sender(token.getToken(),notification);

                            mService.sendNotification( content )
                                    .enqueue( new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                            if (response.code() == 200) {
                                                if (response.body().success == 1) {
                                                    Toast.makeText( OrderStatus.this, "Order Updated!!!", Toast.LENGTH_SHORT ).show();
                                                } else {
                                                    Toast.makeText( OrderStatus.this, "Order was updated but failed to send notification", Toast.LENGTH_SHORT ).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e( "ERROR", t.getMessage() );
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
