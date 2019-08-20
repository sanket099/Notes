package com.example.notes;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "FBF2E2C4-B635-C992-FF8B-C729FE62BC00";
    public static final String API_KEY = "279B20F7-232A-6EA2-FF38-8B84A9D31B00";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public  static List<Data> datas;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );


    }
}
