package com.example.charith.emergencycaller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Charith on 3/23/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private  static String DB_NAME="emnumber";
    String DB_PATH=null;
    Context mcontext;
    SQLiteDatabase mdatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        mcontext=context;
        DB_PATH="/data/data/"+context.getPackageName()+"/databases/";
        Log.e("path",DB_PATH);
    }

    public void createDatabase()throws IOException {
        boolean dbExist=checkDatabase();
        if(dbExist){

        }else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            }catch (IOException e){
                throw new Error("copy err");
            }
        }
    }

    private boolean checkDatabase(){
        SQLiteDatabase checkDB=null;
        try {
            String path=DB_PATH+DB_NAME;
            checkDB=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){}
        if(checkDB !=null){
            checkDB.close();
        }
        return checkDB !=null?true:false;
    }
    private void copyDatabase() throws IOException{
        InputStream myinput=mcontext.getAssets().open(DB_NAME);
        String outfileName=DB_PATH+DB_NAME;
        OutputStream myoutput=new FileOutputStream(outfileName);
        byte[] buffer=new byte[10];
        int length;
        while ((length=myinput.read(buffer))>0){
            myoutput.write(buffer,0,length);
        }

        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void openDatabase()throws SQLException {
        String path=DB_PATH+DB_NAME;
        mdatabase=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
    }

    public boolean insert_data(String id,String name,String number,String table) throws SQLException{
       SQLiteDatabase db=this.getWritableDatabase();
        int new_id= Integer.parseInt(id)+1;

        ContentValues contentValues=new ContentValues();
        contentValues.put("id",new_id);
        contentValues.put("name",name);
        contentValues.put("number",number);
        long result=db.insert(table,null,contentValues);
        if(result == -1){
            return false;
        }else {
            return true;
        }

    }

    public Cursor get_data(String query){
        mdatabase=this.getReadableDatabase();
        Cursor res=mdatabase.rawQuery(query,null);
        return res;
    }

    @Override
    public synchronized void close() {
        if(mdatabase != null){
            mdatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public Cursor custom_query(String query){
        //openDatabase();
        Cursor res= mdatabase.rawQuery(query,null);
        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            try {
                copyDatabase();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }


}
