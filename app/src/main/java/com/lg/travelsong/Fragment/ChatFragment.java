package com.lg.travelsong.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.lg.travelsong.R;
import com.lg.travelsong.bean.ResIdAndName;
import com.lg.travelsong.widget.CustomPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LuoYi on 2016/8/10
 */
public class ChatFragment extends Fragment {
    private ActionBar ab;
    private CustomPopupWindow popupWindow;
    private View view_for_pop;
    private ListView lv_chat;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
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
}
