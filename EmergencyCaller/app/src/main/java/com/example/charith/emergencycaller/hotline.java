package com.example.charith.emergencycaller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class hotline extends AppCompatActivity {
    ListView listView;
    EditText serch_txt;

    String[] name={"sadsda","dsad","sdadfdf","trst","sfadfsdf","sdadfsdfsdfsdf","sdfadfsdfsdfsd","sfsdfsdfsdf",
    "text1","test2","test3","test4","test5","test6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotline);
      //  serch_txt=findViewById(R.id.hotlineserchtxt_id);
        listView=findViewById(R.id.hot_listview_id);
        
        set_list_item();
    }

    public void set_list_item(){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,name);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value= String.valueOf(listView.getItemAtPosition(position));
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
            }
        });
    }
}
