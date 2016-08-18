package com.lg.travelsong.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lg.travelsong.bean.Message;
import com.lg.travelsong.dao.helper.MyContentValues;
import com.lg.travelsong.dao.helper.MyContextWrapper;
import com.lg.travelsong.dao.helper.MySQLiteOpenHelper;
import com.lg.travelsong.global.AppProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite消息表数据库操作
 * @author LuoYi on 2016/8/18
 */
public class MessageDao {
    private MySQLiteOpenHelper helper;

    public MessageDao(Context context) {
        MyContextWrapper wrapper = new MyContextWrapper(context, AppProperty.dirPath());
        helper = new MySQLiteOpenHelper(wrapper);
    }

    /**
     * 增加消息
     *
     * @param message 消息对象
     * @return 是否增加成功
     */
    public boolean add(Message message) {
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new MyContentValues().getContentValues(message);
        //返回值：代表添加这个新行的Id ，-1代表添加失败
        long result = db.insert("message", null, values);
        db.close();
        return result != -1 ? true : false;
    }

    /**
     * 删除消息
     *
     * @param msgid 本地消息id
     * @return 成功删除了多少行
     */

    public int delete(int msgid) {
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        //返回值：成功删除了多少行
        int result = db.delete("message", "msgid = ?", new String[]{msgid + ""});
        db.close();
        return result;
    }

    /**
     * 更新消息
     *
     * @param message 消息对象
     * @return 成功修改了多少行
     */
    public int update(Message message) {
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new MyContentValues().getContentValues(message);
        //返回值：代表成功修改了多少行
        int result = db.update("message", values, "msgid = ?", new String[]{message.msgid + ""});
        db.close();
        return result;
    }

    /**
     * 查询某条消息
     *
     * @param msgid 消息号
     * @return 查询到的消息
     */
    public Message queryOne(int msgid) {
        Message message = null;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select msgid, msgsvrid, mtype, status, issend, createtime, usercode, mcontent, imgpath, reserved, hasread from message where msgid = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{msgid + ""});
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                message = new Message();
                message.msgid = cursor.getInt(0);
                message.msgsvrid = cursor.getString(1);
                message.mtype = cursor.getInt(2);
                message.status = cursor.getInt(3);
                message.issend = cursor.getInt(4);
                message.createtime = cursor.getInt(5);
                message.usercode = cursor.getString(6);
                message.mcontent = cursor.getString(7);
                message.imgpath = cursor.getString(8);
                message.reserved = cursor.getString(9);
                message.hasread = cursor.getInt(10);
            }
            cursor.close();//关闭结果集
        }
        //关闭数据库对象
        db.close();
        return message;
    }

    /**
     * 查询某人的消息
     *
     * @param usercode 用户号
     * @return 查询到的消息list
     */
    public List<Message> queryOne(String usercode) {
        List<Message> list = null;
        Message message = null;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select msgid, msgsvrid, mtype, status, issend, createtime, usercode, mcontent, imgpath, reserved, hasread from message where usercode = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{usercode});
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            list = new ArrayList<>();
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                message = new Message();
                message.msgid = cursor.getInt(0);
                message.msgsvrid = cursor.getString(1);
                message.mtype = cursor.getInt(2);
                message.status = cursor.getInt(3);
                message.issend = cursor.getInt(4);
                message.createtime = cursor.getInt(5);
                message.usercode = cursor.getString(6);
                message.mcontent = cursor.getString(7);
                message.imgpath = cursor.getString(8);
                message.reserved = cursor.getString(9);
                message.hasread = cursor.getInt(10);
                list.add(message);
            }
            cursor.close();//关闭结果集
        }
        //关闭数据库对象
        db.close();
        return list;
    }

    /**
     * 按SQL语句查询消息
     *
     * @param sql 查询语句
     * @return 查询到的消息list
     */
    public List<Message> query(String sql) {
        List<Message> list = null;
        Message message = null;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            list = new ArrayList<>();
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                message = new Message();
                message.msgid = cursor.getInt(cursor.getColumnIndex("msgid"));
                message.msgsvrid = cursor.getString(cursor.getColumnIndex("msgsvrid"));
                message.mtype = cursor.getInt(cursor.getColumnIndex("mtype"));
                message.status = cursor.getInt(cursor.getColumnIndex("status"));
                message.issend = cursor.getInt(cursor.getColumnIndex("issend"));
                message.createtime = cursor.getInt(cursor.getColumnIndex("createtime"));
                message.usercode = cursor.getString(cursor.getColumnIndex("usercode"));
                message.mcontent = cursor.getString(cursor.getColumnIndex("mcontent"));
                message.imgpath = cursor.getString(cursor.getColumnIndex("imgpath"));
                message.reserved = cursor.getString(cursor.getColumnIndex("reserved"));
                message.hasread = cursor.getInt(cursor.getColumnIndex("hasread"));
                list.add(message);
            }
            cursor.close();//关闭结果集
        }
        //关闭数据库对象
        db.close();
        return list;
    }

}
