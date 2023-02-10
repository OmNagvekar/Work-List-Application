package com.example.worklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLDataException;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    public  static int unique=888888;
    public DatabaseManager(Context ctx){
        context =ctx;
    }
    public DatabaseManager open() throws SQLDataException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        dbHelper.close();
    }


    public void insert(String Title,String Description,String InDateTime,String RDateTime,int checkbox,int Complete){
        String qry = "select MAX (Id) from TASK";
        int uid;
        Cursor cr = database.rawQuery(qry, null);
        if (cr==null){
            Log.d ("Tag12","Database is empty");
            uid =1;
            unique =uid;
        }
        else {
            cr.moveToFirst();
            uid = cr.getInt(0) + 1;
            unique = uid;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("Title",Title);
        contentValues.put("Description",Description);
        contentValues.put("INDateTime",InDateTime);
        contentValues.put("RDateTime",RDateTime);
        contentValues.put("Checkbox",checkbox);
        contentValues.put("Complete",Complete);
        contentValues.put("Uid",uid);
        database.insert("TASK",null,contentValues);


    }

    public Cursor fetch()
    {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
       // String [] columns=new String[] {"Id","Title","INDateTime","RDateTime","Checkbox","Complete"};

        String qry = "select * from TASK order by Complete Asc, Checkbox Desc,Id desc";


        Cursor cursor = database.rawQuery(qry, null);

//        Cursor cursor = database.query("TASK",columns,null,null,null,null,null);//orderBy is used sort
//        if (cursor !=null){
//            cursor.moveToFirst();
//
//        }
        return cursor;
    }

    public int update(long id,String Title,String Description,String InDateTime,String RDateTime,int checkbox,int Complete){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Title",Title);
        contentValues.put("Description",Description);
        contentValues.put("INDateTime",InDateTime);
        contentValues.put("RDateTime",RDateTime);
        contentValues.put("Checkbox",checkbox);
        contentValues.put("Complete",Complete);
        int ret = database.update("Task",contentValues, "Id="+id,null);
        return ret;
    }

    public void delete(long id){

        database.delete("TASK","Id="+id,null);
    }
}
