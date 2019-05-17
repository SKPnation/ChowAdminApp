package com.example.ayomide.chowadminapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn;
    TextView tvSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        btnSignIn = findViewById(R.id.btnSignIn);
        tvSlogan = findViewById(R.id.tvSlogan);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/AlexBrush-Regular.ttf");
        tvSlogan.setTypeface(typeface);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(signIn);
            }
        });
    }
}
