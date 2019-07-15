package com.example.ayomide.chowadminapp;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.ayomide.chowadminapp.Common.Common;
import com.example.ayomide.chowadminapp.Model.Shipper;
import com.example.ayomide.chowadminapp.ViewHolder.ShipperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ShipperManagement extends AppCompatActivity {

    FloatingActionButton fabAdd;

    MaterialEditText etName, etPhone, etPassword;

    FirebaseDatabase database;
    DatabaseReference shippers;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Shipper, ShipperViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shipper_management );

        database = FirebaseDatabase.getInstance();
        shippers = database.getReference(Common.SHIPPERS_TABLE );

        fabAdd = (FloatingActionButton) findViewById( R.id.fab );
        fabAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateShipperDialog();
            }
        } );

        recyclerView = findViewById( R.id.recycler_shippers );
        recyclerView.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager( layoutManager );

        loadShippers();
    }

    private void loadShippers()
    {
        adapter = new FirebaseRecyclerAdapter<Shipper, ShipperViewHolder>(
                Shipper.class,
                R.layout.shipper_layout,
                ShipperViewHolder.class,
                shippers) {
            @Override
            protected void populateViewHolder(ShipperViewHolder viewHolder, Shipper model, int position) {
                viewHolder.shipper_name.setText( model.getName() );
                viewHolder.shipper_phone.setText( model.getPhone() );
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void showCreateShipperDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( ShipperManagement.this );
        builder.setTitle( "Create Shipper" );
        builder.setIcon( R.drawable.ic_local_shipping_black_24dp );

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_shipper = inflater.inflate( R.layout.add_new_shipper_layout, null );

        etName = add_new_shipper.findViewById( R.id.etShipperName );
        etPhone = add_new_shipper.findViewById( R.id.etShipperPhone );
        etPassword = add_new_shipper.findViewById( R.id.etShipperPassword );

        builder.setView( add_new_shipper );

        builder.setPositiveButton( "CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (TextUtils.isEmpty( etName.getText().toString() )
                        || TextUtils.isEmpty( etPhone.getText().toString() )
                        || TextUtils.isEmpty( etPassword.getText().toString() ))
                {
                    Toast.makeText( ShipperManagement.this, "All fields are required", Toast.LENGTH_SHORT ).show();
                }
                else
                    {
                        Shipper shipper = new Shipper();
                        shipper.setName(etName.getText().toString());
                        shipper.setPhone(etPhone.getText().toString());
                        shipper.setPassword(etPassword.getText().toString());

                        shippers.child( etPhone.getText().toString() )
                                .setValue( shipper )
                                .addOnSuccessListener( new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText( ShipperManagement.this, "Shipper created successfully", Toast.LENGTH_SHORT ).show();
                                    }
                                } ).addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( ShipperManagement.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
            }
        } );
        builder.show();
    }
}


    /* */