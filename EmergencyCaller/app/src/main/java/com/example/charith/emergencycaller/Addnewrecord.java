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
        set_cansalbtn_listner();
        databaseHelper=new DatabaseHelper(getApplicationContext());
       // check_type();

    }
    public  void set_cansalbtn_listner(){
        cansal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_txt.setText("");
                number_txt.setText("");

            }
        });
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

    public void update_table(String name,String number){
        String id= get_last_id();
        if(is_duplicate(number)){
            Toast.makeText(getApplicationContext(),"the Number is already Exsit!!", Toast.LENGTH_LONG).show();
        }else {
            if(id.equals("-1")){
                Toast.makeText(getApplicationContext(),"cannot Insert!", Toast.LENGTH_LONG).show();
            }else {
                // Toast.makeText(getApplicationContext(),"id:"+id, Toast.LENGTH_LONG).show();
                if(type.equals("emergency")){
                    boolean result=   databaseHelper.insert_data(id,name,number,"emergency");
                    if(result){
                        Intent intent=new Intent(getApplicationContext(),emergency.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Success!!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"fail!!", Toast.LENGTH_LONG).show();
                    }
                } if(type.equals("hospital")){
                    boolean result= databaseHelper.insert_data(id,name,number,"hospital");
                    if(result){
                        Intent intent=new Intent(getApplicationContext(),hospital.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Success!!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"fail!!", Toast.LENGTH_LONG).show();
                    }
                } if(type.equals("hotline")){
                    boolean result=   databaseHelper.insert_data(id,name,number,"hotline");
                    if(result){
                        Intent intent=new Intent(getApplicationContext(),hotline.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Success!!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"fail!!", Toast.LENGTH_LONG).show();
                    }
                } if(type.equals("police")){
                    boolean result=   databaseHelper.insert_data(id,name,number,"police");
                    if(result){
                        Intent intent=new Intent(getApplicationContext(),police.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Success!!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"fail!!", Toast.LENGTH_LONG).show();
                    }
                } if(type.equals("university")){
                    boolean result=   databaseHelper.insert_data(id,name,number,"university");
                    if(result){
                        Intent intent=new Intent(getApplicationContext(),university.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Success!!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"fail!!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }


    public String get_last_id(){
        String id="";
        Cursor res;
        if(type.equals("emergency")){
            res= databaseHelper.get_data("select * from emergency order by id desc limit 1");
        } else if(type.equals("hospital")){
            res= databaseHelper.get_data("select * from hospital order by id desc limit 1");
        }else if(type.equals("hotline")){
            res= databaseHelper.get_data("select * from hotline order by id desc limit 1");
        } else if(type.equals("police")){
            res= databaseHelper.get_data("select * from police order by id desc limit 1");
        }else{
            res= databaseHelper.get_data("select * from university order by id desc limit 1");
        }


        if(res.getCount()==0){
          //empty
            return "-1";
        }else {
            while (res.moveToNext()){
                id=res.getString(0);
            }
            return id;
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
