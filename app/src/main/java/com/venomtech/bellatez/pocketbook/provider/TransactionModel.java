package com.venomtech.bellatez.pocketbook.provider;

public class TransactionModel {

    private long TRANSACTION_ID;
    private int TRANSACTION_AMOUNT;
    private String TRANSACTION_PURPOSE;
    private long BUDGET_ID_KEY;


    public TransactionModel(int TRANSACTION_AMOUNT, String TRANSACTION_PURPOSE, long BUDGET_ID_KEY){
        this.TRANSACTION_AMOUNT = TRANSACTION_AMOUNT;
        this.TRANSACTION_PURPOSE = TRANSACTION_PURPOSE;
        this.BUDGET_ID_KEY = BUDGET_ID_KEY;
    }

    public TransactionModel(long TRANSACTION_ID, int TRANSACTION_AMOUNT, String TRANSACTION_PURPOSE, long BUDGET_ID_KEY){
        this.TRANSACTION_ID = TRANSACTION_ID;
        this.TRANSACTION_AMOUNT = TRANSACTION_AMOUNT;
        this.TRANSACTION_PURPOSE = TRANSACTION_PURPOSE;
        this.BUDGET_ID_KEY = BUDGET_ID_KEY;

    }

//    GETTERS
    public long getTRANSACTION_ID() {
    return TRANSACTION_ID;
}
    public long getBUDGET_ID_KEY() {
    return BUDGET_ID_KEY;
}
    public long getTRANSACTION_AMOUNT() {
        return TRANSACTION_AMOUNT;
    }
    public String getTRANSACTION_PURPOSE() {
        return TRANSACTION_PURPOSE;
    }


    //SETTERS

    public void setTRANSACTION_ID(long TRANSACTION_ID) {
        this.TRANSACTION_ID = TRANSACTION_ID;
    }
    public void setTRANSACTION_AMOUNT(int TRANSACTION_AMOUNT) {
        this.TRANSACTION_AMOUNT = TRANSACTION_AMOUNT;
    }
    public void setTRANSACTION_PURPOSE(String TRANSACTION_PURPOSE) {
        this.TRANSACTION_PURPOSE = TRANSACTION_PURPOSE;
    }

}
