package com.lg.travelsong.bean;

import com.lg.travelsong.utils.PinyinUtils;

/**
 * SQLite通讯录JavaBean
 *
 * @author LuoYi on 2016/8/18
 */
public class Contact implements Comparable<Contact> {
    public String usercode;
    public String nickname;
    public String username;
    public String userhead;

    //按拼音排序要实现接口
    @Override
    public int compareTo(Contact contact) {
        String showname = PinyinUtils.getPinyin(nickname != null ? nickname : (username != null ? username : usercode));
        String shownameC = PinyinUtils.getPinyin(contact.nickname != null ? contact.nickname :
                (contact.username != null ? contact.username : contact.usercode));
        return showname.compareTo(shownameC);
    }
}
