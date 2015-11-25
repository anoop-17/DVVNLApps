package com.uppcl.dvvnl.combingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class SelectGISActivity extends AppCompatActivity {
    public Button mPoleImportButton;
    public Button mDTImportButton;
    private MasterDataSource dbSource;

    private static final int IMPORT_POLE_CODES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gis);
        dbSource = new MasterDataSource(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPoleImportButton = (Button) findViewById(R.id.bt_import_pole_code);
        mDTImportButton = (Button) findViewById(R.id.bt_import_dt_data);

        mPoleImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                startActivityForResult(intent, IMPORT_POLE_CODES);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_POLE_CODES) {
            try {
                Uri uri = data.getData();
                String fileName;
                String scheme = uri.getScheme();
                if (scheme.equals("file")) {
                    fileName = uri.getPath();
                    if (fileName.endsWith(".csv")) {
                        new ImportPoleCode().execute(fileName);
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
        }
    }

    private class ImportPoleCode extends AsyncTask<String, Integer, String> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        public ImportPoleCode() {
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
