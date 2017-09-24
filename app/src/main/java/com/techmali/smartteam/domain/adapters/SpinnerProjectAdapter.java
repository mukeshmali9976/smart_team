package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techmali.smartteam.R;
import com.techmali.smartteam.models.IdValueModel;
import com.techmali.smartteam.models.SyncProject;

import java.util.ArrayList;

public class SpinnerProjectAdapter extends ArrayAdapter<SyncProject> {

    Context context;
    int layoutResourceId;
    ArrayList<SyncProject> data = null;

    public SpinnerProjectAdapter(Context context, int layoutResourceId, ArrayList<SyncProject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public SyncProject getItem(int position) {
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

        SyncProject mData = data.get(position);
        holder.txtTitle.setText(mData.getTitle());

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    static class ViewHolder {
        TextView txtTitle;
    }

}