package com.example.charith.emergencycaller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Addnewrecord extends AppCompatActivity {

    EditText name_txt,number_txt;
    Button add_btn,cansal_btn;
    DatabaseHelper databaseHelper;
    String id,name,number,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewrecord);

        name_txt=findViewById(R.id.add_nametxt_id);
        number_txt=findViewById(R.id.add_numbertxt_id);
        add_btn=findViewById(R.id.add_addbtn_id);
        cansal_btn=findViewById(R.id.add_clearbtn_id);
        Intent intent=getIntent();
        type=intent.getExtras().getString("type");

        set_addbtn_listner();
        databaseHelper=new DatabaseHelper(getApplicationContext());
       // check_type();

    }

    public void check_type(){
        if(type.equals("emergency")){
            Toast.makeText(getApplicationContext(),"Emergency", Toast.LENGTH_LONG).show();
        } if(type.equals("police")){
            Toast.makeText(getApplicationContext(),"police", Toast.LENGTH_LONG).show();
        } if(type.equals("hotline")){
            Toast.makeText(getApplicationContext(),"hotline", Toast.LENGTH_LONG).show();
        } if(type.equals("university")){
            Toast.makeText(getApplicationContext(),"university", Toast.LENGTH_LONG).show();
        } if(type.equals("hospital")){
            Toast.makeText(getApplicationContext(),"hospital", Toast.LENGTH_LONG).show();
        }
    }

    public void set_addbtn_listner(){
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(),"add btn is clicked!!", Toast.LENGTH_LONG).show();
                name= String.valueOf(name_txt.getText());
                number= String.valueOf(number_txt.getText());
                if(check_numEmpty(name,number)){
                   // Toast.makeText(getApplicationContext(),"update can be done", Toast.LENGTH_LONG).show();
                    update_table(name,number);
                }else {
                    Toast.makeText(getApplicationContext(),"fields cannot be empty!!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void update_table(String name,String number){
        String id= get_last_id();
        if(id.equals("-1")){
            Toast.makeText(getApplicationContext(),"cannot Insert!", Toast.LENGTH_LONG).show();
        }else {
           // Toast.makeText(getApplicationContext(),"id:"+id, Toast.LENGTH_LONG).show();
            if(type.equals("emergency")){
                 boolean result=   databaseHelper.insert_data(id,name,number,"emergency");
                 if(result){
                     Toast.makeText(getApplicationContext(),"Success!!", Toast.LENGTH_LONG).show();
                 }else {
                     Toast.makeText(getApplicationContext(),"fail!!", Toast.LENGTH_LONG).show();
                 }
            }

        }

    }
    public String get_last_id(){
        String id="";
        if(type.equals("emergency")){
           Cursor res= databaseHelper.get_data("select * from emergency order by id desc limit 1");
           if(res.getCount()==0){
               return "-1";
               //empty
           }else {
               while (res.moveToNext()){
                   id=res.getString(0);
               }
               return id;
           }
        }else {
            return "-1";
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
}
