package com.example.whatyouneed.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.whatyouneed.R;
import com.example.whatyouneed.model.Item;
import com.example.whatyouneed.util.Util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(Context context){
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        this.context = context;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + Util.TABLE_NAME
                + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_NAME + " INTEGER,"
                + Util.KEY_COLOR + " TEXT,"
                + Util.KEY_QUANTITY + " INTEGER,"
                + Util.KEY_SIZE + " INTEGER,"
                + Util.KEY_DATE + " LONG"
                + ")";

        db.execSQL(CREATE_ITEM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.db_drop);
        db.execSQL(DROP_TABLE + Util.DATABASE_NAME);

        onCreate(db);


    }


    //CRUD


    public void addItem(Item item ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Util.KEY_NAME, item.getItemName());
        values.put(Util.KEY_COLOR, item.getItemColor());
        values.put(Util.KEY_QUANTITY, item.getItemQuantity());
        values.put(Util.KEY_SIZE, item.getItemSize());
        values.put(Util.KEY_DATE, java.lang.System.currentTimeMillis());  //timestamp of system

        db.insert(Util.TABLE_NAME, null, values);

        Log.d("DB handler", "addItem: ");




    }


    public Item getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID,
                        Util.KEY_NAME,
                        Util.KEY_QUANTITY,
                        Util.KEY_SIZE,
                        Util.KEY_DATE},
                        Util.KEY_ID + "=?",
                new String[]{
                        String.valueOf(id)}, null, null, null, null

                        );
        if (cursor == null) {
            cursor.moveToFirst();
        }

        Item item = new Item();
        if (cursor != null) {
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Util.KEY_NAME)));
            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Util.KEY_QUANTITY)));
            item.setItemColor(cursor.getString(cursor.getColumnIndex(Util.KEY_COLOR)));
            item.setItemSize(cursor.getInt(cursor.getColumnIndex(Util.KEY_SIZE)));
            //convert time stamp from device
//            item.setDateItemAdded(String.format(Util.KEY_DATE, "dd-MM-yyyy hh:mm:ss").toString());
            DateFormat dateFormat = DateFormat.getDateInstance();

            String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE)))
            .getTime());

            item.setDateItemAdded(formatedDate);
        }
        return item;

    }

    public List<Item> getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID,
                        Util.KEY_NAME,
                        Util.KEY_QUANTITY,
                        Util.KEY_SIZE,
                        Util.KEY_DATE},
                        null, null, null, null, Util.KEY_DATE
                        + " DESC");//last item added is on the top

                if (cursor.moveToFirst()){
                    do {
                        Item item = new Item();
                        item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(Util.KEY_NAME)));
                        item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Util.KEY_QUANTITY)));
                        item.setItemColor(cursor.getString(cursor.getColumnIndex(Util.KEY_COLOR)));
                        item.setItemSize(cursor.getInt(cursor.getColumnIndex(Util.KEY_SIZE)));

                        //convert time stamp from device
                        DateFormat dateFormat = DateFormat.getDateInstance();

                        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE)))
                                .getTime());

                        item.setDateItemAdded(formatedDate);

                        itemList.add(item);
                    } while (cursor.moveToNext());
        }
                return itemList;

        
    }
}
