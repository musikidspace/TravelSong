package com.lg.travelsong.utils;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    /**
     * MD5值
     *
     * @param paramString
     * @return
     */
    public static String md5(String paramString) {
        String returnStr;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            returnStr = byteToHexString(localMessageDigest.digest());
            return returnStr;
        } catch (Exception e) {
            return paramString;
        }
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 将unix时间戳转化为展示给用户的时间或日期
     * @param srvut 现在的时间
     * @param unix_timestamp 要转化的时间
     * @return
     */
    public static String unix_timestamp2TimeOrDate(int srvut, int unix_timestamp){
        String showTime;
        long dayut = srvut;//当天0点的时间戳
        //相差时间，如果为负在现在时间之后
        int differ = srvut - unix_timestamp;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        //将现在的时间转化成"yyyy年MM月dd日 HH:mm:ss"格式
        String date = sdf.format(new Date((long) srvut * 1000));
        try {
            dayut = sdf.parse(date.split(" ")[0] + " 00:00:00").getTime() / 1000;
        } catch (ParseException e) {
            MyLogUtils.logCatch("unix_timestamp2TimeOrDate", e.getMessage());
            return date.substring(12, 17);
        }
        if (differ < 0){
            showTime = "未来";
        } else if (differ < srvut - dayut){//显示今天的时间
            showTime = date.substring(12, 17);
        } else if (differ > srvut - dayut && differ < srvut - dayut + 86400){//昨天
            showTime = "昨天";
        } else if (differ > srvut - dayut && differ < srvut - dayut + 86400 + 86400){//前天
            showTime = "前天";
        } else {
            showTime = date.substring(5, 11);
        }
        return showTime;
    }

    /**
     * 将unix时间戳转化为展示给用户的时间或日期
     * 直接与本地时间比较
     * @param unix_timestamp 要转化的时间
     * @return
     */
    public static String unix_timestamp2TimeOrDate(int unix_timestamp){
        return unix_timestamp2TimeOrDate((int) (System.currentTimeMillis() / 1000), unix_timestamp);
    }

    /**
     * 获取第一个字符，不是26个字母的返回"#"
     * @param all
     * @return
     */
    public static String getFirstCharS(String all){
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        if (all.length() > 0){
            char first = all.charAt(0);
            for (int i = 0; i < chars.length; i ++){
                if (first == chars[i]){
                    return String.valueOf(first);
                }
            }
        }
        return "#";
    }

}
