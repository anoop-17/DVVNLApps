package com.uppcl.dvvnl.combingapp;

import android.accounts.Account;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by DVVNL on 02-10-2015.
 * This is a container class for database.
 * A contract class is a container for constants that define names for URIs, tables, and columns.
 * The contract class allows you to use the same constants across all the other classes in the
 * same package.
 * This lets you change a column name in one place and have it propagate throughout your code.
 * A good way to organize a contract class is to put definitions that are global to your whole
 * database in the root level of the class. Then create an inner class for each table that enumerates its columns.
 */
public final class MasterContract {

    public MasterContract() {
    }
//Tables

    public static final String BILL_TABLE_NAME = "BILL_TABLE";
    public static final String METER_TABLE_NAME = "METER_TABLE";
    public static final String PAYMENT_TABLE_NAME = "PAYMENT_TABLE";
    public static final String GIS_TABLE_NAME = "GIS_TABLE";
    public static String MONTH = "SEP";
    public static String YEAR = "2015";
    //Columns
    public static String[] CSVHeader;
    public static final String COLUMN_NAME_SERIAL_NBR = "SERIAL_NBR";
    public static final String COLUMN_NAME_MULTIPLY_FACTOR = "MULTIPLY_FACTOR";
    public static final String COLUMN_NAME_METER_STATUS = "METER_STATUS";
    public static final String COLUMN_NAME_BILL_DATE = "BILL_DATE";
    public static final String COLUMN_NAME_MDI = "MDI";
    public static final String COLUMN_NAME_BILL_BASIS = "BILL_BASIS";
    public static final String COLUMN_NAME_BILL_TYP = "BILL_TYP";
    public static final String COLUMN_NAME_CONSUMPTION = "CONSUMPTION";
    public static final String COLUMN_NAME_SBM_MACHINE_ID = "SBM_MACHINE_ID";
    public static final String COLUMN_NAME_LAST_OK_READ_STATUS = "LAST_OK_READ_STATUS";
    public static final String COLUMN_NAME_LAST_OK_READING = "LAST_OK_READING";
    public static final String COLUMN_NAME_METER_READ_FLTY_CNT = "METER_READ_FLTY_CNT";
    public static final String COLUMN_NAME_PAY_DATE = "LAST_PAY_DATE";
    public static final String COLUMN_NAME_PAY_AMT = "LAST_PAY_AMT";
    public static final String COLUMN_NAME_POLE_NO = "POLE_NO";
    public static final String COLUMN_NAME_METER_READ_REMARK = "METER_READ_REMARK";
    public static final String COLUMN_NAME_INSTALLATION_DATE = "INSTALLATION_DATE";
    public static final String COLUMN_NAME_BILL_CYC_CD = "BILL_CYC_CD";
    public static final String COLUMN_NAME_MONTH = "MONTH";
    public static final String COLUMN_NAME_YEAR = "YEAR";
    public static final String COLUMN_NAME_ARREAR = "ARREAR";
    public static final String COLUMN_NAME_LPSC = "LPSC";
    public static final String COLUMN_NAME_TOTAL_OUTSTANDING = "TOTAL_OUTSTANDING";

    public static final String COLUMN_NAME_CURRENT_ASSESSMENT = "CURRENT_ASSESSMENT";


    public static final String COLUMN_ID = "_ID";
    public static final String ACCOUNT_TABLE_NAME = "ACCOUNT_TABLE";
    public static final String COLUMN_NAME_ACCT_ID = "ACCT_ID";
    public static final String COLUMN_NAME_KNO = "KNO";
    public static final String COLUMN_NAME_DIV_CODE = "DIV_CODE";
    public static final String COLUMN_NAME_MOBILE_NO = "MOBILE_NO";
    public static final String COLUMN_NAME_BOOK_NO = "BOOK_NO";
    public static final String COLUMN_NAME_SCNO = "SCNO";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_ADDRESS = "ADDRESS";
    public static final String COLUMN_NAME_SUPPLY_TYPE = "SUPPLY_TYPE";
    public static final String COLUMN_NAME_LOAD = "LOAD";
    public static final String COLUMN_NAME_LOAD_UNIT = "LOAD_UNIT";
    public static final String COLUMN_NAME_DOC = "DOC";
    public static final String COLUMN_NAME_SECURITY_AMT = "SECURITY_AMT";
    public static final String COLUMN_NAME_CON_STATUS = "CON_STATUS";


}

