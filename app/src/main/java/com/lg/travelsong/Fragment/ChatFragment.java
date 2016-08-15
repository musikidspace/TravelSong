package com.lg.travelsong.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lg.travelsong.R;

/**
 * @author LuoYi on 2016/8/10
 */
public class ChatFragment extends Fragment {
    private ActionBar ab;
    private String host;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        host = getArguments().getString("host");
        ab = getActivity().getActionBar();
        ab.setTitle(R.string.chat_title);
        TextView tv = new TextView(getActivity());
        tv.setTextSize(90);
        tv.setText(host);
        return tv;
    }
}
