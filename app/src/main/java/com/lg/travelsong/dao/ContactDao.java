package com.lg.travelsong.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lg.travelsong.bean.Contact;
import com.lg.travelsong.dao.helper.MyContentValues;
import com.lg.travelsong.dao.helper.MyContextWrapper;
import com.lg.travelsong.dao.helper.MySQLiteOpenHelper;
import com.lg.travelsong.global.AppProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite通讯录表数据库操作
 *
 * @author LuoYi on 2016/8/18
 */
public class ContactDao {
    private MySQLiteOpenHelper helper;

    public ContactDao(Context context) {
        MyContextWrapper wrapper = new MyContextWrapper(context, AppProperty.dirPath());
        helper = new MySQLiteOpenHelper(wrapper);
    }

    /**
     * 增加通讯录
     *
     * @param contact 通讯录对象
     * @return 是否增加成功
     */
    public boolean add(Contact contact) {
        long result = -1;
        long resultsvr = -1;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new MyContentValues().getContentValues(contact);

        db.beginTransaction();
        try {
            //返回值：代表添加这个新行的Id ，-1代表添加失败
            result = db.insert("contact", null, values);
            if (result != -1) {
                //// TODO: 2016/8/19 同时增加服务器端
                resultsvr = 1;
                if (resultsvr != -1) {
                    db.setTransactionSuccessful();//标记事务中的sql语句全部成功执行
                }
            }
            db.close();
        } finally {
            db.endTransaction();//判断事务的标记是否成功，如果不成功，回滚错误之前执行的sql语句
        }
        return (result != -1 && resultsvr != -1) ? true : false;
    }

    /**
     * 删除通讯录
     *
     * @param usercode 用户号
     * @return 成功删除了多少行
     */
    public int delete(String usercode) {
        int result = 0;
        int resultsvr = 0;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();

        db.beginTransaction();
        try {
            //返回值：成功删除了多少行
            result = db.delete("contact", "usercode = ?", new String[]{usercode});
            if (result != 0) {
                //// TODO: 2016/8/19 同时删除服务器端
                resultsvr = 1;
                if (resultsvr != result) {
                    db.setTransactionSuccessful();//标记事务中的sql语句全部成功执行
                }
            }
            db.close();
        } finally {
            db.endTransaction();//判断事务的标记是否成功，如果不成功，回滚错误之前执行的sql语句
        }
        return resultsvr == result ? result : 0;
    }

    /**
     * 更新通讯录
     *
     * @param contact 通讯录对象
     * @return 成功修改了多少行
     */
    public int update(Contact contact) {
        int result = 0;
        int resultsvr = 0;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new MyContentValues().getContentValues(contact);

        db.beginTransaction();
        try {
            //返回值：代表成功修改了多少行
            result = db.update("contact", values, "usercode = ?", new String[]{contact.usercode});
            if (result != -1) {
                //// TODO: 2016/8/19 同时修改服务器端
                resultsvr = 1;
                if (resultsvr != -1) {
                    db.setTransactionSuccessful();//标记事务中的sql语句全部成功执行
                }
            }
            db.close();
        } finally {
            db.endTransaction();//判断事务的标记是否成功，如果不成功，回滚错误之前执行的sql语句
        }
        return resultsvr == result ? result : 0;
    }

    /**
     * 查询某个通讯录
     *
     * @param usercode 用户号
     * @return 查询到的通讯录
     */
    public Contact queryOne(String usercode) {
        Contact contact = null;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select usercode, nickname, username, userhead from contact where usercode = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{usercode});
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                contact = new Contact();
                contact.usercode = cursor.getString(0);
                contact.nickname = cursor.getString(1);
                contact.username = cursor.getString(2);
                contact.userhead = cursor.getString(3);
            }
            cursor.close();//关闭结果集
        }
        //关闭数据库对象
        db.close();
        return contact;
    }

    /**
     * 按SQL语句查询通讯录
     *
     * @param sql 查询语句
     * @return 查询到的通讯录list
     */
    public List<Contact> query(String sql) {
        List<Contact> list = null;
        Contact contact = null;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            list = new ArrayList<>();
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                contact = new Contact();
                contact.usercode = cursor.getString(cursor.getColumnIndex("usercode"));
                contact.nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                contact.username = cursor.getString(cursor.getColumnIndex("username"));
                contact.userhead = cursor.getString(cursor.getColumnIndex("userhead"));
                list.add(contact);
            }
            cursor.close();//关闭结果集
        }
        //关闭数据库对象
        db.close();
        return list;
    }

}
