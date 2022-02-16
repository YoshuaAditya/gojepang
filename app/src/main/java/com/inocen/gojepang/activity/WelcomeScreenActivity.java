package com.inocen.gojepang.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.inocen.gojepang.R;
import com.inocen.gojepang.network.SampleDownloaderActivity;

public class WelcomeScreenActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen_activity);

        if(Build.VERSION.SDK_INT>=23) {
            requestWriteStorageFor23();
        }
        else {
            startApp();
        }
    }

    private void requestWriteStorageFor23() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(WelcomeScreenActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the storage is required for this app to Download material files.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("GOJEPANG", "Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }
        else startApp();
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i("GOJEPANG", "Permission has been denied by user");

                } else {

                    Log.i("GOJEPANG", "Permission has been granted by user");

                }
                startApp();
                return;
            }
        }
    }

    public void startApp(){
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(WelcomeScreenActivity.this, SampleDownloaderActivity.class));
//                startActivity(new Intent(WelcomeScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}
