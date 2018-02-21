package com.shapps.cryptocompare.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.shapps.cryptocompare.model.ExchangeDetailsSchema.ExchangesDetailsEntry.*;

/**
 * Created by shyam on 14/11/17.
 */

public class ExchangeDetailsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Exchanges.db";
    private static String SQL_CREATE_ENTRIES = "";
    private static String SQL_DELETE_ENTRIES = "";

    public ExchangeDetailsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_CRYPTO_CURRENCY + " TEXT," +
                        COLUMN_NAME_CURRENCY + " TEXT," +
                        COLUMN_NAME_EX_NAME + " TEXT," +
                        COLUMN_NAME_BUY_FEE + " TEXT," +
                        COLUMN_NAME_SELL_FEE + " TEXT," +
                        COLUMN_NAME_APP_ADDRESS + " TEXT," +
                        COLUMN_NAME_WEB_ADDRESS + " TEXT," +
                        COLUMN_NAME_ACTIVE + " INTEGER)";

        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
