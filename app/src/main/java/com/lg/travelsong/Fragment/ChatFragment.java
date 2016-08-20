package com.lg.travelsong.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.travelsong.R;
import com.lg.travelsong.activity.ContactActivity;
import com.lg.travelsong.bean.Conversation;
import com.lg.travelsong.bean.ResIdAndName;
import com.lg.travelsong.dao.ConversationDao;
import com.lg.travelsong.manager.ThreadPool;
import com.lg.travelsong.utils.MyBitmapUtils;
import com.lg.travelsong.utils.MyStringUtils;
import com.lg.travelsong.widget.CustomPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoYi on 2016/8/10
 */
public class ChatFragment extends Fragment {

    private static final int MSG_QUERY_FIRST_SUCCESS = 100;
    private Intent intent;
    private ActionBar ab;
    private CustomPopupWindow popupWindow;
    private View view_for_pop;
    private ListView lv_chat;
    private List<Conversation> listCon;
    private int offset = 0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_QUERY_FIRST_SUCCESS:
                    lv_chat.setAdapter(new ConversationAdapter());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                break;
            // TODO: 2016/8/20 图标
            case R.id.contact:
                intent = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                popupWindow.showPopupWindow(view_for_pop);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(View view) {
        ab = getActivity().getActionBar();
        view_for_pop = view.findViewById(R.id.view_for_pop);
        lv_chat = (ListView) view.findViewById(R.id.lv_chat);
    }

    private void initData() {
        //查询会话列表数据
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                listCon = new ConversationDao(getActivity()).queryLimit(20, 0);
                Message msg = mHandler.obtainMessage();
                msg.what = MSG_QUERY_FIRST_SUCCESS;
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
        popupWindow = new CustomPopupWindow(getActivity(), list, new CustomPopupWindow.OnItemClickListener() {
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
                    case 4:
                        break;
                }
            }
        });
        ab.setTitle(R.string.chat_title);
    }

    class ConversationAdapter extends BaseAdapter{

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
            MyBitmapUtils myBitmapUtils = MyBitmapUtils.getInstance(getActivity());
            ViewHolder holder;
            if (view == null){
                holder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.item_chat, null);
                holder.iv_head = (ImageView) view.findViewById(R.id.iv_head);
                holder.v_count = view.findViewById(R.id.v_count);
                holder.tv_showname = (TextView) view.findViewById(R.id.tv_showname);
                holder.tv_lasttime = (TextView) view.findViewById(R.id.tv_lasttime);
                holder.tv_lastmsg = (TextView) view.findViewById(R.id.tv_lastmsg);
                holder.iv_mute = (ImageView) view.findViewById(R.id.iv_mute);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Bitmap bm = myBitmapUtils.readBitmapFromUrl((listCon.get(i).userhead));
            if (bm != null){
                holder.iv_head.setImageBitmap(bm);
            }
//            holder.v_count.setCount(listCon.get(i).unreadcount);
            String showname = listCon.get(i).nickname != null ? listCon.get(i).nickname
                    : (listCon.get(i).username != null ? listCon.get(i).username : listCon.get(i).usercode);
            holder.tv_showname.setText(showname);
            holder.tv_lasttime.setText(MyStringUtils.unix_timestamp2TimeOrDate(listCon.get(i).lasttime));
            holder.tv_lastmsg.setText(listCon.get(i).lastmsg);
            holder.iv_mute.setVisibility(listCon.get(i).mute == 0 ? View.INVISIBLE : View.VISIBLE);
            return view;
        }
    }

    static class ViewHolder{
        public ImageView iv_head;
        public View v_count;
        public TextView tv_showname;
        public TextView tv_lasttime;
        public TextView tv_lastmsg;
        public ImageView iv_mute;
    }
}
