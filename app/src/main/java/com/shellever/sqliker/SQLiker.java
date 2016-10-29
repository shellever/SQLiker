package com.shellever.sqliker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// DataType
// SQLite       Java
// NULL         null
// INTEGER      int/boolean
// REAL         float/double
// TEXT         String
// BLOB         byte[]
public class SQLiker<T> {

    private Context context;                        // Toast testing
    private SQLikerOpenHelper helper;
    private String dbName = "liker.db";
    private int dbVersion = 1;
    private String tableName = "liker";
    private SQLiteDatabase db;
    private Map<String, Class<?>> fieldTypeMaps;     // <name, type>
    private Class<T> clazz;

    public SQLiker(Context context, Class<T> clazz) {
        this.context = context;
        this.clazz = clazz;
        initHelper(context, dbName, dbVersion, clazz);
    }

    public SQLiker(Context context, String name, int version, Class<T> clazz) {
        this.context = context;
        dbName = name;
        dbVersion = version;
        this.clazz = clazz;
        initHelper(context, name, version, clazz);
    }

    private void initHelper(Context context, String name, int version, Class<T> clazz) {
        helper = new SQLikerOpenHelper(context, name, version);
        fieldTypeMaps = new HashMap<>();
        tableName = clazz.getSimpleName();
        String sql = "CREATE TABLE " + tableName + " (";
        sql += "_id INTEGER PRIMARY KEY AUTOINCREMENT,";    // _id
        Field[] fields = clazz.getDeclaredFields();         // private & public & all
        for (Field field : fields) {
            String fieldName = field.getName();
            if ("serialVersionUID".equals(fieldName)) {     // 需要去掉此属性，不然会崩溃
                continue;
            }
            Class<?> type = field.getType();                // 每个数据类型都有对应的Class类对象
            String typeName = type.getSimpleName();
            switch (typeName) {
                case "String":
                    sql += fieldName + " TEXT,";
                    break;
                case "int":
                case "short":
                case "long":
                case "boolean":
                    sql += fieldName + " INTEGER,";
                    break;
                case "float":
                case "double":
                    sql += fieldName + " REAL,";
                    break;
            }
            fieldTypeMaps.put(fieldName, type);     // Map<String, Class<?>>
        }
        sql = sql.substring(0, sql.length() - 1);   // [0, sql.length()-1)
        sql += ")";
        Toast.makeText(context, sql, Toast.LENGTH_SHORT).show();     // test
        helper.setCreateTableSql(sql);
        sql = "DROP TABLE IF EXISTS " + tableName;
        Toast.makeText(context, sql, Toast.LENGTH_SHORT).show();     // test
        helper.setDropTableSql(sql);
    }

    private static Object getter(Object obj, String attr) {
        Object result = null;
        String methodName = "get" + attr.toUpperCase().charAt(0) + attr.substring(1);   // getMethod
        try {
            Method method = obj.getClass().getMethod(methodName);
            result = method.invoke(obj);                         //
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void setter(Object obj, String attr, Object value, Class<?> type) {
        String methodName = "set" + attr.toUpperCase().charAt(0) + attr.substring(1);   // setMethod
        try {
            Method method = obj.getClass().getMethod(methodName, type);     // public setter method in bean
            method.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(T t) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Set<String> fieldNameSet = fieldTypeMaps.keySet();
        for (String fieldName : fieldNameSet) {
            switch (fieldTypeMaps.get(fieldName).getSimpleName()) {
                case "String":
                    values.put(fieldName, (String) getter(t, fieldName));
                    break;
                case "int":
                    values.put(fieldName, (int) getter(t, fieldName));
                    break;
                case "float":
                    values.put(fieldName, (float) getter(t, fieldName));
                    break;
                case "double":
                    values.put(fieldName, (double) getter(t, fieldName));
                    break;
                case "boolean":
                    values.put(fieldName, (boolean) getter(t, fieldName));
                    break;
                case "short":
                    values.put(fieldName, (short) getter(t, fieldName));
                    break;
                case "long":
                    values.put(fieldName, (long) getter(t, fieldName));
                    break;
            }
        }
        db.insert(tableName, null, values);
        db.close();
    }

    // delete from User where name=?
    public void delete(String where) {
        db = helper.getWritableDatabase();
        String rawWhere = where.trim();
        String[] whereClause = rawWhere.split("\\s*[!<=>]+\\s*");
        String operation = rawWhere.substring(whereClause[0].length(), rawWhere.length() - whereClause[1].length()).trim();

        // delete from User where name=?
        String sql = "DELETE FROM " + tableName + " WHERE " + whereClause[0] + operation + "?";
        String[] args = {whereClause[1]};
        db.execSQL(sql, args);
        db.close();
    }

    // update User set age=?,asset=? where name=?
    public void update(T t, String where) {
        db = helper.getWritableDatabase();

        String rawWhere = where.trim();
        String[] whereClause = rawWhere.split("\\s*[!<=>]+\\s*");
        String operation = rawWhere.substring(whereClause[0].length(), rawWhere.length() - whereClause[1].length()).trim();

        ContentValues values = new ContentValues();
        Set<String> fieldNameSet = fieldTypeMaps.keySet();
        for (String fieldName : fieldNameSet) {
            switch (fieldTypeMaps.get(fieldName).getSimpleName()) {
                case "String":
                    values.put(fieldName, (String) getter(t, fieldName));
                    break;
                case "int":
                    values.put(fieldName, (int) getter(t, fieldName));
                    break;
                case "float":
                    values.put(fieldName, (float) getter(t, fieldName));
                    break;
                case "double":
                    values.put(fieldName, (double) getter(t, fieldName));
                    break;
                case "boolean":
                    values.put(fieldName, (boolean) getter(t, fieldName));
                    break;
                case "short":
                    values.put(fieldName, (short) getter(t, fieldName));
                    break;
                case "long":
                    values.put(fieldName, (long) getter(t, fieldName));
                    break;
            }
        }
        String whereClause2 = whereClause[0] + operation + "?";
        String[] whereArgs = {whereClause[1]};

        db.update(tableName, values, whereClause2, whereArgs);
        db.close();
    }

    // Map<Integer, User> mUserMap = query("name=Hyper");
    // 10/28/2016 - 暂时只支持一个条件的查询
    //
    // Map<Integer, User> mUserMap = query("asset>100, age>25");
    //
    @SuppressLint("UseSparseArrays")
    public Map<Integer, T> query(String where) {
        Map<Integer, T> results = new HashMap<>();
        db = helper.getReadableDatabase();          //
        Set<String> fieldNameSet = fieldTypeMaps.keySet();
        String rawWhere = where.trim();
        String[] whereClause = rawWhere.split("\\s*[!<=>]+\\s*");
        String operation = rawWhere.substring(whereClause[0].length(), rawWhere.length() - whereClause[1].length()).trim();

        // select * from User where name=?
        String sql = "SELECT * FROM " + tableName + " WHERE " + whereClause[0] + operation + "?";
        String[] args = {whereClause[1]};
        Cursor cursor = db.rawQuery(sql, args);

        int id;
        T t;
        while (cursor.moveToNext()) {
            try {
                t = clazz.newInstance();
                id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                for (String fieldName : fieldNameSet) {
                    switch (fieldTypeMaps.get(fieldName).getSimpleName()) {
                        case "String":
                            setter(t, fieldName, cursor.getString(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "int":
                            setter(t, fieldName, cursor.getInt(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "float":
                            setter(t, fieldName, cursor.getFloat(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "double":
                            setter(t, fieldName, cursor.getDouble(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "boolean":
                            setter(t, fieldName, cursor.getInt(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "short":
                            setter(t, fieldName, cursor.getShort(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "long":
                            setter(t, fieldName, cursor.getLong(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                    }
                }
                results.put(id, t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();

        return results;
    }

    // returnType: Map<int, T>
    // int -> id
    // T   -> User
    @SuppressLint("UseSparseArrays")
    public Map<Integer, T> queryAll() {
        Map<Integer, T> results = new HashMap<>();
        db = helper.getReadableDatabase();              //
        Set<String> fieldNameSet = fieldTypeMaps.keySet();

        // Okay
//        String sql = "SELECT _id,";
//
//        // 必须关掉AS的Instant Run，不然会在反射过程中出现非法字段$change
//        for (String fieldName : fieldNameSet) {
//            sql += fieldName + ",";
//        }
//        sql = sql.substring(0, sql.length() - 1);   // [0, sql.length()-1)
//        sql += " FROM " + tableName;

        // select * from User
        String sql = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(sql, null);

        int id;
        T t;
        while (cursor.moveToNext()) {
            try {
                t = clazz.newInstance();
                id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                for (String fieldName : fieldNameSet) {
                    switch (fieldTypeMaps.get(fieldName).getSimpleName()) {
                        case "String":
                            setter(t, fieldName, cursor.getString(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "int":
                            setter(t, fieldName, cursor.getInt(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "float":
                            setter(t, fieldName, cursor.getFloat(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "double":
                            setter(t, fieldName, cursor.getDouble(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "boolean":
                            setter(t, fieldName, cursor.getInt(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "short":
                            setter(t, fieldName, cursor.getShort(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "long":
                            setter(t, fieldName, cursor.getLong(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                    }
                }
                results.put(id, t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();

        return results;
    }

    public int getCount(){
        int count;
        db = helper.getReadableDatabase();
        String sql = "SELECT COUNT(_id) FROM " + tableName;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);       // 获取表的记录数目
        cursor.close();
        db.close();
        return count;
    }

    public List<T> findAll() {
        List<T> results = new ArrayList<>();

        db = helper.getReadableDatabase();
        Set<String> fieldNameSet = fieldTypeMaps.keySet();
        fieldNameSet.add("_id");            // extra field

        String[] columns = fieldNameSet.toArray(new String[fieldNameSet.size()]);
        Cursor cursor = db.query(true, tableName, columns, null, null, null, null, null, null);

//        String sql = "select name,gender,age from User";
//        Cursor cursor = db.rawQuery(sql, null);

        T t;
        while (cursor.moveToNext()) {
            try {
                t = clazz.newInstance();
                for (String fieldName : fieldNameSet) {
                    switch (fieldTypeMaps.get(fieldName).getSimpleName()) {
                        case "String":
                            setter(t, fieldName, cursor.getString(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "int":
                            setter(t, fieldName, cursor.getInt(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "float":
                            setter(t, fieldName, cursor.getFloat(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "double":
                            setter(t, fieldName, cursor.getDouble(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "boolean":
                            setter(t, fieldName, cursor.getInt(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "short":
                            setter(t, fieldName, cursor.getShort(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                        case "long":
                            setter(t, fieldName, cursor.getLong(cursor.getColumnIndexOrThrow(fieldName)), fieldTypeMaps.get(fieldName));
                            break;
                    }
                }
                results.add(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();

        return results;
    }
}
