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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class university extends AppCompatActivity {

    ListView listView;
    String call_number="";
    DatabaseHelper databaseHelper;
    Cursor result;
    private int REQUEST_CALL=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        listView=findViewById(R.id.universitylistview_id);

        set_database_initialize();
        set_list_item();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.hotlinemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.serch_item){
          //  Toast.makeText(getApplicationContext(),"serch icon is clicked!!",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getApplication(),Search.class);
            intent.putExtra("type","university");
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    public void set_database_initialize(){
        databaseHelper=new DatabaseHelper(getApplicationContext());
        try{
            databaseHelper.onUpgrade(databaseHelper.mdatabase,2,3);
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

    public void set_list_item(){

        /*ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,name);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value= String.valueOf(listView.getItemAtPosition(position));
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
            }
        });*/
        result=databaseHelper.custom_query("select * from university");
        ArrayList<String> thelist=new ArrayList<String >();
        if(result.getCount()==0){
            //Empty
        }else {
            while (result.moveToNext()){
                thelist.add(result.getString(1)+"   "+result.getString(2));
                ListAdapter listAdapter=new ArrayAdapter<>(this,R.layout.list_item,thelist);
                listView.setAdapter(listAdapter);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String value= String.valueOf(listView.getItemAtPosition(position));
                    show_msg("Do You Want to Get a Call ?",value);
                }
            });
        }



    }
    public void show_msg(String titile, final String msg){
        AlertDialog.Builder dialog=new AlertDialog.Builder(university.this);

        dialog.setMessage(msg).setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get call
                call_number=msg;
                get_call();

            }
        });
        dialog.create();
        dialog.setTitle(titile);
        dialog.show();

    }

    public void get_call(){

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(university.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else {
            Intent intent=new Intent(Intent.ACTION_CALL);

            String delimiter="   ";

            String[] temp=call_number.split(delimiter);
            Toast.makeText(university.this,temp[1],Toast.LENGTH_LONG).show();
            String tel="tel:"+temp[1].trim();


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


}
