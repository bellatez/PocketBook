package com.venomtech.bellatez.pocketbook;

import android.view.View;
import android.widget.TextView;

import com.venomtech.bellatez.pocketbook.provider.DBHandler;

public class Helper {

    public static void toggleView(TextView view, DBHandler crud, long id) {
        // you can check budgetList.size() > 0

        if (crud.getTransactionCount(id) > 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void toggleViewBudget(TextView view, DBHandler crud) {
        // you can check budgetList.size() > 0

        if (crud.getBudgetCount() > 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

}
