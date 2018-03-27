package com.example.charith.emergencycaller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Update_table extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText name_txt,number_txt;
    AutoCompleteTextView search_txt;
    Button update_btn,clear_btn;
    String type="";
    String value="";
    ArrayAdapter arrayAdapter1;
    ArrayList<String> thelist1;
    String name,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_table);
        databaseHelper=new DatabaseHelper(getApplicationContext());
        name_txt=findViewById(R.id.update_nametxt_id);
        number_txt=findViewById(R.id.update_numbertxt_id);
        search_txt=findViewById(R.id.update_serchtxt_id);
        update_btn=findViewById(R.id.update_updatebtn_id);
        clear_btn=findViewById(R.id.update_clearbtn_id);

        Intent intent=getIntent();
        type=intent.getExtras().getString("type");
       // Toast.makeText(getApplicationContext(),type, Toast.LENGTH_LONG).show();
        set_autocomplete_field();
       // set_updatebtn_listner();

    }

    public void set_updatebtn_listner(){
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               name= String.valueOf(name_txt.getText());
               number=number_txt.getText().toString();
               if(check_numEmpty(name,number)){
                   update_table(name,number);
               }else {
                   Toast.makeText(getApplicationContext(),"fields cannot be empty!!", Toast.LENGTH_LONG).show();
               }

            }
        });
    }

    public  void update_table(String name,String number){
        if(is_duplicate(number)){
            Toast.makeText(getApplicationContext(),"The number is Already Exsit!!", Toast.LENGTH_LONG).show();
        }else {
            //update
        }
    }
    public boolean is_duplicate(String num1){
        int check=3;
        Cursor dup= databaseHelper.get_data("select * from "+type);
        if(dup.getCount()==0){
            //empty//
            return true;
        }else {
            while (dup.moveToNext()){
                String num2=dup.getString(dup.getColumnIndex("number"));
                if(num1.equals(num2)){
                    check=1;
                    break;
                }
            }
            if(check==1){
                return true;
            }else {
                return false;
            }

        }

    }
    public boolean check_numEmpty(String name,String number){
        if(name.isEmpty() || number.isEmpty()){
            return  false;
        }else {
            if(number.trim().length()==10){
                return true;
            }else {
                Toast.makeText(getApplicationContext(),"number fiels must contain 10 numbers!", Toast.LENGTH_LONG).show();
                return  false;
            }

        }
    }

    public void set_autocomplete_field(){

        thelist1=new ArrayList<String>();
        Cursor res=databaseHelper.get_data("select * from "+type);
        if(res.getCount()==0){
            //empty
        }else {
            while (res.moveToNext()){
                thelist1.add(res.getString(0)+"    "+res.getString(1)+"    "+res.getString(2));
                arrayAdapter1=new ArrayAdapter(Update_table.this,android.R.layout.select_dialog_item,thelist1);
            }

            search_txt.setThreshold(1);
            search_txt.setAdapter(arrayAdapter1);
            search_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    value=search_txt.getText().toString();
                     Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
                 //   show_msg("Do You Want to Get a Call ?",value);
                }
            });
        }

    }
}
