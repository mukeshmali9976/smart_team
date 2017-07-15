package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.techmali.smartteam.R;
import com.techmali.smartteam.models.IdValueModel;

import java.util.ArrayList;

public class SpinnerArrayAdapter extends ArrayAdapter<IdValueModel> {

    Context context;
    int layoutResourceId;
    ArrayList<IdValueModel> data = null;

    public SpinnerArrayAdapter(Context context, int layoutResourceId, ArrayList<IdValueModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public IdValueModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.tvSpinItem);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        IdValueModel mData = data.get(position);
        holder.txtTitle.setText(mData.getValue());

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    static class ViewHolder {
        TextView txtTitle;
    }

}