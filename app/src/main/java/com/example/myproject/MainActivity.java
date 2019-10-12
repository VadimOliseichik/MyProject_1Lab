package com.example.myproject;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    private final static String PERMISSION = Manifest.permission.READ_PHONE_STATE;
    private final static int PERMISSION_CODE = 1;
    private static boolean REQUESTCANCELED = false;
    private void showId(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewVersionName = findViewById(R.id.textViewVersionName);

        try {
            textViewVersionName.setText("Version: " + this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        showIDPhone();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            showId();

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        showIDPhone();
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    private void dialogForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app needs phones ID, get the permission")
                .setTitle("Request the permission")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        REQUESTCANCELED = false;
                        requestPermission(PERMISSION, PERMISSION_CODE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        REQUESTCANCELED = true;
                        showIDPhone();
                    }
                });
        builder.create().show();
    }

    private void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You don`t have permission. Do it request again?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogForPermission();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showIDPhone();
                    }
                });
        builder.create().show();
    }

    private void showIDPhone() {
        if (REQUESTCANCELED){
            showMessage();
        }
        else {
            if (ContextCompat.checkSelfPermission(this, PERMISSION)
                    != PackageManager.PERMISSION_GRANTED) {
                dialogForPermission();
            }
            else {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String phoneID = telephonyManager.getDeviceId();
                TextView phoneID_TV = findViewById(R.id.phoneID_Tv);
                phoneID_TV.setText(phoneID);
            }
        }

    }
}
