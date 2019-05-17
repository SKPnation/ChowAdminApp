package com.example.ayomide.chowadminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ayomide.chowadminapp.Common.Common;
import com.example.ayomide.chowadminapp.Home;
import com.example.ayomide.chowadminapp.Model.Category;
import com.example.ayomide.chowadminapp.Model.User;
import com.example.ayomide.chowadminapp.R;
import com.example.ayomide.chowadminapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    private MaterialEditText etPhone, etPassword;
    private Button btnSignIn;

    private ProgressDialog mDialog;

    DatabaseReference table_user;
    private FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_in );

        etPhone = findViewById( R.id.etPhone );
        etPassword = findViewById( R.id.etPassword );
        btnSignIn = findViewById( R.id.btnSignIn );

        table_user = FirebaseDatabase.getInstance().getReference("User");

        btnSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(etPhone.getText().toString(), etPassword.getText().toString());
            }
        } );
    }

    private void signInUser(final String phone, final String password)
    {
        mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Calm down...");
        mDialog.show();

        table_user.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(phone).exists())
                {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if(Boolean.parseBoolean(user.getIsStaff())) //if staff is true
                    {
                        if (user.getPassword().equals(etPassword.getText().toString()))
                        {
                            Intent homeIntent = new Intent(SignIn.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                            Toast.makeText( SignIn.this, "Login successful", Toast.LENGTH_LONG ).show();
                        }
                        else {
                            Toast.makeText( SignIn.this, "Wrong password", Toast.LENGTH_LONG ).show();
                        }
                    }
                    else
                    {
                        Toast.makeText( SignIn.this, "Please login with staff account", Toast.LENGTH_LONG ).show();
                    }
                }
                else
                {
                    mDialog.dismiss();
                    Toast.makeText( SignIn.this, "User does not exist", Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
