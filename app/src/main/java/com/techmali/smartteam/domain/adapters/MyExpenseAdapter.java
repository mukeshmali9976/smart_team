package com.techmali.smartteam.domain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techmali.smartteam.R;
import com.techmali.smartteam.models.ExpenseModel;

import java.util.List;


/**
 * Created by mukesh mali on 5/31/2017.
 */

public class MyExpenseAdapter extends RecyclerView.Adapter<MyExpenseAdapter.ViewHolder> {

    private Context context;
    private List<ExpenseModel> myExpenseList;

    public MyExpenseAdapter(Context context, List<ExpenseModel> myExpenseList) {
        this.context = context;
        this.myExpenseList = myExpenseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.row_expense, parent, false);
        return new ViewHolder(itemLayout);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return myExpenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
