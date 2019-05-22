package com.venomtech.bellatez.pocketbook.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.venomtech.bellatez.pocketbook.Helper;
import com.venomtech.bellatez.pocketbook.MyDividerItemDecoration;
import com.venomtech.bellatez.pocketbook.R;
import com.venomtech.bellatez.pocketbook.RecyclerTouchListener;
import com.venomtech.bellatez.pocketbook.adapter.BudgetListAdapter;
import com.venomtech.bellatez.pocketbook.provider.BudgetModel;
import com.venomtech.bellatez.pocketbook.provider.DBHandler;

import java.util.ArrayList;
import java.util.List;


public class BudgetListFragment extends Fragment {

    EditText budgetamnt;
    TextView budgetitem;
    TextView noBudgetsView;
    Button createbtn;
    RecyclerView recyclerView;

    BudgetListAdapter adapter;
    private List<BudgetModel> budgetsList = new ArrayList<>();
    private DBHandler crud;

    static final String BUDGET_ID = "budgetId";
    Long budgetId;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));

        crud = new DBHandler(getActivity());
        adapter = new BudgetListAdapter(crud.getAllBudget(), crud);
        recyclerView.setAdapter(adapter);

        budgetsList.addAll(crud.getAllBudget());

        budgetitem = v.findViewById(R.id.budgetitem);
        noBudgetsView = v.findViewById(R.id.empty_budgets_view);

        Helper.toggleViewBudget(noBudgetsView,crud);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder budgetBuilder = new AlertDialog.Builder(getActivity());

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_budget, null);
            budgetamnt = dialogView.findViewById(R.id.budgetamnt);
            createbtn = dialogView.findViewById(R.id.createbtn);

            budgetBuilder.setView(dialogView);
            final AlertDialog dialog = budgetBuilder.create();
            dialog.show();

            createbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createBudget();
                    dialog.dismiss();
                }
            });
            }

        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String amnt = String.valueOf(budgetsList.get(position).getBUDGET_AMOUNT());
                long ID = Long.valueOf(budgetsList.get(position).getBUDGET_ID());

                openFragment(amnt, ID);
            }

            @Override
            public void onLongClick(View view, final int position) {
                new AlertDialog.Builder(getActivity())
                .setTitle(R.string.deleteRequest)
                .setMessage("Budget will be permanently deleted")
                .setCancelable(true)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(position);
                        Toast.makeText(getActivity(), "Budget has been deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
            }
        }));
    }

    private void openFragment(String amnt, long ID) {
        Fragment fragment = new TransactionListFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle args = new Bundle();
        args.putString("budgetamnt", amnt);
        args.putLong("budgetid", ID);
        fragment.setArguments(args);

        fragmentTransaction.replace(R.id.fragment_holder, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void createBudget(){
        String budgetData = budgetamnt.getText().toString();
        if(budgetData.equals("")){
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.warning)
                    .setMessage(R.string.validation)
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.ok, null)
                    .show();
            Toast.makeText(getActivity(), R.string.budgetFailed, Toast.LENGTH_LONG).show();
        } else{
            int _budget = Integer.parseInt(budgetData);
            BudgetModel budget = new BudgetModel(_budget);
            long id = crud.createBudget(budget);

            BudgetModel insertedItem = crud.getBudget(id);
            budgetsList.add(0,insertedItem);
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), R.string.createdbudget, Toast.LENGTH_LONG).show();
            openFragment(budgetData, id);

        }
    }

    private void deleteItem(int i) {
        crud.deleteBudget(budgetsList.get(i).getBUDGET_ID());
        budgetsList.remove(i);
        adapter.notifyItemRemoved(i);
        Helper.toggleViewBudget(noBudgetsView,crud);

    }

}


