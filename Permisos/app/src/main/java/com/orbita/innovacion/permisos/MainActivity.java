package com.orbita.innovacion.permisos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button camara, contactos;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_CONTACTS_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camara = (Button) findViewById(R.id.btmCamara);
        contactos = (Button) findViewById(R.id.btmcontactos);

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    tomolafoto();
                } else {
                    int permiso = checkSelfPermission(Manifest.permission.CAMERA);
                    if (permiso != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 001);
                    } else
                        tomolafoto();
                }

            }
        });

        contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        leercontactos();
                    } else {
                        int permiso1 = checkSelfPermission(Manifest.permission.READ_CONTACTS);

                        if (permiso1 != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 004);
                        } else
                            leercontactos();
                    }

            }
        });

    }

    private void tomolafoto() {
        Intent intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intento);
        Toast.makeText(this, "Camara", Toast.LENGTH_SHORT).show();
    }

    private void leercontactos(){
        Intent intento = new Intent(MainActivity.this, Contactos.class);
        startActivity(intento);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    tomolafoto();
                }
                break;

            case REQUEST_CONTACTS_PERMISSION:
                final int numOfRequ = grantResults.length;
                final boolean isGran = numOfRequ == 2
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequ - 1];
                if (isGran) {
                    leercontactos();
                }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
