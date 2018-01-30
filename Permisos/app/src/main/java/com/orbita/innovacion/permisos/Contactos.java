package com.orbita.innovacion.permisos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Contactos extends AppCompatActivity {

    ListView contactos;

    private static final int REQUEST_TELEPHONE_PERMISSION = 1;
    private static final int REQUEST_MESSAGE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        contactos = (ListView) findViewById(R.id.lv01);

        final ArrayList contacts = new ArrayList();

        String[] projeccion = new String[] {ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE};

        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND " +
                ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";

        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projeccion,
                selectionClause,
                null,
                sortOrder
        );

        while(c.moveToNext()){
            contacts.add(new PojoContactos(c.getString(1), c.getString(2)));
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts);
        contactos.setAdapter(arrayAdapter);

        contactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object fhater = (PojoContactos) parent.getItemAtPosition(position);

                String name = ((PojoContactos)fhater).getNombre();
                String phone = ((PojoContactos)fhater).getNumero();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mandarsms(phone, name);
                } else {
                    int permiso = checkSelfPermission(Manifest.permission.SEND_SMS);
                    if (permiso != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 001);
                    } else
                        mandarsms(phone, name);
                }
            }
        });

        contactos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Object fhater = (PojoContactos) parent.getItemAtPosition(position);

                String name = ((PojoContactos)fhater).getNombre();
                String phone = ((PojoContactos)fhater).getNumero();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    hacerllamada(phone, name);
                } else {
                    int permiso = checkSelfPermission(Manifest.permission.CALL_PHONE);
                    if (permiso != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 001);
                    } else
                        hacerllamada(phone, name);
                }
                return true;
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void hacerllamada(String phone, String name) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
        Toast.makeText(Contactos.this, "Llamando a " + name, Toast.LENGTH_SHORT).show();
    }

    private void mandarsms(String phone, String name){
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
        smsIntent.putExtra("sms_body", "XD");
        startActivity(smsIntent);
        Toast.makeText(Contactos.this, "SMS a " + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_TELEPHONE_PERMISSION:
                final int numOfReques = grantResults.length;
                final boolean isGrante = numOfReques == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfReques - 1];
                /*if (isGrante) {
                    hacerllamada();
                }*/

            case REQUEST_MESSAGE_PERMISSION:
                final int numOfReque = grantResults.length;
                final boolean isGrant = numOfReque == 2
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfReque - 1];
                /*if (isGrant) {
                    mandarsms();
                }*/
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Retroceso ToolBar", Toast.LENGTH_SHORT).show();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Retroceso", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
