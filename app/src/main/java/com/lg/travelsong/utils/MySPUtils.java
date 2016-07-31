package com.lg.travelsong.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 * @author LuoYi on 2016/8/1
 */
public class MySPUtils {

    private static SharedPreferences sp;

    public static String getString(Context context, String key){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        return sp.getString(key, "");
    }

    public static boolean getBoolean(Context context, String key){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        return sp.getBoolean(key, false);
    }

    public static int getInt(Context context, String key){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        return sp.getInt(key, 0);
    }

    public static long getLong(Context context, String key){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        return sp.getLong(key, 0);
    }

    public static float getFloat(Context context, String key){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        return sp.getFloat(key, 0);
    }

    public static void putString(Context context, String key, String value){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putBoolean(Context context, String key, boolean value){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static void putInt(Context context, String key, int value){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public static void putLong(Context context, String key, long value){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    public static void putFloat(Context context, String key, float value){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static boolean contains(Context context, String key){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        return sp.contains(key);
    }

    public static void remove(Context context, String key){
        if (sp == null){
            sp = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}