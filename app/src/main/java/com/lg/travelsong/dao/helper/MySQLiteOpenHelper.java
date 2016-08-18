package com.lg.travelsong.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库及表;SQLite帮助类
 * 创建带路径的db，需传入重写得contecxt对象--MyContextWrapper
 * 默认databases文件夹下，传默认context对象
 *
 * @author LuoYi on 2016/8/17
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context) {
        super(context, "travelsong.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table conversation (csid integer primary key autoincrement, userhead varchar(200)," +
                " usercode varchar(30), nickname varchar(30), username varchar(30), lasttime integer," +
                " lastmsg varchar(2000), hasdelete integer default (0), istop integer default (0))";
        String sql2 = "create table contact (usercode varchar(30), nickname varchar(30)," +
                " username varchar(30), userhead varchar(200))";
        String sql3 = "create table message (msgid integer primary key autoincrement, msgsvrid varchar(40)," +
                " mtype integer default (1), status integer default (1), issend integer, createtime integer, usercode varchar(30)," +
                " mcontent varchar(2000), imgpath varchar(200), reserved varchar(20), hasread integer default (0))";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
        String isql = "insert into conversation (usercode, nickname, username, lasttime, lastmsg) " +
                " values ('githuber', '你好', 'hello', 1471524290, ' 有好多好多很多很多很多很" +
                " 多还打电话的霍华德很多很多很多很多很多很多好好的hdhdhdhddhhdhdhdhdhd');";
        String isql1 = "insert into conversation (usercode, nickname, username, lasttime, lastmsg) " +
                " values ('1', '1的昵称', 'work', 1471524512, '[微笑]')";
        String isql2 = "insert into conversation (usercode, nickname, username, lasttime)" +
                " values ('2', '2的昵称', 'world', 1471524591)";

        String isql3 = "insert into contact (usercode, nickname, username, userhead)" +
                " values ('githuber', '你好', 'hello', '')";
        String isql4 = "insert into contact (usercode, nickname, username, userhead)" +
                " values ('1', '1的昵称', 'work', '')";
        String isql5 = "insert into contact (usercode, nickname, username, userhead)" +
                " values ('2', '2的昵称', 'world', '')";

        String isql6 = "insert into message (msgsvrid, mtype, status, issend, createtime, usercode, mcontent, imgpath, reserved, hasread)" +
                " values ('021560vav51b51', 1, 2, 1, 1471524090, 'githuber', '暮冬时烤雪迟夏写长信','', '', 1)";
        String isql7 = "insert into message (msgsvrid, mtype, status, issend, createtime, usercode, mcontent, imgpath, reserved, hasread)" +
                " values ('afgatyh2635', 1, 3, 1, 1471524190, 'githuber', '早春不过一棵树','', '', 1)";
        String isql8 = "insert into message (msgsvrid, mtype, status, issend, createtime, usercode, mcontent, imgpath, reserved, hasread)" +
                " values ('zfghfe34567234567890', 1, 2, 1, 1471524290, 'githuber', '有好多好多很多很多很多很" +
                " 多还打电话的霍华德很多很多很多很多很多很多好好的hdhdhdhddhhdhdhdhdhd','', '', 1)";
        String isql9 = "insert into message (msgsvrid, mtype, status, issend, createtime, usercode, mcontent, imgpath, reserved, hasread)" +
                " values ('121123456', 1, 2, 1, 1471524512, '1', '[微笑]','', '', 0)";
        String isql10 = "insert into message (msgsvrid, mtype, status, issend, createtime, usercode, mcontent, imgpath, reserved, hasread)" +
                " values ('asdfghjkdf', 1, 3, 0, 1471524591, '2', null,'', '', 0)";

        sqLiteDatabase.execSQL(isql);
        sqLiteDatabase.execSQL(isql1);
        sqLiteDatabase.execSQL(isql2);
        sqLiteDatabase.execSQL(isql3);
        sqLiteDatabase.execSQL(isql4);
        sqLiteDatabase.execSQL(isql5);
        sqLiteDatabase.execSQL(isql6);
        sqLiteDatabase.execSQL(isql7);
        sqLiteDatabase.execSQL(isql8);
        sqLiteDatabase.execSQL(isql9);
        sqLiteDatabase.execSQL(isql10);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
