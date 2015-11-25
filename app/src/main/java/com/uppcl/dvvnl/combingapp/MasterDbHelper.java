package com.uppcl.dvvnl.combingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DVVNL on 02-10-2015.
 */
public class MasterDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.

    private final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MasterContract.ACCOUNT_TABLE_NAME + "(" + MasterContract.COLUMN_ID + "  integer primary key autoincrement not null, " +
            MasterContract.COLUMN_NAME_ACCT_ID + " VARCHAR(10) UNIQUE ,  " + MasterContract.COLUMN_NAME_DIV_CODE + "  VARCHAR(9), " +
            MasterContract.COLUMN_NAME_MOBILE_NO + " VARCHAR(10), " + MasterContract.COLUMN_NAME_KNO + " VARCHAR(10), " +
            MasterContract.COLUMN_NAME_BOOK_NO + " VARCHAR(12), " + MasterContract.COLUMN_NAME_SCNO + " VARCHAR(10), " +
            MasterContract.COLUMN_NAME_NAME + " VARCHAR(255), " + MasterContract.COLUMN_NAME_ADDRESS + " VARCHAR(255), " +
            MasterContract.COLUMN_NAME_SUPPLY_TYPE + " VARCHAR(3), " + MasterContract.COLUMN_NAME_LOAD + " REAL, " +
            MasterContract.COLUMN_NAME_LOAD_UNIT + " VARCHAR(3)  ,   " + MasterContract.COLUMN_NAME_DOC + " DATE, " +
            MasterContract.COLUMN_NAME_SECURITY_AMT + " REAL ," + MasterContract.COLUMN_NAME_CON_STATUS + " VARCHAR(20)," +
            MasterContract.COLUMN_NAME_TOTAL_OUTSTANDING + " REAL, LATEST_BY DATE)";

    private final String SQL_CREATE_BILL_TABLE = " CREATE TABLE " + MasterContract.BILL_TABLE_NAME + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + MasterContract.COLUMN_NAME_ACCT_ID + " VARCHAR(10), "
            + MasterContract.COLUMN_NAME_SERIAL_NBR + " VARCHAR(14), " + MasterContract.COLUMN_NAME_CURRENT_ASSESSMENT + " DOUBLE," +
            MasterContract.COLUMN_NAME_LPSC + " DOUBLE, " + MasterContract.COLUMN_NAME_ARREAR + " DOUBLE, " +
            MasterContract.COLUMN_NAME_BILL_DATE + " DATE, " + MasterContract.COLUMN_NAME_CONSUMPTION + " DOUBLE,  " +
            MasterContract.COLUMN_NAME_LAST_OK_READING + "  DOUBLE, " + MasterContract.COLUMN_NAME_LAST_OK_READ_STATUS + " DATE, " +
            MasterContract.COLUMN_NAME_METER_READ_REMARK + " VARCHAR(3))";
    private final String SQL_CREATE_PAYMENT_DETAILS = "CREATE TABLE " + MasterContract.PAYMENT_TABLE_NAME + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            MasterContract.COLUMN_NAME_ACCT_ID + " VARCHAR(10), " + MasterContract.COLUMN_NAME_PAY_AMT + " REAL, " +
            MasterContract.COLUMN_NAME_PAY_DATE + " DATE)";
    private final String SQL_CREATE_POLE_TABLE = "CREATE TABLE POLE ( _ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, GISID VARCHAR(16), DTGISID VARCHAR(16), POLE_TYPE VARCHAR(16)," +
            " POLE_STRUCTURE VARCHAR(16), IS_COMPOSITE VARCHAR(1), PAINTCODE VARCHAR(255))";
    private final String SQL_CREATE_DT_TABLE = "CREATE TABLE DISTRIBUTION_TRANSFORMER( _ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, GISID VARCHAR(16), FEEDER_NAME VARCHAR(255)," +
            " SUBSTATION_NAME VARCHAR(255), DIVISION VARCHAR(5),LOCATION VARCHAR(255), CAPACITY(3), MF VARCHAR(3), METER_NO VARCHAR(16), MODEM_NO VARCHAR(8), PAINT_CODE VARCHAR(255)) ";

    public static final String COLUMN_NAME_READING_DATE = "READING_DATE";
    public static final String COLUMN_NAME_METER_NO = "METER_NO";
    public static final String COLUMN_NAME_KWH_IMPORT = "KWH_IMPORT";
    public static final String COLUMN_NAME_KWH_EXPORT = "KWH_EXPORT";
    public static final String COLUMN_NAME_KVARH_IMPORT = "KVARH_IMPORT";
    public static final String COLUMN_NAME_KVARH_EXPORT = "KVARH_EXPORT";
    public static final String COLUMN_NAME_KVAH_IMPORT = "KVA_IMPORT";
    public static final String COLUMN_NAME_KVAH_EXPORT = "KVA_EXPORT";
    public static final String COLUMN_NAME_UC_IMPORT = "UC_IMPORT";
    public static final String COLUMN_NAME_UC_EXPORT = "UC_EXPORT";
    public static final String COLUMN_NAME_UHI_IMPORT = "UHI_IMPORT";
    public static final String COLUMN_NAME_UHI_EXPORT = "UHI_EXPORT";
    public static final String COLUMN_NAME_CMD_IMPORT = "CMD_IMPORT";
    public static final String COLUMN_NAME_CMD_EXPORT = "CMD_EXPORT";
    public static final String COLUMN_NAME_PF = "PF";
    public static final String COLUMN_NAME_BILLS = "BILLS";
    public static final String COLUMN_NAME_VOLTAGE_R = "VOLTAGE_R";
    public static final String COLUMN_NAME_VOLTAGE_Y = "VOLTAGE_Y";
    public static final String COLUMN_NAME_VOLTAGE_B = "VOLTAGE_B";
    public static final String COLUMN_NAME_CURRENT_R = "CURRENT_R";
    public static final String COLUMN_NAME_CURRENT_Y = "CURRENT_Y";
    public static final String COLUMN_NAME_CURRENT_B = "CURRENT_B";
    public static final String COLUMN_NAME_KWH_SEGMENT_1 = "KWH_SEGMENT_1";
    public static final String COLUMN_NAME_KWH_SEGMENT_2 = "KWH_SEGMENT_2";
    public static final String COLUMN_NAME_KWH_SEGMENT_3 = "KWH_SEGMENT_3";
    public static final String COLUMN_NAME_KWH_SEGMENT_4 = "KWH_SEGMENT_4";
    public static final String COLUMN_NAME_KWH_SEGMENT_5 = "KWH_SEGMENT_5";
    public static final String COLUMN_NAME_KWH_SEGMENT_6 = "KWH_SEGMENT_6";
    public static final String COLUMN_NAME_KWH_SEGMENT_7 = "KWH_SEGMENT_7";
    public static final String COLUMN_NAME_KWH_SEGMENT_8 = "KWH_SEGMENT_8";
    public static final String COLUMN_NAME_KVAH_SEGMENT_1 = "KVAH_SEGMENT_1";
    public static final String COLUMN_NAME_KVAH_SEGMENT_2 = "KVAH_SEGMENT_2";
    public static final String COLUMN_NAME_KVAH_SEGMENT_3 = "KVAH_SEGMENT_3";
    public static final String COLUMN_NAME_KVAH_SEGMENT_4 = "KVAH_SEGMENT_4";
    public static final String COLUMN_NAME_KVAH_SEGMENT_5 = "KVAH_SEGMENT_5";
    public static final String COLUMN_NAME_KVAH_SEGMENT_6 = "KVAH_SEGMENT_6";
    public static final String COLUMN_NAME_KVAH_SEGMENT_7 = "KVAH_SEGMENT_7";
    public static final String COLUMN_NAME_KVAH_SEGMENT_8 = "KVAH_SEGMENT_8";
public static  String TABLE_HT_READINGS="HT_READINGS";

    private static String HT_METERS = "CREATE TABLE HT_METERS ( _ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ACCOUNT_ID VARCHAR(10), MAIN_METER VARCHAR(8), POLE_METER VARCHAR(8), " +
            "PROCESS VARCHAR(32), MAIN_MF VARCHAR(3), NAME VARCHAR(255), LOAD VARCHAR(3), POLE_MF VARCHAR(3) ) ";

    public  static String CREATE_QUERY_HT_READINGS = "CREATE TABLE "+TABLE_HT_READINGS+" ( _ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_NAME_READING_DATE + " INTEGER UNIQUE,  " +
            COLUMN_NAME_METER_NO + " VARCHAR(8),   " + COLUMN_NAME_KWH_IMPORT + "  VARCHAR(10), " + COLUMN_NAME_KWH_EXPORT + " VARCHAR(10), " +
            " " + COLUMN_NAME_KVARH_IMPORT + " VARCHAR(10), " + COLUMN_NAME_KVARH_EXPORT + " VARCHAR(10), " + COLUMN_NAME_KVAH_IMPORT + " VARCHAR(10)," +
            " " + COLUMN_NAME_KVAH_EXPORT + " VARCHAR(10), " + COLUMN_NAME_UC_IMPORT + " VARCHAR(10), " + COLUMN_NAME_UC_EXPORT + " VARCHAR(10), " +
            " " + COLUMN_NAME_UHI_IMPORT + " VARCHAR(10), " + COLUMN_NAME_UHI_EXPORT + " VARCHAR(10), " + COLUMN_NAME_CMD_IMPORT + " VARCHAR(10), " + COLUMN_NAME_CMD_EXPORT +
            " VARCHAR(10), " + COLUMN_NAME_BILLS + "  VARCHAR(10) , " + COLUMN_NAME_PF + " VARCHAR(3), " + COLUMN_NAME_VOLTAGE_R + " VARCHAR(10), " + COLUMN_NAME_VOLTAGE_Y + " VARCHAR(10), " +
            " " + COLUMN_NAME_VOLTAGE_B + " VARCHAR(10),  " + COLUMN_NAME_CURRENT_R + " VARCHAR(10), " + COLUMN_NAME_CURRENT_Y + " VARCHAR(10), " + COLUMN_NAME_CURRENT_B +
            " VARCHAR(10), " + COLUMN_NAME_KWH_SEGMENT_1 + " VARCHAR(10),  " + COLUMN_NAME_KWH_SEGMENT_2 + " VARCHAR(10),  " + COLUMN_NAME_KWH_SEGMENT_3 + " VARCHAR(10), " +
            " " + COLUMN_NAME_KWH_SEGMENT_4 + " VARCHAR(10),  " + COLUMN_NAME_KWH_SEGMENT_5 + " VARCHAR(10),  " + COLUMN_NAME_KWH_SEGMENT_6 + " VARCHAR(10),  " +
            COLUMN_NAME_KWH_SEGMENT_7 + " VARCHAR(10),  " + COLUMN_NAME_KWH_SEGMENT_8 + " VARCHAR(10), " + COLUMN_NAME_KVAH_SEGMENT_1 + " VARCHAR(10), " +
            " " + COLUMN_NAME_KVAH_SEGMENT_2 + " VARCHAR(10), " + COLUMN_NAME_KVAH_SEGMENT_3 + " VARCHAR(10), " + COLUMN_NAME_KVAH_SEGMENT_4 + " VARCHAR(10), " +
            COLUMN_NAME_KVAH_SEGMENT_5 + " VARCHAR(10), " + COLUMN_NAME_KVAH_SEGMENT_6 + " VARCHAR(10), " + COLUMN_NAME_KVAH_SEGMENT_7 + " VARCHAR(10), " +
            " " + COLUMN_NAME_KVAH_SEGMENT_8 + " VARCHAR(10), SEAL VARCHAR(50), MONTH VARCHAR(10), TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MasterDatabase.db";

    public MasterDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MasterDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Log.e("select Query",""+SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


       /* db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_BILL_TABLE);
       // db.execSQL(SQL_CREATE_DT_TABLE);
        db.execSQL(SQL_CREATE_PAYMENT_DETAILS);
      //  db.execSQL(SQL_CREATE_POLE_TABLE);*/

        db.execSQL(CREATE_QUERY_HT_READINGS);
        db.execSQL(HT_METERS);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }
}