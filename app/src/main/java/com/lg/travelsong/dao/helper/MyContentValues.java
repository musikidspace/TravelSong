package com.lg.travelsong.dao.helper;

import android.content.ContentValues;

import com.lg.travelsong.utils.MyLogUtils;

import java.lang.reflect.Field;

/**
 * 传入JavaBean对象，返回所有属性的ContentValues
 *
 * @author LuoYi on 2016/8/19
 */
public class MyContentValues {

    public ContentValues getContentValues(Object obj) {
        ContentValues contentValues = new ContentValues();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String type = field.getGenericType().toString(); // 获取属性的类型
                if ("class java.lang.String".equals(type)) {
                    contentValues.put(field.getName(), (String) field.get(obj));
                } else if ("class java.lang.Integer".equals(type) || "int".equals(type)) {
                    contentValues.put(field.getName(), (Integer) field.get(obj));
                } else if ("class java.lang.Boolean".equals(type) || "boolean".equals(type)) {
                    contentValues.put(field.getName(), (Boolean) field.get(obj));
                } else if ("class java.lang.Double".equals(type) || "double".equals(type)) {
                    contentValues.put(field.getName(), (Double) field.get(obj));
                } else if ("class java.lang.Long".equals(type) || "long".equals(type)) {
                    contentValues.put(field.getName(), (Long) field.get(obj));
                } else if ("class java.lang.Float".equals(type) || "float".equals(type)) {
                    contentValues.put(field.getName(), (Float) field.get(obj));
                } else if ("class java.lang.Byte".equals(type) || "byte".equals(type)) {
                    contentValues.put(field.getName(), (Byte) field.get(obj));
                } else if ("class [Ljava.lang.Byte".equals(type) || "class [B".equals(type)) {
                    contentValues.put(field.getName(), (byte[]) field.get(obj));
                } else if ("class java.lang.Short".equals(type) || "short".equals(type)) {
                    contentValues.put(field.getName(), (Short) field.get(obj));
                } else {
                    throw new Exception("getContentValues 只支持String.Integer.Boolean.Double.Long.Float.Byte.byte[].Short类型的属性");
                }
            } catch (Exception e) {
                MyLogUtils.logCatch("MyContentValues-->getContentValues", e.getMessage());
            }
        }
        return contentValues;
    }
}