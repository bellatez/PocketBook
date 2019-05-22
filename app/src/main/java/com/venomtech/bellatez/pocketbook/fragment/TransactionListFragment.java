package com.venomtech.bellatez.pocketbook.fragment;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.venomtech.bellatez.pocketbook.Helper;
import com.venomtech.bellatez.pocketbook.MyDividerItemDecoration;
import com.venomtech.bellatez.pocketbook.R;
import com.venomtech.bellatez.pocketbook.adapter.TransactionListAdapter;
import com.venomtech.bellatez.pocketbook.provider.DBHandler;
import com.venomtech.bellatez.pocketbook.provider.TransactionModel;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TransactionListFragment extends Fragment {

    private TransactionListAdapter adapter;
    private List<TransactionModel> transactionsList = new ArrayList<>();
    private DBHandler crud;

    TextView initialamnt;
    TextView finalamnt;
    RecyclerView recyclerView;
    EditText transactionamnt;
    EditText purpose;
    Button createbtn;
    TextView noTransactionsView;

    public TransactionListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        recyclerView = v.findViewById(R.id.listRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));


        crud = new DBHandler(getActivity());
        adapter = new TransactionListAdapter(crud.getAllTransactions(ID()), crud);
        recyclerView.setAdapter(adapter);

        transactionsList.addAll(crud.getAllTransactions(ID()));

        initialamnt = v.findViewById(R.id.initialamnt);
        finalamnt = v.findViewById(R.id.balance);
        noTransactionsView = v.findViewById(R.id.empty_transactions_view);




        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder transactionBuilder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_transaction, null);
                transactionamnt = dialogView.findViewById(R.id.transactionamnt);
                purpose = dialogView.findViewById(R.id.purpose);
                createbtn = dialogView.findViewById(R.id.createbtn);

                transactionBuilder.setView(dialogView);
                final AlertDialog dialog = transactionBuilder.create();
                dialog.show();

                createbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveTransactions();
                        dialog.dismiss();
                    }
                });
            }
        });

        setData();
        Helper.toggleView(noTransactionsView, crud, ID());

    }

    private void saveTransactions(){
        String transactionAmnt = transactionamnt.getText().toString();
        String use = purpose.getText().toString();
        if(transactionAmnt.equals("") || use.equals("")){
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.warning)
                    .setMessage(R.string.validation)
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.ok, null)
                    .show();
            Toast.makeText(getActivity(), R.string.transactionFailed, Toast.LENGTH_LONG).show();
        }else {
            int transactions = Integer.parseInt(transactionAmnt);
            if(transactions > getBudget()){
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warningMessage)
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.ok, null)
                        .show();
                Toast.makeText(getActivity(), R.string.transactionFailed, Toast.LENGTH_LONG).show();
            }
            else{
                TransactionModel transaction = new TransactionModel(transactions, use, ID());
                long id = crud.createTransaction(transaction);
                TransactionModel t = crud.getTransaction(id,ID());
                transactionsList.add(0, t);
                adapter.notifyDataSetChanged();
                setData();
                Helper.toggleView(noTransactionsView, crud, ID());
                Toast.makeText(getActivity(), R.string.transactonCreated, Toast.LENGTH_LONG).show();

            }
        }

    }



    private long ID(){
        Long bID = getArguments().getLong("budgetid");
        return bID;
    }
    private void setData(){
        String result = String.format("%,d", getBudget());
        initialamnt.setText(result+" XAF");
        calculateBalance(getBudget());
    }

    public int getBudget(){
        int amnt = Integer.parseInt(getArguments().getString("budgetamnt"));
        return amnt;
    }

    private void calculateBalance(int amnt){
        int balance = amnt - crud.sumTransactions(ID());
        String result = String.format("%,d", balance);
        finalamnt.setText(result+" XAF");

    }

}
