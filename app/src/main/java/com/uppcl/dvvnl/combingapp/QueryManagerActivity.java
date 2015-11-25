package com.uppcl.dvvnl.combingapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class QueryManagerActivity extends AppCompatActivity {
    private Button mRunButton;
    private EditText mCreateQuery;
    private TableLayout mShowResult;
    private MasterDataSource dbSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_manager);
        mRunButton = (Button) findViewById(R.id.bt_query_run);
        mCreateQuery = (EditText) findViewById(R.id.query_create);
        mShowResult = (TableLayout) findViewById(R.id.query_result);
        dbSource = new MasterDataSource(this);

        mRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbSource.open();
                String query = mCreateQuery.getText().toString();
                try {
                    String result[][] = dbSource.runQuerty(query);

                    if (result !=null) {
                        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                        int rows = result.length;
                        int cols = result[0].length;
                        for(int i=0;i<rows;i++){
                            TableRow tableRow = new TableRow(mShowResult.getContext());
                            tableRow.setLayoutParams(tableParams);
                            for(int j=0;j<cols;j++){

                                TextView textView = new TextView(getApplicationContext());
                                textView.setText(result[i][j]);
                                textView.setLayoutParams(rowParams);
                                tableRow.addView(textView);
                            }
                            mShowResult.addView(tableRow);

                        }

                       // mShowResult.setText(result);
                    } else {
                        Toast.makeText(getApplicationContext(), "Noting was returned", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
