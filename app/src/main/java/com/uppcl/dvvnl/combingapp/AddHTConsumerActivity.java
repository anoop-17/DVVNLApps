package com.uppcl.dvvnl.combingapp;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddHTConsumerActivity extends AppCompatActivity {
    private EditText mAccountID;
    private EditText mMainMeter;
    private EditText mPoleMeter;
    private Button mButton;
    private EditText mProcess;
    private EditText mPoleMF;
    private EditText mName;
    private EditText mLoad;
    private MasterDataSource dbSource;
private EditText mMainMF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbSource = new MasterDataSource(this);

        setContentView(R.layout.activity_add_htconsumer);
        mAccountID = (EditText) findViewById(R.id.add_et_account);
        mMainMeter = (EditText) findViewById(R.id.add_et_main);
        mPoleMeter = (EditText) findViewById(R.id.add_et_pole);
        mProcess = (EditText) findViewById(R.id.add_et_process);
        mPoleMF = (EditText) findViewById(R.id.add_et_pole_mf);
        mMainMF=(EditText)findViewById(R.id.add_et_pole_mf);
        mName = (EditText) findViewById(R.id.add_et_name);
        mLoad = (EditText) findViewById(R.id.add_et_load);
        mButton = (Button) findViewById(R.id.add_bt_save);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbSource.open();
                ContentValues values = new ContentValues();
                values.put("POLE_METER", mPoleMeter.getText().toString());
                values.put("ACCOUNT_ID", mAccountID.getText().toString());
                values.put("MAIN_METER", mMainMeter.getText().toString());
                values.put("PROCESS", mProcess.getText().toString());
                values.put("NAME", mName.getText().toString());
                values.put("LOAD", mLoad.getText().toString());
                values.put("MAIN_MF",mMainMF.getText().toString());
                values.put("POLE_MF",mPoleMF.getText().toString());

                try {
                    long i = dbSource.insertMeters(values);
                    dbSource.close();
                    if (i >= 1) {
                        Toast.makeText(getApplicationContext(), "Consumer Successfully Added", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Consumer Could not be Added", Toast.LENGTH_SHORT).show();

                    }
                    Log.e("Meter Inserted at", "" + i);
                } catch (Exception e) {
                    Log.e("Select Query", "" + MasterDbHelper.TABLE_HT_READINGS);
                    Toast.makeText(getApplicationContext(), "Consumer Could not be Added", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        });
    }
}
