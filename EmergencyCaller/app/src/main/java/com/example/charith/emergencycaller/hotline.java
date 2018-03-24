package com.example.charith.emergencycaller;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class hotline extends AppCompatActivity {
    ListView listView;
    EditText serch_txt;
    DatabaseHelper databaseHelper;
    Cursor result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotline);
      //  serch_txt=findViewById(R.id.hotlineserchtxt_id);
        listView=findViewById(R.id.hot_listview_id);

        set_database_initialize();
        set_list_item();
    }

    public void set_database_initialize(){
        databaseHelper=new DatabaseHelper(getApplicationContext());
        try{
            //  databaseHelper.onUpgrade(databaseHelper.mdatabase,11,12);
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
        result=databaseHelper.custom_query("select * from hotline");
        ArrayList<String> thelist=new ArrayList<String >();
        if(result.getCount()==0){
            //Empty
        }else {
            while (result.moveToNext()){
                thelist.add(result.getString(0)+"   "+result.getString(1)+"   "+result.getString(2));
                ListAdapter listAdapter=new ArrayAdapter<>(this,R.layout.list_item,thelist);
                listView.setAdapter(listAdapter);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String value= String.valueOf(listView.getItemAtPosition(position));
                    show_msg("data",value);
                }
            });
        }



    }
    public void show_msg(String titile,String msg){
        AlertDialog.Builder dialog=new AlertDialog.Builder(hotline.this);

        dialog.setMessage(msg).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });
        dialog.create();
        dialog.setTitle(titile);
        dialog.show();

    }
}
