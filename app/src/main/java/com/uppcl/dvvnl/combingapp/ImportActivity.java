package com.uppcl.dvvnl.combingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;

public class ImportActivity extends AppCompatActivity {
    private MasterDataSource dbSource;

    private Button mMasterButton;
    private Button mPoleButtom;
    private Button mDTButton;
    static final int SELECT_POLE_CODE = 1;
    static final int SELECT_MASTER_DATA = 2;
    static final int SELECT_GIS_DATA = 2;
    private Date latestBy;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == SELECT_MASTER_DATA) {
            try {
                Uri uri = data.getData();
                String fileName;
                String scheme = uri.getScheme();
                if (scheme.equals("file")) {
                    fileName = uri.getPath();
                    if (fileName.endsWith(".csv")) {
                        new ImportMaster().execute(fileName);
                        Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Master must be in CSV file", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), " No File Selected", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

            }


        } else {
            Log.e("Rdinot match up", "" + requestCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        dbSource = new MasterDataSource(this);
        mMasterButton =(Button)findViewById(R.id.bt_import_master);
        mPoleButtom=(Button)findViewById(R.id.bt_import_pole_code);
        mDTButton=(Button) findViewById(R.id.bt_import_dt_codes);
        mMasterButton.setVisibility(View.GONE);
        mPoleButtom.setVisibility(View.GONE);
        mDTButton.setVisibility(View.GONE);
        mMasterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                startActivityForResult(intent, SELECT_MASTER_DATA);
            }
        });

        mPoleButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mDTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    private class ImportMaster extends AsyncTask<String, Integer, String> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        public ImportMaster() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... strings) {
            importCSV(strings);
            return null;
        }
    }
    public void importCSV(String[] fileName) {
        dbSource.open();
        // File csvFile = new File("/storage/emulated/0/Download/MASTER_DVVNL_DIV211211_MAR2015.csv");
        //Build reader instance
        CSVReader reader = null;
        try {

            reader = new CSVReader(new FileReader(fileName[0]), ',', '"', 0);
            int i = 0;
            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            MasterContract.CSVHeader = reader.readNext();
            HashMap<String, String> rows = new HashMap<String, String>();

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine != null) {
                    for (int j = 0; j < nextLine.length; j++) {
                        rows.put(MasterContract.CSVHeader[j], nextLine[j]);
                    }
                    rows.put("LATEST_BY","");
                    long j = dbSource.insertAccountDetails(rows);
                    Log.e("Inserted at", "" + j);
                }
            }


        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbSource.close();
    }

}
