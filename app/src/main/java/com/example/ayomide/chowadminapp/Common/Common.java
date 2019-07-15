package com.example.ayomide.chowadminapp.Common;

import com.example.ayomide.chowadminapp.Model.Request;
import com.example.ayomide.chowadminapp.Model.User;
import com.example.ayomide.chowadminapp.Remote.APIService;
import com.example.ayomide.chowadminapp.Remote.FCMRetrofitClient;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Common {

    public static User currentUser;
    public static Request currentRequest;

    public static final int IMAGE_REQUEST = 71;

    public static final String SHIPPERS_TABLE = "Shippers";

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final String fcmUrl = "https://fcm.googleapis.com/";

    public static APIService getFCMClient()
    {
        return FCMRetrofitClient.getFCMClient( fcmUrl ).create( APIService.class );
    }

    public static String getDate(long time)
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis( time );
        StringBuilder date = new StringBuilder(android.text.format.DateFormat.format("dd-MM-yyyy HH:mm",
                calendar)
                .toString());
        return date.toString();
    }

    public static String convertCodeToStatus(String code)
    {
        if (code.equals( "0" ))
            return "Placed";
        else if (code.equals( "1" ))
            return "On my way";
        else
            return "Delivered";
    }
}
