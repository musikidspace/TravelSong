package com.lg.travelsong.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lg.travelsong.bean.Conversation;
import com.lg.travelsong.dao.helper.MyContentValues;
import com.lg.travelsong.dao.helper.MyContextWrapper;
import com.lg.travelsong.dao.helper.MySQLiteOpenHelper;
import com.lg.travelsong.global.AppProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite会话列表表的数据库操作
 *
 * @author LuoYi on 2016/8/18
 */
public class ConversationDao {
    private MySQLiteOpenHelper helper;

    public ConversationDao(Context context) {
        MyContextWrapper wrapper = new MyContextWrapper(context, AppProperty.dirPath());
        helper = new MySQLiteOpenHelper(wrapper);
    }

    /**
     * 增加会话
     *
     * @param conversation 会话对象
     * @return 是否增加成功
     */
    public boolean add(Conversation conversation) {
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new MyContentValues().getContentValues(conversation);
        //返回值：代表添加这个新行的Id ，-1代表添加失败
        long result = db.insert("conversation", null, values);
        db.close();
        return result != -1 ? true : false;
    }

    /**
     * 删除会话
     *
     * @param usercode 用户号
     * @return 成功删除了多少行
     */

    public int delete(String usercode) {
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        //返回值：成功删除了多少行
        int result = db.delete("conversation", "usercode = ?", new String[]{usercode});
        db.close();
        return result;
    }

    /**
     * 更新会话
     *
     * @param conversation 会话对象
     * @return 成功修改了多少行
     */
    public int update(Conversation conversation) {
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new MyContentValues().getContentValues(conversation);
        //返回值：代表成功修改了多少行
        int result = db.update("conversation", values, "usercode = ?", new String[]{conversation.usercode});
        db.close();
        return result;
    }

    /**
     * 查询某个会话
     *
     * @param usercode 用户号
     * @return 查询到的会话
     */
    public Conversation queryOne(String usercode) {
        Conversation conversation = null;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from conversation where usercode = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{usercode});
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                conversation = new Conversation();
                setAll(conversation, cursor);
            }
            cursor.close();//关闭结果集
        }
        //关闭数据库对象
        db.close();
        return conversation;
    }

    /**
     * 查询limit条hasdelete为0的会话
     *
     * @param limit  每次查询的条数
     * @param offset 从第几条记录“之后“开始查询
     * @return 查询到的会话list
     */
    public List<Conversation> queryLimit(int limit, int offset) {
        List<Conversation> list = null;
        Conversation conversation = null;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from conversation order by lasttime limit " + limit + " offset " + offset;
        Cursor cursor = db.rawQuery(sql, null);
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            list = new ArrayList<>();
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                conversation = new Conversation();
                setAll(conversation, cursor);
                list.add(conversation);
            }
            cursor.close();//关闭结果集
        }
        //关闭数据库对象
        db.close();
        return list;
    }

    /**
     * 按SQL语句查询会话
     *
     * @param sql 查询语句
     * @return 查询到的会话list
     */
    public List<Conversation> query(String sql) {
        List<Conversation> list = null;
        Conversation conversation = null;
        //调用getReadableDatabase方法,来初始化数据库的创建或读取
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            list = new ArrayList<>();
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                conversation = new Conversation();
                setAll(conversation, cursor);
                list.add(conversation);
            }
            cursor.close();//关闭结果集
        }
        //关闭数据库对象
        db.close();
        return list;
    }

    //设置属性
    private void setAll(Conversation conversation, Cursor cursor){
        conversation.userhead = cursor.getString(cursor.getColumnIndex("userhead"));
        conversation.usercode = cursor.getString(cursor.getColumnIndex("usercode"));
        conversation.nickname = cursor.getString(cursor.getColumnIndex("nickname"));
        conversation.username = cursor.getString(cursor.getColumnIndex("username"));
        conversation.unreadcount = cursor.getInt(cursor.getColumnIndex("unreadcount"));
        conversation.lasttime = cursor.getInt(cursor.getColumnIndex("lasttime"));
        conversation.lastmsg = cursor.getString(cursor.getColumnIndex("lastmsg"));
        conversation.ctype = cursor.getInt(cursor.getColumnIndex("ctype"));
        conversation.mute = cursor.getInt(cursor.getColumnIndex("mute"));
        conversation.istop = cursor.getInt(cursor.getColumnIndex("istop"));
    }
}
