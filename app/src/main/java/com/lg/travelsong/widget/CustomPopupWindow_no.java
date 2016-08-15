package com.lg.travelsong.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.travelsong.R;
import com.lg.travelsong.utils.MyBitmapUtils;

import java.util.List;

/**
 * @author LuoYi on 2016/8/15
 */
public class CustomPopupWindow_no {
    private int width = 480;
    //private int height = 800;
    private Context context;
    private PopupWindow popupWindow;
    private List<ListViewNameAndId> list;

    public CustomPopupWindow_no(Context context) {
        this.context = context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
//        height = displayMetrics.heightPixels;
    }

    /**
     * 生成ListView样式的PopupWidow
     */
    public void getListViewPopupWidow(List<ListViewNameAndId> list) {
        this.list = list;
        View view = View.inflate(context, R.layout.listview_menu_no, null);
        ListView lv = (ListView) view.findViewById(R.id.lv_menu);
        lv.setAdapter(new MyAdapter());

        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(getTransparentBitmap());// 一定要在showAtLocation之前定义背景才能点外面消失
        popupWindow.setAnimationStyle(R.style.Style_PopupAnim_Right);
        popupWindow.showAtLocation(view, Gravity.RIGHT, 0, 0);
        popupWindow.setOutsideTouchable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, i + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 透明图片
     *
     * @return
     */
    private Drawable getTransparentBitmap() {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(
                context.getResources().getColor(R.color.transparent));
        return drawable;
    }

    public class ListViewNameAndId {
        public String name;
        public int id;

        public ListViewNameAndId(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }

    static class ViewHolder {
        public ImageView iv;
        public TextView tv;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyBitmapUtils myBitmapUtils = MyBitmapUtils.getInstance(context);
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(context, R.layout.item_menu_no, null);
                holder.iv = (ImageView) view.findViewById(R.id.iv_item);
                holder.tv = (TextView) view.findViewById(R.id.tv_item);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.iv.setImageBitmap(myBitmapUtils.readBitMap(((ListViewNameAndId) getItem(i)).id));
            holder.tv.setText(((ListViewNameAndId) getItem(i)).name);

            return view;
        }
    }
}
