package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.models.MenuData;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter {

    private final ArrayList<MenuData> list;
    private final Context mCtx;
    private final LayoutInflater mLayoutInflater;

    public MenuAdapter(Context context, ArrayList<MenuData> list) {
        this.mCtx = context;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(mCtx);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MenuData getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.row_menu, viewGroup, false);
            holder.tvMainTitle = (TextView) convertView.findViewById(R.id.tvMainTitle);
            holder.tvMainSubTitle = (TextView) convertView.findViewById(R.id.tvMainSubTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MenuData data = getItem(i);

        holder.tvMainSubTitle.setText(data.getMenuSubName());
        holder.tvMainTitle.setText(data.getMenuName());
        return convertView;
    }

    private class ViewHolder {
        private TextView tvMainTitle, tvMainSubTitle;
    }

}
