package com.lg.travelsong.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String相关工具类
 * @author LuoYi on 2016/8/9
 */
public class MyStringUtils {
    /**
     * 验证合法邮箱
     * @param email 邮箱
     * @return 是否合法
     */
    public static Boolean validEmail (String email){
        String regex = "^[a-z0-9A-Z_-]+@[a-z0-9A-Z_-]+(\\.[a-z0-9A-Z_-]+)+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 验证合法手机号
     * @param phone 手机号
     * @return 是否合法
     */
    public static Boolean validPhone (String phone){
        String regex = "^1[3-8]{1}[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
