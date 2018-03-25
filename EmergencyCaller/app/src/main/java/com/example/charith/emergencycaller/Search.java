package com.example.charith.emergencycaller;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class Search extends AppCompatActivity {
    AutoCompleteTextView serchtxt;

    DatabaseHelper databaseHelper;
    ArrayAdapter arrayAdapter;
    Cursor res;
    String type="";
    private int REQUEST_CALL=1;
    String tel;
    String value;
    ArrayList<String> thelist;
    String telnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        database_initialize();
        Intent intent=getIntent() ;
        type=intent.getExtras().getString("type").toString();
        serchtxt=findViewById(R.id.auto_searchtxt_id);

       // Toast.makeText(getApplicationContext(),type,Toast.LENGTH_LONG).show();
       // check_status();

        set_autocomplete_search();

    }

    public void check_status(){
        Toast.makeText(getApplicationContext(),type,Toast.LENGTH_LONG).show();
    }


    public void set_autocomplete_search(){

        thelist=new ArrayList<String>();
        if(type.equals("hotline")){
            //Toast.makeText(getApplicationContext(),type,Toast.LENGTH_LONG).show();
            res=databaseHelper.custom_query("select * from hotline");
        }if(type.equals("emergency")){
            res=databaseHelper.custom_query("select * from emergency");
        } if(type.equals("university")){
            res=databaseHelper.custom_query("select * from university");
        }

        if(res.getCount()==0){
            //empty
        }else {
            while (res.moveToNext()){
                thelist.add(res.getString(0)+"    "+res.getString(1)+"    "+res.getString(2));
                arrayAdapter=new ArrayAdapter(Search.this,android.R.layout.select_dialog_item,thelist);
            }

            serchtxt.setThreshold(1);
            serchtxt.setAdapter(arrayAdapter);
            serchtxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    value=serchtxt.getText().toString();
                   // Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
                    show_msg("Do You Want to Get a Call ?",value);
                }
            });
        }
    }
   public void show_msg(String titile, final String msg){
        AlertDialog.Builder dialog=new AlertDialog.Builder(Search.this);

        dialog.setMessage(msg).setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get call
                telnumber=value;
                get_call();

            }
        });
        dialog.create();
        dialog.setTitle(titile);
        dialog.show();

    }
    public void get_call(){

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Search.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else {
            Intent  intent=new Intent(Intent.ACTION_CALL);

            String delimiter="   ";

            String[] temp=telnumber.split(delimiter);
            Toast.makeText(Search.this,"calling",Toast.LENGTH_LONG).show();
            if(type.equals("university")){
                 tel="tel:"+temp[2].trim();
                //Toast.makeText(Search.this,temp[2],Toast.LENGTH_LONG).show();
            }else {
                tel="tel:"+temp[1].trim();
                //Toast.makeText(Search.this,temp[1],Toast.LENGTH_LONG).show();
            }

            intent.setData(Uri.parse(tel));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                get_call();
            }
        }
    }

    public void database_initialize(){
        databaseHelper=new DatabaseHelper(getApplicationContext());
        try{
            databaseHelper.onUpgrade(databaseHelper.mdatabase,1,2);
            databaseHelper.createDatabase();
        }catch (IOException e){
            throw new Error("asdsd");
        }
        try{
            databaseHelper.openDatabase();
        }catch (SQLException sql){
            throw sql;
        }
    }




}
