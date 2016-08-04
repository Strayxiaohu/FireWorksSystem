package com.xiaohu.fireworkssystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.xiaohu.fireworkssystem.model.CodeModel;
import com.xiaohu.fireworkssystem.model.bean.UserBean;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/28.
 */
public class MyDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME="sqlite_fireworks.db";
    private Map<String,Dao> daos=new HashMap<String,Dao>();
    private MyDBHelper(Context context){
        super(context,TABLE_NAME,null,4);
    }
    public MyDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, UserBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,CodeModel.class,true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static MyDBHelper myDBHelper;
    //单例获取该helper
    public static synchronized MyDBHelper getMyDBHelper(Context context){
        context=context.getApplicationContext();
        if(myDBHelper==null){
            synchronized (MyDBHelper.class){
                if(myDBHelper==null){
                    myDBHelper=new MyDBHelper(context);
                }
            }
        }
        return myDBHelper;
    }
    public synchronized Dao getDao(Class clazz){
        Dao dao=null;
        String className=clazz.getSimpleName();
        if(daos.containsKey(className)){
            dao=daos.get(className);
        }
        if(dao==null){
            try {
                dao=super.getDao(clazz);
                daos.put(className,dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        for(String key:daos.keySet()){
            Dao dao=daos.get(key);
            dao=null;
        }
    }
}
