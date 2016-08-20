package com.lg.travelsong.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.travelsong.R;
import com.lg.travelsong.bean.Contact;
import com.lg.travelsong.bean.ResIdAndName;
import com.lg.travelsong.dao.ContactDao;
import com.lg.travelsong.manager.ThreadPool;
import com.lg.travelsong.utils.MyBitmapUtils;
import com.lg.travelsong.utils.MyDisplayUtils;
import com.lg.travelsong.utils.MyStringUtils;
import com.lg.travelsong.utils.PinyinUtils;
import com.lg.travelsong.widget.CustomPopupWindow;
import com.lg.travelsong.widget.QuickIndexBar;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LuoYi on 2016/8/20
 */
public class ContactActivity extends BaseActivity implements View.OnClickListener {

    private static final int MSG_QUERY_CONTACT_SUCCESS = 100;
    private Context mContext;
    private List<Contact> listCon;
    private Intent intent;
    private CustomPopupWindow popupWindow;
    private View view_for_pop;
    private ImageView iv_back;
    private ListView lv_contact;
    private QuickIndexBar qib_index;
    private TextView tv_current;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_QUERY_CONTACT_SUCCESS:
                    lv_contact.setAdapter(new ContactAdapter());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(mContext, "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add:
                popupWindow.showPopupWindow(view_for_pop);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mContext = this;
        //ActionBar自定义布局
        ActionBar ab = getActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //界面布局
        LinearLayout ll_actionbar = new LinearLayout(mContext);
        ll_actionbar.setOrientation(LinearLayout.HORIZONTAL);
        ll_actionbar.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        iv_back = new ImageView(mContext);
        iv_back.setImageBitmap(MyBitmapUtils.getInstance(mContext).readBitMap(R.drawable.actionbar_back));
        iv_back.setPadding(MyDisplayUtils.dp2px(mContext, 8), 0, MyDisplayUtils.dp2px(mContext, 8), 0);
        iv_back.setOnClickListener(this);
        TextView tv_back = new TextView(mContext);
        tv_back.setText(R.string.contact);
        tv_back.setTextColor(getResources().getColor(R.color.gray));
        tv_back.setTextSize(18);
        ll_actionbar.addView(iv_back, lp);
        ll_actionbar.addView(tv_back, lp);
        //设置进actionbar
        ab.setCustomView(ll_actionbar);

        view_for_pop = findViewById(R.id.view_for_pop);
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        qib_index = (QuickIndexBar) findViewById(R.id.qib_index);
        tv_current = (TextView) findViewById(R.id.tv_current);
        qib_index.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                //根据当前触摸的字母，去集合中找那个item的首字母和letter一样，然后将对应的item放到屏幕顶端
                for (int i = 0; i < listCon.size(); i++) {
                    String showname = listCon.get(i).nickname != null ? listCon.get(i).nickname
                            : (listCon.get(i).username != null ? listCon.get(i).username : listCon.get(i).usercode);
                    String firstCharS = MyStringUtils.getFirstCharS(PinyinUtils.getPinyin(showname));
                    if (letter.equals(firstCharS)) {
                        //说明找到了，那么应该讲当前的item放到屏幕顶端
                        lv_contact.setSelection(i);
                        break;//只需要找到第一个就行
                    }
                }
                //显示当前触摸的字母
                showCurrentWord(letter);
            }
        });

        //通过缩小currentWord来隐藏
        ViewHelper.setScaleX(tv_current, 0);
        ViewHelper.setScaleY(tv_current, 0);
    }

    private void initData() {
        //查询好友列表数据
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                listCon = new ContactDao(mContext).queryAll();
                Collections.sort(listCon);
                Message msg = mHandler.obtainMessage();
                msg.what = MSG_QUERY_CONTACT_SUCCESS;
                mHandler.sendMessage(msg);
            }
        });

        //初始化actionbar add按钮弹出框
        List<ResIdAndName> list = new ArrayList<>();
        ResIdAndName rn1 = new ResIdAndName(R.drawable.actionbar_add, getText(R.string.chat_group_chat).toString());
        ResIdAndName rn2 = new ResIdAndName(R.drawable.actionbar_back, getText(R.string.chat_add_contacts).toString());
        ResIdAndName rn3 = new ResIdAndName(R.drawable.actionbar_more, getText(R.string.chat_scan_qrcode).toString());
        ResIdAndName rn4 = new ResIdAndName(R.drawable.actionbar_search, getText(R.string.chat_money).toString());
        list.add(rn1);
        list.add(rn2);
        list.add(rn3);
        list.add(rn4);
        popupWindow = new CustomPopupWindow(mContext, list, new CustomPopupWindow.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
    }

    /**
     * 显示快速检索的文字
     */
    private boolean isScale = false;

    protected void showCurrentWord(String letter) {
        tv_current.setText(letter);
        if (!isScale) {
            isScale = true;
            ViewPropertyAnimator.animate(tv_current).scaleX(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(450).start();
            ViewPropertyAnimator.animate(tv_current).scaleY(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(450).start();
        }

        //先移除之前的任务
        mHandler.removeCallbacksAndMessages(null);

        //延时隐藏currentWord
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimator.animate(tv_current).scaleX(0f).setDuration(450).start();
                ViewPropertyAnimator.animate(tv_current).scaleY(0f).setDuration(450).start();
                isScale = false;
            }
        }, 1500);
    }

    @Override
    public void onClick(View view) {
        if (view == iv_back) {
            onBackPressed();
        }
    }

    class ContactAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listCon.size();
        }

        @Override
        public Object getItem(int i) {
            return listCon.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyBitmapUtils myBitmapUtils = MyBitmapUtils.getInstance(mContext);
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(mContext, R.layout.item_contact, null);
                holder.tv_word = (TextView) view.findViewById(R.id.tv_word);
                holder.iv_head = (ImageView) view.findViewById(R.id.iv_head);
                holder.tv_showname = (TextView) view.findViewById(R.id.tv_showname);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Bitmap bm = myBitmapUtils.readBitmapFromUrl((listCon.get(i).userhead));
            if (bm != null) {
                holder.iv_head.setImageBitmap(bm);
            }
            String showname = listCon.get(i).nickname != null ? listCon.get(i).nickname
                    : (listCon.get(i).username != null ? listCon.get(i).username : listCon.get(i).usercode);
            holder.tv_showname.setText(showname);

            String currentWord = MyStringUtils.getFirstCharS(PinyinUtils.getPinyin(showname));
            if (i > 0) {
                String lastShowname = listCon.get(i - 1).nickname != null ? listCon.get(i - 1).nickname
                        : (listCon.get(i - 1).username != null ? listCon.get(i - 1).username : listCon.get(i - 1).usercode);
                //获取上一个item的首字母
                String lastWord = MyStringUtils.getFirstCharS(PinyinUtils.getPinyin(lastShowname));
                //拿当前的首字母和上一个首字母比较
                if (currentWord.equals(lastWord)) {
                    //说明首字母相同，需要隐藏当前item的tv_word
                    holder.tv_word.setVisibility(View.GONE);
                } else {
                    //不一样，需要显示当前的首字母
                    //由于布局是复用的，所以在需要显示的时候，再次将tv_word设置为可见
                    holder.tv_word.setVisibility(View.VISIBLE);
                    holder.tv_word.setText(currentWord);
                }
            } else {
                holder.tv_word.setVisibility(View.VISIBLE);
                holder.tv_word.setText(currentWord);
            }

            return view;
        }
    }

    static class ViewHolder {
        public TextView tv_word;
        public ImageView iv_head;
        public TextView tv_showname;
    }

}
