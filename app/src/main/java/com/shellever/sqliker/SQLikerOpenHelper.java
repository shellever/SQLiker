package com.shellever.sqliker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLikerOpenHelper extends SQLiteOpenHelper {

    private String CREATE_TABLE_SQL = "";
    private String DROP_TABLE_SQL = "";

    public SQLikerOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SQL); // drop table
        onCreate(db);               // create table
    }

    public void setCreateTableSql(String createTableSql) {
        CREATE_TABLE_SQL = createTableSql;
    }

    public void setDropTableSql(String dropTableSql) {
        DROP_TABLE_SQL = dropTableSql;
    }
}
