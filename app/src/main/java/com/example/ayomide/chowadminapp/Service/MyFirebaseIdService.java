package com.example.ayomide.chowadminapp.Service;

import com.example.ayomide.chowadminapp.Common.Common;
import com.example.ayomide.chowadminapp.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateToServer(refreshedToken);
    }

    private void updateToServer(String refreshedToken) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token( refreshedToken, true ); //is true because it's from the server app
        tokens.child(Common.currentUser.getPhone()).setValue( data );
    }
}
