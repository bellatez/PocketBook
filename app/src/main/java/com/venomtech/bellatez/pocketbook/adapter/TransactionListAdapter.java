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
import com.venomtech.bellatez.pocketbook.provider.DBHandler;
import com.venomtech.bellatez.pocketbook.provider.TransactionModel;

import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {
    private List<TransactionModel> transactionList;
    private DBHandler db;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemamnt;
        public TextView purpose;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            itemamnt = v.findViewById(R.id.transactionamnt);
            purpose= v.findViewById(R.id.purpose);
            cardView = v.findViewById(R.id.card_transaction);
        }
    }


    public TransactionListAdapter(List<TransactionModel> myTransactionList, DBHandler myDB){
        transactionList = myTransactionList;
        db = myDB;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transaction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Context context = holder.itemamnt.getContext();

        TransactionModel transacs = transactionList.get(position);

        holder.purpose.setText(transacs.getTRANSACTION_PURPOSE());
        String amnt = String.format("%,d", transacs.getTRANSACTION_AMOUNT());
        holder.itemamnt.setText(amnt+" xaf");

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.deleteRequest)
                        .setMessage(holder.itemamnt.getText())
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(position);
                                Toast.makeText(context, "Budget has been deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    private void deleteItem(int i) {
        db.deleteTransaction(transactionList.get(i).getTRANSACTION_ID());
        transactionList.remove(i);
        notifyItemRemoved(i);

    }

    @Override
    public int getItemCount() {
        if(transactionList != null){
            return transactionList.size();
        }
        return 0;
    }
}
