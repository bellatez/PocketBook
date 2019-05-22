package com.venomtech.bellatez.pocketbook.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.venomtech.bellatez.pocketbook.R;
import com.venomtech.bellatez.pocketbook.provider.BudgetModel;
import com.venomtech.bellatez.pocketbook.provider.DBHandler;

import java.util.List;


public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.MyViewHolder> {

    private List<BudgetModel> budgetsList;
    private DBHandler db;
    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClicked(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView budgetamnt;
        public TextView timestamp;
        public CardView cardView;

        public MyViewHolder(View v, final onItemClickListener listener){
            super(v);
            budgetamnt = v.findViewById(R.id.budgetitem);
            timestamp = v.findViewById(R.id.createDate);
            cardView = v.findViewById(R.id.card_budget);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClicked(position);
                        }
                    }

                }
            });
        }
    }

    public BudgetListAdapter(List<BudgetModel> mybudgetsList, DBHandler myDB){
        budgetsList = mybudgetsList;
        db = myDB;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_budget, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Context context = holder.budgetamnt.getContext();

        BudgetModel budgets = budgetsList.get(position);

        String amount = String.format("%,d", budgets.getBUDGET_AMOUNT());

        holder.budgetamnt.setText(amount+" xaf");
        if (db.sumTransactions(budgets.getBUDGET_ID()) == 0){
            holder.timestamp.setText(budgets.getCREATED_AT());
        } else {

            holder.timestamp.setText("-"+String.format("%,d", db.sumTransactions(budgets.getBUDGET_ID()))+" xaf");
        }
    }

    public void updateBudgetlist(List<BudgetModel> newlist){
        this.budgetsList.clear();
        this.budgetsList.addAll(newlist);
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        Log.d("budget list count", "getItemCount: "+budgetsList.size());
        if(budgetsList != null){
            return budgetsList.size();
        }
        return 0;
    }

}


