package com.example.whatyouneed.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.whatyouneed.R;
import com.example.whatyouneed.util.Util;

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
}
