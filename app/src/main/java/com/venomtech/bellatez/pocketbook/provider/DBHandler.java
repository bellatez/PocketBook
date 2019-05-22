package com.venomtech.bellatez.pocketbook.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    //    database related constants
    private static final int DATABASE_VERSION = 1;

//    database name
    private static final String DATABASE_NAME = "pocketbook";

//    tables in database
    protected static final String TABLE_TRANSACTION = "operations";
    protected static final String TABLE_BUDGET = "budget";

    //    transaction table columns
    protected static final String TRANSACTION_ID = "_id";
    protected static final String TRANSACTION_AMOUNT = "amount";
    protected static final String TRANSACTION_PURPOSE = "purpose";
    protected static final String BUDGET_ID_KEY = "budgetId";

    //    budget table columns
    protected static final String BUDGET_ID = "_id";
    protected static final String BUDGET_AMOUNT = "amount";
    protected static final String CREATED_AT = "created_at";


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String BUDGET_CREATE =
                "CREATE TABLE " + TABLE_BUDGET + "("
                        + BUDGET_ID + " integer primary key autoincrement, "
                        + BUDGET_AMOUNT + " integer, " +
                        CREATED_AT + " DATETIME " + ");";

        final String TRANSACTION_CREATE =
                "CREATE TABLE " + TABLE_TRANSACTION + "("
                        + TRANSACTION_ID + " integer primary key autoincrement, "
                        + TRANSACTION_AMOUNT + " integer not null, "
                        + TRANSACTION_PURPOSE + " text not null, "
                        + BUDGET_ID_KEY + "integer, "
                        + BUDGET_ID_KEY + " REFERENCES "+TABLE_BUDGET+"("+BUDGET_ID+") ON DELETE CASCADE);";


//        EXECUTING THE SQL COMMANDS TO CREATE THE VARIOUS TABLES

        db.execSQL(BUDGET_CREATE);
        db.execSQL(TRANSACTION_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);

//        CREATE NEW TABLES
        onCreate(db);
    }


    //    transaction cruds
    public long createTransaction(TransactionModel transaction){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_AMOUNT, transaction.getTRANSACTION_AMOUNT());
        values.put(TRANSACTION_PURPOSE, transaction.getTRANSACTION_PURPOSE());
        values.put(BUDGET_ID_KEY, transaction.getBUDGET_ID_KEY());

        //insert row
        long trans_id = db.insert(TABLE_TRANSACTION, null, values);
        return trans_id;

    }

    public TransactionModel getTransaction(long trans_id, long budget_id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM "+TABLE_TRANSACTION+" WHERE "+TRANSACTION_ID+ "="+trans_id+ " AND " +BUDGET_ID_KEY + "="+budget_id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        TransactionModel trans = new TransactionModel(
                cursor.getInt(cursor.getColumnIndex(TRANSACTION_ID)),
                cursor.getInt(cursor.getColumnIndex(TRANSACTION_AMOUNT)),
                cursor.getString(cursor.getColumnIndex(TRANSACTION_PURPOSE)),
                cursor.getInt(cursor.getColumnIndex(BUDGET_ID_KEY)));


        // close the db connection
        cursor.close();

        return trans;
    }

    //    retrieve all transactions
    public List<TransactionModel> getAllTransactions(long budget_id){
        List<TransactionModel> transactions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE "+ BUDGET_ID_KEY + "="+budget_id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()){
            do {

                transactions.add(new TransactionModel(
                        c.getLong(0),
                        c.getInt(1),
                        c.getString(2),
                        c.getLong(3)
                ));
            }
            while (c.moveToNext());
        }
        return transactions;
    }

    public int sumTransactions(long budget_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sumQuery = "SELECT "+ TRANSACTION_AMOUNT +" FROM " + TABLE_TRANSACTION + " WHERE "+ BUDGET_ID_KEY + "="+budget_id;
        Cursor c = db.rawQuery(sumQuery, null);
        int sum = 0;
        if(c.moveToFirst()){
            do{
               sum += c.getInt(0);
            }while (c.moveToNext());
        }
        return sum;
    }

    //    deleting a transaction
    public void deleteTransaction(long trans_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTION, TRANSACTION_ID + " =?", new String[]{String.valueOf(trans_id)});
    }

    public int getTransactionCount(long budget_id) {
        String countQuery = "SELECT  * FROM " + TABLE_TRANSACTION+ " WHERE "+BUDGET_ID_KEY+ "="+budget_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

//budget crud handler

    public long createBudget(BudgetModel budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BUDGET_AMOUNT, budget.getBUDGET_AMOUNT());
        values.put(CREATED_AT, getDateTime());

        long bud_id = db.insert(TABLE_BUDGET, null, values);
        return bud_id;
    }


    public BudgetModel getBudget(long bud_id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BUDGET,
                new String[]{BUDGET_ID, BUDGET_AMOUNT, CREATED_AT},
                BUDGET_ID + "=?",
                new String[]{String.valueOf(bud_id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        BudgetModel budget = new BudgetModel(
                cursor.getInt(cursor.getColumnIndex(BUDGET_ID)),
                cursor.getInt(cursor.getColumnIndex(BUDGET_AMOUNT)),
                cursor.getString(cursor.getColumnIndex(CREATED_AT)));

        // close the db connection
        cursor.close();

        return budget;
    }

    //    fetching all budget
    public List<BudgetModel> getAllBudget(){

        List<BudgetModel> budgets = new ArrayList<BudgetModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BUDGET;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {

                budgets.add(new BudgetModel(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(2)
                ));
            }
            while (c.moveToNext());
        }
        return budgets;
    }

    public void deleteBudget(long bud_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUDGET, BUDGET_ID + " =?", new String[] {String.valueOf(bud_id)});
    }

    public void emptyBudget(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUDGET, null,null);
    }



    //    setting the date to be returned
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int getBudgetCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BUDGET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}