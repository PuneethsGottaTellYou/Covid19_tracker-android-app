package com.example.covidapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {


    private Button permissionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
            return;
        }
        permissionButton = findViewById(R.id.permissionButton);

        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Dexter.withActivity(MainActivity.this)
                       .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                       .withListener(new PermissionListener() {
                           @Override
                           public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                               startActivity(new Intent(MainActivity.this, HomeActivity.class));
                               finish();
                               return;

                           }

                           @Override
                           public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                               if(permissionDeniedResponse.isPermanentlyDenied())
                               {
                                   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                   builder.setTitle("Permission Denied")
                                           .setMessage("Access to fine location is restricted, go to settings to allow permission")
                                           .setNegativeButton("Cancel", null)
                                           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialogInterface, int i) {
                                                   Intent intent = new Intent();
                                                   intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                   intent.setData(Uri.fromParts("package", getPackageName(), null));
                                               }
                                           }).show();
                               }else
                               {
                                   Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                               }
                           }

                           @Override
                           public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken)
                           {
                               permissionToken.continuePermissionRequest();
                           }
                       }).check();
            }
        });
    }
}