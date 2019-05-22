package com.venomtech.bellatez.pocketbook.provider;

public class BudgetModel {

   private long BUDGET_ID;
   private int BUDGET_AMOUNT;
   private String CREATED_AT;


    public BudgetModel (int BUDGET_AMOUNT){
        this.BUDGET_AMOUNT = BUDGET_AMOUNT;
    }

    public BudgetModel (long BUDGET_ID, int BUDGET_AMOUNT, String CREATED_AT){
        this.BUDGET_ID = BUDGET_ID;
        this.BUDGET_AMOUNT = BUDGET_AMOUNT;
        this.CREATED_AT = CREATED_AT;
    }

//    setters
    public void setBUDGET_ID(long BUDGET_ID) {
        this.BUDGET_ID = BUDGET_ID;
    }
    public void setBUDGET_AMOUNT(int BUDGET_AMOUNT) {
        this.BUDGET_AMOUNT = BUDGET_AMOUNT;
    }
    public void setCREATED_AT(String CREATED_AT) {
        this.CREATED_AT = CREATED_AT;
    }


//    getters
    public long getBUDGET_ID() {
        return BUDGET_ID;
    }
    public int getBUDGET_AMOUNT() {
        return BUDGET_AMOUNT;
    }

    public String getCREATED_AT() {
        return CREATED_AT;
    }
}