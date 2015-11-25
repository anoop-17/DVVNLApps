package com.uppcl.dvvnl.combingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.*;

/**
 * Created by DVVNL on 02-10-2015.
 */
public class MasterDataSource {
    private SQLiteDatabase database;
    private MasterDbHelper dbHelper;
    private String queryOutput[][];


    public MasterDataSource(Context context) {
        dbHelper = new MasterDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public String[][] getAccountDetailsfromDB(int i) {

        String quesry = "SELECT * FROM " + MasterContract.ACCOUNT_TABLE_NAME + " WHERE _ID = '" + i + "'";
        Log.e("Query", "" + quesry);
        Cursor cursor = database.rawQuery(quesry, null);
        Log.e("NO. of rows returned", "" + cursor.getCount());
        String details[][] = new String[cursor.getCount()][cursor.getColumnCount()];
        cursor.moveToFirst();
        try {
            int k = 0;
            while (!cursor.isAfterLast()) {
                Log.e("ROw No.", "" + k);
                for (int j = 0; j < cursor.getColumnCount() - 1; j++) {
                    Log.e("Value at row" + k, "" + cursor.getString(j));
                    details[k][j] = cursor.getString(j + 1);
                }
                k++;
                cursor.moveToNext();
            }


            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
            Log.e("Exception--------", "" + e.getMessage());
        }


        return details;
    }

    public AccountDetails cursorToAccountDetails(Cursor cursor) {
        return null;
    }

    public ContentValues getMeterReadingByTimeStamp(String timestamp) {
        ContentValues values = new ContentValues();
        String query = "SELECT * FROM " + MasterDbHelper.TABLE_HT_READINGS + " WHERE TIMESTAMP ='" +
                timestamp + "' ";

        Log.e("Previous Que", "" + query);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                values.put(cursor.getColumnName(i), cursor.getString(i));
            }
        }
        return values;
    }

    public ContentValues getPreviousMeterReading(String meter) {
        ContentValues values = new ContentValues();
        String query = "SELECT * FROM " + MasterDbHelper.TABLE_HT_READINGS + " WHERE " + MasterDbHelper.COLUMN_NAME_METER_NO + " ='" +
                meter + "' ORDER BY READING_DATE DESC LIMIT 1";

        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            values = new ContentValues();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                values.put(cursor.getColumnName(i), cursor.getString(i));
            }
        }
        return values;
    }

    public ArrayList<ContentValues> getAllPreviousMeterReading(String meter) {
        ArrayList<ContentValues> values = new ArrayList<ContentValues>();
        String query = "SELECT * FROM " + MasterDbHelper.TABLE_HT_READINGS + " WHERE " + MasterDbHelper.COLUMN_NAME_METER_NO + " ='" +
                meter + "' ORDER BY READING_DATE DESC ";

        Log.e("Previous Que", "" + query);
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                int rowCount = 0;
                do {
                    ContentValues value = new ContentValues();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        value.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    values.add(rowCount++, value);

                } while (cursor.moveToNext());
            }

            cursor.moveToFirst();


        }
        return values;
    }

    public long insertMeterReadings(ContentValues values) {
        long insertId = -1;

        try {
            insertId = database.insert(MasterDbHelper.TABLE_HT_READINGS, null, values);
        } catch (Exception e) {
            //  Log.e("",MasterDbHelper.TABLE_HT_READINGS);
            e.printStackTrace();
        }
        return insertId;
    }

    public long insertMeters(ContentValues values) {
        long insertId = 0;
        try {

            insertId = database.insert("HT_METERS", null,
                    values);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e.getMessage());
        }
        return insertId;
    }

    public ArrayList<String> getPreviousReadingDates(String meter) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("SELECT READINGS");
        String query = "SELECT READING_DATE FROM HT_READINGS WHERE METER_NO='" + meter + "'";
        Log.e("previousreading", "" + query);
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;

    }

    public ContentValues getConsumerDetailsByMeterNo(String meterNo, int position) {
        ContentValues values = new ContentValues();
        String meterType[] = {"MAIN_METER", "POLE_METER"};
        String query = "SELECT * FROM HT_METERS WHERE " + meterType[position] + " ='" + meterNo + "'";
        Cursor cursor = database.rawQuery(query, null);
        boolean b = cursor.moveToFirst();
        try {
            if (b) {
                values.put("ACCOUNT_ID", cursor.getString(1));
                values.put("MAIN_MF", cursor.getString(5));
                values.put("PROCESS", cursor.getString(4));
                values.put("MAIN_METER", cursor.getString(2));
                values.put("POLE_METER", cursor.getString(3));
                values.put("LOAD", cursor.getString(7));
                values.put("NAME", cursor.getString(6));
                values.put("POLE_MF", cursor.getString(8));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    public int updateReadings(String rowID, ContentValues values) {
        int result = database.update(MasterDbHelper.TABLE_HT_READINGS, values, "_ID=?", new String[]{rowID});
        return result;
    }

    public ArrayList<String> getMeterList(String account_Id) {

        String query = "SELECT * FROM HT_METERS WHERE ACCOUNT_ID ='" + account_Id + "'";
        Log.e("Querry", "" + query);
        Cursor cursor = database.rawQuery(query, null);
        boolean b = cursor.moveToFirst();
        ArrayList<String> list = new ArrayList<String>();
        if (b) {
            list.add(0, cursor.getString(2));
            list.add(1, cursor.getString(3));


        }


        return list;
    }

    public long insertAccountDetails(HashMap<String, String> details) {
        ContentValues values = new ContentValues();
        values.put(MasterContract.COLUMN_NAME_ACCT_ID, details.get(MasterContract.COLUMN_NAME_ACCT_ID));
        values.put(MasterContract.COLUMN_NAME_DIV_CODE, details.get(MasterContract.COLUMN_NAME_DIV_CODE));
        values.put(MasterContract.COLUMN_NAME_MOBILE_NO, details.get(MasterContract.COLUMN_NAME_MOBILE_NO));
        values.put(MasterContract.COLUMN_NAME_KNO, details.get(MasterContract.COLUMN_NAME_KNO));
        values.put(MasterContract.COLUMN_NAME_BOOK_NO, details.get(MasterContract.COLUMN_NAME_BOOK_NO));
        values.put(MasterContract.COLUMN_NAME_SCNO, details.get(MasterContract.COLUMN_NAME_SCNO));
        values.put(MasterContract.COLUMN_NAME_NAME, details.get(MasterContract.COLUMN_NAME_NAME));
        values.put(MasterContract.COLUMN_NAME_ADDRESS, details.get(MasterContract.COLUMN_NAME_ADDRESS));
        values.put(MasterContract.COLUMN_NAME_SUPPLY_TYPE, details.get(MasterContract.COLUMN_NAME_SUPPLY_TYPE));
        values.put(MasterContract.COLUMN_NAME_LOAD, details.get(MasterContract.COLUMN_NAME_LOAD));
        values.put(MasterContract.COLUMN_NAME_LOAD_UNIT, details.get(MasterContract.COLUMN_NAME_LOAD_UNIT));
        values.put(MasterContract.COLUMN_NAME_DOC, details.get(MasterContract.COLUMN_NAME_DOC));
        values.put(MasterContract.COLUMN_NAME_SECURITY_AMT, details.get(MasterContract.COLUMN_NAME_SECURITY_AMT));

        values.put(MasterContract.COLUMN_NAME_TOTAL_OUTSTANDING, details.get(MasterContract.COLUMN_NAME_TOTAL_OUTSTANDING));
        values.put("LATEST_BY", "");

        long insertId = database.insert(MasterContract.ACCOUNT_TABLE_NAME, null,
                values);
        values = new ContentValues();
        values.put(MasterContract.COLUMN_NAME_ACCT_ID, details.get(MasterContract.COLUMN_NAME_ACCT_ID));
        values.put(MasterContract.COLUMN_NAME_PAY_DATE, details.get(MasterContract.COLUMN_NAME_PAY_DATE));
        values.put(MasterContract.COLUMN_NAME_PAY_AMT, details.get(MasterContract.COLUMN_NAME_PAY_AMT));
        insertId = database.insert(MasterContract.PAYMENT_TABLE_NAME, null,
                values);

        values = new ContentValues();
        values.put(MasterContract.COLUMN_NAME_ACCT_ID, details.get(MasterContract.COLUMN_NAME_ACCT_ID));
        values.put(MasterContract.COLUMN_NAME_MONTH, MasterContract.MONTH);
        values.put(MasterContract.COLUMN_NAME_YEAR, MasterContract.YEAR);
        values.put(MasterContract.COLUMN_NAME_SERIAL_NBR, MasterContract.COLUMN_NAME_SERIAL_NBR);
        values.put(MasterContract.COLUMN_NAME_TOTAL_OUTSTANDING, details.get(MasterContract.COLUMN_NAME_TOTAL_OUTSTANDING));


        return insertId;
    }

    public void updateAccount() {

    }

    public String[][] runQuerty(String query) {
        Cursor cursor = database.rawQuery(query, null);
        //   String result = new String("");
        if (cursor != null) {
            // move cursor to first row
            String columns[] = cursor.getColumnNames();
            int rows = cursor.getCount();
            String[][] result = new String[rows + 1][columns.length];


            result[0] = columns;
            int rowCounter = 1;
            if (cursor.moveToFirst()) {
                do {
                    //  // Get version from Cursor
                    //String bookName = cursor.getString(cursor.getColumnIndex("bookTitle"));
                    for (int i = 0; i < columns.length; i++) {
                        result[rowCounter][i] = cursor.getString(i);
                    }
                    // add the bookName into the bookTitles ArrayList
                    rowCounter++;
                    // move to next row
                } while (cursor.moveToNext());
            }
            queryOutput = result;
        }

        return queryOutput;
    }

    public class AccountDetails {

        private String name;
        private String account_id;
        private String kno;
        private String div;
        private String mobile;
        private String book;
        private String add;
        private String sc;
        private String St;
        private String load;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAccount_id() {
            return account_id;
        }

        public void setAccount_id(String account_id) {
            this.account_id = account_id;
        }

        public String getKno() {
            return kno;
        }

        public void setKno(String kno) {
            this.kno = kno;
        }

        public String getDiv() {
            return div;
        }

        public void setDiv(String div) {
            this.div = div;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public String getAdd() {
            return add;
        }

        public void setAdd(String add) {
            this.add = add;
        }

        public String getSc() {
            return sc;
        }

        public void setSc(String sc) {
            this.sc = sc;
        }

        public String getSt() {
            return St;
        }

        public void setSt(String st) {
            St = st;
        }

        public String getLoad() {
            return load;
        }

        public void setLoad(String load) {
            this.load = load;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDoc() {
            return doc;
        }

        public void setDoc(String doc) {
            this.doc = doc;
        }

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String unit;
        private String doc;
        private String amt;
        private String status;
    }

}
