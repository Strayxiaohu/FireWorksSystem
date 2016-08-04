package com.xiaohu.fireworkssystem.model.bean;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.xiaohu.fireworkssystem.db.MyDBHelper;


import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class UserDao {
    private Context context;
    private Dao<UserBean, Integer> userDao;
    private MyDBHelper helper;

    public UserDao(Context context) {
        this.context = context;
        helper = MyDBHelper.getMyDBHelper(context);
        userDao = helper.getDao(UserBean.class);
    }

    //添加
    public void add(UserBean user) {
        try {
            userDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //删除
    public void delete(int id) {
        try {
            userDao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询所有
    public List<UserBean> QueryAll() {
        List<UserBean> list = null;
        try {
            list = userDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //条件查询
    public void QueryByName(String name, String value) {
        QueryBuilder builder = userDao.queryBuilder();
        try {
            builder.where().eq("name", "=1");
            builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        QueryBuilder<Article, Integer> queryBuilder = articleDaoOpe
//                .queryBuilder();
//        Where<Article, Integer> where = queryBuilder.where();
//        where.eq("user_id", 1);
//        where.and();
//        where.eq("name", "xxx");
//
//        //或者
//        articleDaoOpe.queryBuilder().//
//                where().//
//                eq("user_id", 1).and().//
//                eq("name", "xxx");

    }

}
