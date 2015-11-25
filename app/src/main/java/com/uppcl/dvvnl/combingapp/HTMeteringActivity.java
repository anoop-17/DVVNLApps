package com.uppcl.dvvnl.combingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HTMeteringActivity extends AppCompatActivity {
    private EditText mDate;
    private EditText mTime;
    private EditText mKWHImport;
    private EditText mKWHExport;
    private EditText mKVARHImport;
    private EditText mKVARHExport;
    private EditText mKVAHImport;
    private EditText mKVAHExport;
    private EditText mUCImport;
    private EditText mUCExport;
    private EditText mUHIImport;
    private EditText mUHIExport;
    private EditText mCMDImport;
    private EditText mCMDExport;
    private EditText mBills;
    private EditText mPF;
    private EditText mVoltageR;
    private EditText mVoltageY;
    private EditText mVoltageB;
    private EditText mCurrentR;
    private EditText mCurrentY;
    private EditText mCurrentB;
    private EditText mKWHSegment1;
    private EditText mKWHSegment2;
    private EditText mKWHSegment3;
    private EditText mKWHSegment4;
    private EditText mKWHSegment5;
    private EditText mKWHSegment6;
    private EditText mKWHSegment7;
    private EditText mKWHSegment8;
    private EditText mKVAHSegment1;
    private EditText mKVAHSegment2;
    private EditText mKVAHSegment3;
    private EditText mKVAHSegment4;
    private EditText mKVAHSegment5;
    private EditText mKVAHSegment6;
    private EditText mKVAHSegment7;
    private EditText mKVAHSegment8;
    private EditText mETAccount;
    private EditText mSealing;
    private Button mSave;
    private Button mAdd;
    private Button mSelect;
    private Button mExport;
    private MasterDataSource dbSource;
    private Spinner mMeterListSpinner;
    private Spinner mMonth;
    private Spinner mYear;
    private Spinner mPreviousReadingDates;
    private ScrollView mScrollView;
    private ContentValues mConsumerDetails;
    private ContentValues mValues;
    private ContentValues mPreviousMonthReadings;
    private RelativeLayout mEditor;
    private boolean isDateInCorrectFormat = false;
    List<String> meterList;
    private Button mReset;
    private Button mUpdate;
    String mCurrentReadingID;
    ArrayList<ContentValues> allMeterReadings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmetering);
        dbSource = new MasterDataSource(this);
        mValues = new ContentValues();
        mSelect = (Button) findViewById(R.id.ht_bt_select);
        mMeterListSpinner = (Spinner) findViewById(R.id.ht_sp_meters);
        mKWHImport = (EditText) findViewById(R.id.ht_et_kwh_import);
        mKWHExport = (EditText) findViewById(R.id.ht_et_kwh_export);
        mKVAHImport = (EditText) findViewById(R.id.ht_et_kvah_import);
        mKVAHExport = (EditText) findViewById(R.id.ht_et_kvah_export);
        mKVARHImport = (EditText) findViewById(R.id.ht_et_kvarh_import);
        mKVARHExport = (EditText) findViewById(R.id.ht_et_kvarh_export);
        mUCImport = (EditText) findViewById(R.id.ht_et_UC_import);
        mUCExport = (EditText) findViewById(R.id.ht_et_UC_export);
        mUHIImport = (EditText) findViewById(R.id.ht_et_UHI_import);
        mUHIExport = (EditText) findViewById(R.id.ht_et_UHI_export);
        mCMDImport = (EditText) findViewById(R.id.ht_et_cmd_import);
        mCMDExport = (EditText) findViewById(R.id.ht_et_cmd_export);
        mBills = (EditText) findViewById(R.id.ht_et_bill);
        mPF = (EditText) findViewById(R.id.ht_et_pf);
        mVoltageR = (EditText) findViewById(R.id.ht_et_Voltage_r);
        mVoltageY = (EditText) findViewById(R.id.ht_et_Voltage_y);
        mVoltageB = (EditText) findViewById(R.id.ht_et_Voltage_b);
        mCurrentR = (EditText) findViewById(R.id.ht_et_current_r);
        mCurrentY = (EditText) findViewById(R.id.ht_et_current_y);
        mCurrentB = (EditText) findViewById(R.id.ht_et_current_b);
        mKWHSegment1 = (EditText) findViewById(R.id.ht_et_segment1);
        mKWHSegment2 = (EditText) findViewById(R.id.ht_et_segment2);
        mKWHSegment3 = (EditText) findViewById(R.id.ht_et_segment3);
        mKWHSegment4 = (EditText) findViewById(R.id.ht_et_segment4);
        mKWHSegment5 = (EditText) findViewById(R.id.ht_et_segment5);
        mKWHSegment6 = (EditText) findViewById(R.id.ht_et_segment6);
        mKWHSegment7 = (EditText) findViewById(R.id.ht_et_segment7);
        mKWHSegment8 = (EditText) findViewById(R.id.ht_et_segment8);
        mKVAHSegment1 = (EditText) findViewById(R.id.ht_et_kvah_segment1);
        mKVAHSegment2 = (EditText) findViewById(R.id.ht_et_kvah_segment2);
        mKVAHSegment3 = (EditText) findViewById(R.id.ht_et_kvah_segment3);
        mKVAHSegment4 = (EditText) findViewById(R.id.ht_et_kvah_segment4);
        mKVAHSegment5 = (EditText) findViewById(R.id.ht_et_kvah_segment5);
        mKVAHSegment6 = (EditText) findViewById(R.id.ht_et_kvah_segment6);
        mKVAHSegment7 = (EditText) findViewById(R.id.ht_et_kvah_segment7);
        mKVAHSegment8 = (EditText) findViewById(R.id.ht_et_kvah_segment8);
        mSealing = (EditText) findViewById(R.id.ht_et_seal);
        mETAccount = (EditText) findViewById(R.id.ht_et_account);
        mAdd = (Button) findViewById(R.id.ht_bt_add);
        mTime = (EditText) findViewById(R.id.ht_et_time);
        mSave = (Button) findViewById(R.id.ht_bt_save);
        mExport = (Button) findViewById(R.id.ht_bt_export);
        mReset = (Button) findViewById(R.id.ht_bt_reset);
        mEditor = (RelativeLayout) findViewById(R.id.ht_rl_editor);
        mUpdate = (Button) findViewById(R.id.ht_bt_update);
        mEditor.setVisibility(View.GONE);
        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Initialize Editor", "" + e.getMessage());
                }
                addListenerForDataValidation();

            }
        });
        mExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   exportAsPDF();
                Intent intent = new Intent(getApplicationContext(), ExportActivity.class);
                startActivity(intent);
                Log.e("Month", "" + mValues.get("MONTH"));
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddHTConsumerActivity.class);
                startActivity(intent);
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();

            }
        });
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
            }
        });
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }


    private void addListenerForDataValidation() {
        mTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
                    try {
                        Date date = dateFormat.parse(mTime.getText().toString());
                        isDateInCorrectFormat = true;
                        mValues.put(MasterDbHelper.COLUMN_NAME_READING_DATE, date.getTime());


                        Log.e("Reading Date", "" + date.toString());
                    } catch (ParseException e) {
                        isDateInCorrectFormat = false;
                        mTime.setError("Please put date correct Format");
                        e.printStackTrace();
                    }
                }
                isDateInCorrectFormat = false;

            }
        });
        mKWHImport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {

                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHImport.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = null;

                            previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_IMPORT);
                            if (previousMonthValue == null)
                                previousMonthValue = new Double(0);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (inputValue >= previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_IMPORT, inputValue);
                            else mKWHImport.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_IMPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHImport.setError("Enter Value");
                    }
                    Log.e("KWH Import", "" + mValues.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));

                }

            }
        });
        mKWHExport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHExport.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_EXPORT);
                            if (previousMonthValue == null)
                                previousMonthValue = new Double(0);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (inputValue >= previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_EXPORT, inputValue);
                            else mKWHExport.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_EXPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHExport.setError("Enter  Value");
                    }


                }

            }
        });
        mKVAHImport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    mKVAHImport.setError(null);
                    Double inputValue;
                    try {
                        inputValue = Double.parseDouble(mKVAHImport.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (inputValue >= previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT, inputValue);
                            else mKVAHImport.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHImport.setError("Enter Value");
                    }


                }

            }
        });
        mKVAHExport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    mKVAHExport.setError(null);

                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVAHExport.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_EXPORT);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (inputValue >= previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_EXPORT, inputValue);
                            else mKVAHExport.setError("Enter Correct Value");
                        } else
                            mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_EXPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHExport.setError("Enter Value");
                    }


                }

            }
        });
        mKVARHImport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    mKVARHImport.setError(null);
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVARHImport.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (inputValue >= previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT, inputValue);
                            else mKVARHImport.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVARHImport.setError("Enter Value");
                    }


                }

            }
        });
        mKVARHExport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    mKVARHExport.setError(null);
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVARHExport.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVARH_EXPORT);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (inputValue >= previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVARH_EXPORT, inputValue);
                            else mKVARHExport.setError("Enter Correct Value");
                        } else
                            mValues.put(MasterDbHelper.COLUMN_NAME_KVARH_EXPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVARHExport.setError("Enter Value");
                    }


                }

            }
        });
        mUCImport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mUCImport.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_UC_IMPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mUCImport.setError("Enter Value");

                    }


                }

            }
        });
        mUCExport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mUCExport.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_UC_EXPORT, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mUCExport.setError("Enter Value");
                    }


                }

            }
        });
        mUHIImport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mUHIImport.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_UHI_IMPORT, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mUHIImport.setError("Enter Value");
                    }


                }

            }
        });
        mUHIExport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mUHIExport.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_UHI_EXPORT, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mUHIExport.setError("Enter Value");
                    }

                }

            }
        });
        mCMDImport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mCMDImport.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_CMD_IMPORT);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (inputValue >= previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_CMD_IMPORT, inputValue);
                            else mCMDImport.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_CMD_IMPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mCMDImport.setError("Enter Value");
                    }


                }

            }
        });
        mCMDExport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mCMDExport.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_CMD_EXPORT);
                            if (previousMonthValue == null) previousMonthValue = new Double(0);
                            if (inputValue >= previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_CMD_EXPORT, inputValue);
                            else mCMDExport.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_CMD_EXPORT, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mCMDExport.setError("Enter Value");
                    }


                }

            }
        });
        mBills.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mBills.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_BILLS, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mBills.setError("Enter Value");
                    }

                }

            }
        });
        mPF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mPF.getText().toString());
                        if (inputValue >= 1) mPF.setError("Error Correct Value");
                        else
                            mValues.put(MasterDbHelper.COLUMN_NAME_PF, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mPF.setError("Enter Value");
                    }

                }

            }
        });
        mVoltageR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        Double inputValue = Double.parseDouble(mVoltageR.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_VOLTAGE_R, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mVoltageR.setError("Enter Value");
                    }

                }

            }
        });
        mVoltageY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        Double inputValue = Double.parseDouble(mVoltageY.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_VOLTAGE_Y, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mVoltageY.setError("Enter Value");
                    }

                }

            }
        });
        mVoltageB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        Double inputValue = Double.parseDouble(mVoltageB.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_VOLTAGE_B, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mVoltageB.setError("Enter Value");
                    }

                }

            }
        });
        mCurrentR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        Double inputValue = Double.parseDouble(mCurrentR.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_CURRENT_R, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mCurrentR.setError("Enter Value");
                    }

                }

            }
        });
        mCurrentY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        Double inputValue = Double.parseDouble(mCurrentY.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_CURRENT_Y, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mCurrentY.setError("Enter Value");
                    }

                }

            }
        });
        mCurrentB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        Double inputValue = Double.parseDouble(mCurrentB.getText().toString());
                        mValues.put(MasterDbHelper.COLUMN_NAME_CURRENT_B, inputValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mCurrentB.setError("Enter Value");
                    }

                }

            }
        });
        mKWHSegment1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHSegment1.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1);
                            if (inputValue > previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1, inputValue);
                            else mKWHSegment1.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment1.setError(e.toString());
                    }


                }

            }
        });
        mKWHSegment2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHSegment2.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2);
                            if (inputValue > previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2, inputValue);
                            else mKWHSegment2.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment2.setError(e.toString());
                    }


                }

            }
        });
        mKWHSegment3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHSegment3.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3);
                            if (inputValue > previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3, inputValue);
                            else mKWHSegment3.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment3.setError(e.toString());
                    }


                }

            }
        });
        mKWHSegment4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHSegment4.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4);
                            if (inputValue > previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4, inputValue);
                            else mKWHSegment4.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment4.setError("Enter Value");
                    }


                }

            }
        });
        mKWHSegment5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHSegment5.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5);
                            if (inputValue > previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5, inputValue);
                            else mKWHSegment5.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment5.setError("Enter Value");
                    }


                }

            }
        });
        mKWHSegment6.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHSegment6.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6);
                            if (inputValue > previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6, inputValue);
                            else mKWHSegment6.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment6.setError("Enter Value");
                    }


                }

            }
        });
        mKWHSegment7.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHSegment7.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7);
                            if (inputValue > previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7, inputValue);
                            else mKWHSegment7.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment7.setError("Enter Value");
                    }


                }

            }
        });
        mKWHSegment8.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKWHSegment8.getText().toString());
                        if (mPreviousMonthReadings!= null) {
                            Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8);
                            if (inputValue > previousMonthValue)
                                mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8, inputValue);
                            else mKWHSegment8.setError("Enter Correct Value");
                        } else mValues.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8, inputValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment8.setError("Enter Value");
                    }


                }

            }
        });
        mKVAHSegment1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVAHSegment1.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment1.setError("Enter Value");
                    }

                    try {

                        Double correspondingKWHValue = Double.parseDouble(mKWHSegment1.getText().toString());
                        if (inputValue >= correspondingKWHValue) {
                            if (mPreviousMonthReadings!= null) {
                                Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1);
                                if (previousMonthValue == null) previousMonthValue = new Double(0);

                                if (inputValue > previousMonthValue)
                                    mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1, inputValue);
                                else mKVAHSegment1.setError("Enter Correct Value");
                            } else {
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1, inputValue);

                            }
                        } else mKVAHSegment1.setError("Value is less than KWH");

                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment1.setError("First Enter KWH Segment Value");
                    }


                }

            }
        });
        mKVAHSegment2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = new Double(0);
                    try {
                        inputValue = Double.parseDouble(mKVAHSegment2.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment2.setError("Enter Value");
                    }
                    try {
                        Double correspondingKWHValue = Double.parseDouble(mKWHSegment2.getText().toString());
                        if (correspondingKWHValue < inputValue) {
                            if (mPreviousMonthReadings!= null) {
                                Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2);
                                if (previousMonthValue == null) previousMonthValue = new Double(0);
                                if (inputValue > previousMonthValue)
                                    mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2, inputValue);
                                else mKVAHSegment2.setError("Enter Correct Value");
                            } else
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2, inputValue);

                        } else mKVAHSegment2.setError("Value is less than KWH");

                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment2.setError("First Enter KWH value");
                    }


                }

            }
        });
        mKVAHSegment3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVAHSegment3.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment3.setError("Enter Value");
                    }
                    try {
                        Double correspondingKWHValue = Double.parseDouble(mKWHSegment3.getText().toString());
                        if (inputValue >= correspondingKWHValue) {
                            if (mPreviousMonthReadings!= null) {
                                Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3);
                                if (previousMonthValue == null) previousMonthValue = new Double(0);
                                if (inputValue > previousMonthValue)
                                    mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3, inputValue);
                                else mKVAHSegment3.setError("Enter Correct Value");
                            } else
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3, inputValue);

                        } else mKVAHSegment3.setError("Value is less than KWH");
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment3.setError("First Enter KWH Value");
                    }


                }

            }
        });
        mKVAHSegment4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVAHSegment4.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment4.setError("Enter Value");
                    }
                    try {
                        Double correspondingKWHValue = Double.parseDouble(mKWHSegment4.getText().toString());
                        if (inputValue >= correspondingKWHValue) {
                            if (mPreviousMonthReadings!= null) {
                                Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4);
                                if (previousMonthValue == null) previousMonthValue = new Double(0);
                                if (inputValue > previousMonthValue)
                                    mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4, inputValue);
                                else mKVAHSegment4.setError("Enter Correct Value");
                            } else
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4, inputValue);

                        } else mKVAHSegment4.setError("Value is less than KWH");
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment4.setError("First Enter KWH Value");
                    }

                }

            }
        });
        mKVAHSegment5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVAHSegment5.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment5.setError("Enter Value");
                    }
                    try {
                        Double correspondingKWHValue = Double.parseDouble(mKWHSegment5.getText().toString());
                        if (inputValue >= correspondingKWHValue) {
                            if (mPreviousMonthReadings!= null) {
                                Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5);
                                if (previousMonthValue == null) previousMonthValue = new Double(0);
                                if (inputValue > previousMonthValue)
                                    mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5, inputValue);
                                else mKVAHSegment5.setError("Enter Correct Value");
                            } else
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5, inputValue);

                        } else mKVAHSegment5.setError("Value is less than KWH");
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment5.setError("First Enter KWH Value");
                    }
                }

            }
        });
        mKVAHSegment6.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVAHSegment6.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKWHSegment6.setText("Enter Value");
                    }
                    try {
                        Double correspondingKWHValue = Double.parseDouble(mKWHSegment6.getText().toString());
                        if (inputValue >= correspondingKWHValue) {
                            if (mPreviousMonthReadings!= null) {
                                Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6);
                                if (previousMonthValue == null) previousMonthValue = new Double(0);
                                if (inputValue > previousMonthValue)
                                    mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6, inputValue);
                                else mKVAHSegment6.setError("Enter Correct Value");
                            } else
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6, inputValue);

                        } else mKVAHSegment6.setError("Value is less than KWH");
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment6.setError("First Enter KWH Value");
                    }

                }

            }
        });
        mKVAHSegment7.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVAHSegment7.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment7.setText("Enter Value");
                    }
                    try {
                        Double correspondingKWHValue = Double.parseDouble(mKWHSegment7.getText().toString());
                        if (inputValue >= correspondingKWHValue) {
                            if (mPreviousMonthReadings!= null) {
                                Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7);
                                if (previousMonthValue == null) previousMonthValue = new Double(0);
                                if (inputValue > previousMonthValue)
                                    mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7, inputValue);
                                else mKVAHSegment7.setError("Enter Correct Value");
                            } else
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7, inputValue);

                        } else mKVAHSegment7.setError("Value is less than KWH");
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment7.setError("First Enter KWH Value");
                    }

                }

            }
        });
        mKVAHSegment8.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Double inputValue = null;
                    try {
                        inputValue = Double.parseDouble(mKVAHSegment8.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment8.setError("Enter Value");
                    }
                    try {
                        Double correspondingKWHValue = Double.parseDouble(mKWHSegment8.getText().toString());
                        if (inputValue >= correspondingKWHValue) {
                            if (mPreviousMonthReadings!= null) {
                                Double previousMonthValue = mPreviousMonthReadings.getAsDouble(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8);
                                if (previousMonthValue == null) previousMonthValue = new Double(0);
                                if (inputValue > previousMonthValue)
                                    mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8, inputValue);
                                else mKVAHSegment8.setError("Enter Correct Value");
                            } else
                                mValues.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8, inputValue);

                        } else mKVAHSegment8.setError("Value is less than KWH");
                    } catch (Exception e) {
                        e.printStackTrace();
                        mKVAHSegment8.setError("First Enter KWH Value");
                    }
                }

            }
        });

        mValues.put("SEAL", "" + mSealing.getText().toString());
    }

    public boolean validateData() {
        boolean isAllFieldValid = false;
        if (isDateInCorrectFormat) {
            if (mPreviousMonthReadings == null) {
                isAllFieldValid = true;
            } else {

            }
        }
        return isAllFieldValid;
    }

    public void init() {

        mPreviousReadingDates = (Spinner) findViewById(R.id.ht_sp_previous);
        mPreviousReadingDates.setVisibility(View.VISIBLE);
        dbSource.open();
        ArrayList<String> meters = dbSource.getMeterList(mETAccount.getText().toString());
        dbSource.close();
        if (meters.size() > 0) {
            mEditor.setVisibility(View.VISIBLE);
            mMeterListSpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, meters);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mMeterListSpinner.setAdapter(dataAdapter);
            mMeterListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    dbSource.open();
                    clearData();
                    mValues.put(MasterDbHelper.COLUMN_NAME_METER_NO, String.valueOf(mMeterListSpinner.getSelectedItem()));

                    allMeterReadings = dbSource.getAllPreviousMeterReading(String.valueOf(mMeterListSpinner.getSelectedItem()));
                    mConsumerDetails = dbSource.getConsumerDetailsByMeterNo(String.valueOf(mMeterListSpinner.getSelectedItem()), position);
                    dbSource.close();
                    if (allMeterReadings.size() != 0) {
                        ArrayList<String> previousReadingDateList = new ArrayList<String>();
                        for (int i = 0; i < allMeterReadings.size(); i++) {
                            Long dateInMillis =allMeterReadings.get(i).getAsLong(MasterDbHelper.COLUMN_NAME_READING_DATE);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(dateInMillis);
                            previousReadingDateList.add(dateFormat.format(calendar.getTime()));
                        }
                        mPreviousReadingDates.setVisibility(View.VISIBLE);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(HTMeteringActivity.this, android.R.layout.simple_spinner_item, previousReadingDateList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mPreviousReadingDates.setAdapter(dataAdapter);
                        mPreviousReadingDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                dbSource.open();
                                ContentValues values=allMeterReadings.get(mPreviousReadingDates.getSelectedItemPosition());
                                Log.e("Values-------", "" + values.size());
                                mCurrentReadingID = values.getAsString("_ID");
                                values.put(MasterDbHelper.COLUMN_NAME_METER_NO, String.valueOf(mMeterListSpinner.getSelectedItem()));
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
                                    Long dateInMillis =values.getAsLong(MasterDbHelper.COLUMN_NAME_READING_DATE);

                                    Date date=new Date(dateInMillis);

                                    mTime.setText(dateFormat.format(date));
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                mKWHImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
                                mKWHExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_EXPORT));
                                mKVAHImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
                                mKVAHExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_EXPORT));
                                mKVARHImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
                                mKVARHExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_EXPORT));
                                mUCImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
                                mUCExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_UC_EXPORT));
                                mUHIImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_UHI_IMPORT));
                                mUHIExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_UHI_EXPORT));
                                mCMDImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CMD_IMPORT));
                                mCMDExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CMD_EXPORT));
                                mBills.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_BILLS));
                                mPF.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_PF));
                                mVoltageR.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_R));
                                mVoltageY.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_Y));
                                mVoltageB.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_B));
                                mCurrentR.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_R));
                                mCurrentY.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_Y));
                                mCurrentB.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_B));
                                mKWHSegment1.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1));
                                mKWHSegment2.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2));
                                mKWHSegment3.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3));
                                mKWHSegment4.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4));
                                mKWHSegment5.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5));
                                mKWHSegment6.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6));
                                mKWHSegment7.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7));
                                mKWHSegment8.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8));
                                mKVAHSegment1.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1));
                                mKVAHSegment2.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2));
                                mKVAHSegment3.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3));
                                mKVAHSegment4.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4));
                                mKVAHSegment5.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5));
                                mKVAHSegment6.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6));
                                mKVAHSegment7.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7));
                                mKVAHSegment8.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8));
                                mSealing.setText(values.getAsString("SEAL"));
                                try {
                                    String month = values.getAsString("MONTH");
                                    String d[] = month.split("-");
                                    switch (d[0]) {
                                        case "JAN":
                                            mMonth.setSelection(0);
                                            break;

                                        case "FEB":
                                            mMonth.setSelection(1);
                                            break;

                                        case "MAR":
                                            mMonth.setSelection(2);
                                            break;

                                        case "APR":
                                            mMonth.setSelection(3);
                                            break;

                                        case "MAY":
                                            mMonth.setSelection(4);
                                            break;

                                        case "JUN":
                                            mMonth.setSelection(5);
                                            break;

                                        case "JUL":
                                            mMonth.setSelection(6);
                                            break;

                                        case "AUG":
                                            mMonth.setSelection(7);
                                            break;

                                        case "SEP":
                                            mMonth.setSelection(8);
                                            break;

                                        case "OCT":
                                            mMonth.setSelection(9);
                                            break;

                                        case "NOV":
                                            mMonth.setSelection(10);
                                            break;

                                        case "DEC":
                                            mMonth.setSelection(11);
                                            break;

                                    }
                                    switch (d[1]) {
                                        case "2015":
                                            mYear.setSelection(0);
                                            break;

                                        case "2016":
                                            mYear.setSelection(1);
                                            break;


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else {
                        mPreviousReadingDates.setVisibility(View.INVISIBLE);

                        Toast.makeText(getApplicationContext(), "No Previous Reading for This meter", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mMonth = (Spinner) findViewById(R.id.ht_sp_month);
            ArrayList<String> monthList = new ArrayList<String>();
            monthList.add("JAN");
            monthList.add("FEB");
            monthList.add("MAR");
            monthList.add("APR");
            monthList.add("MAY");
            monthList.add("JUN");
            monthList.add("JUL");
            monthList.add("AUG");
            monthList.add("SEP");
            monthList.add("OCT");
            monthList.add("NOV");
            monthList.add("DEC");
            dataAdapter = new ArrayAdapter<String>(HTMeteringActivity.this,
                    android.R.layout.simple_spinner_item, monthList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mMonth.setAdapter(dataAdapter);
            mMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mValues.put("MONTH", String.valueOf(mMonth.getSelectedItem()) + "-" + String.valueOf(mYear.getSelectedItem()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            mYear = (Spinner) findViewById(R.id.ht_sp_year);
            ArrayList<String> yearList = new ArrayList<String>();
            yearList.add("2015");
            yearList.add("2016");
            ArrayAdapter<String> data2Adapter = new ArrayAdapter<String>(HTMeteringActivity.this,
                    android.R.layout.simple_spinner_item, yearList);
            data2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mYear.setAdapter(data2Adapter);
            mYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                      mValues.put("MONTH", String.valueOf(mMonth.getSelectedItem()) + "-" + String.valueOf(mYear.getSelectedItem()));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Add This Consumer First", Toast.LENGTH_SHORT).show();

        }

    }

    public void initializeEditor() throws Exception {

        mPreviousReadingDates = (Spinner) findViewById(R.id.ht_sp_previous);
        // mPreviousReadingDates.setVisibility(View.GONE);
        dbSource.open();
        ArrayList<String> meters = dbSource.getMeterList(mETAccount.getText().toString());
        dbSource.close();
        if (meters.size() > 0) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, meters);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mMeterListSpinner.setAdapter(dataAdapter);
            mMeterListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    dbSource.open();
                    clearData();
                    Log.e("Spinner ITEM", "" + 1);
                    mValues.put(MasterDbHelper.COLUMN_NAME_METER_NO, String.valueOf(mMeterListSpinner.getSelectedItem()));

                    mPreviousMonthReadings = dbSource.getPreviousMeterReading(String.valueOf(mMeterListSpinner.getSelectedItem()));
                    mConsumerDetails = dbSource.getConsumerDetailsByMeterNo(String.valueOf(mMeterListSpinner.getSelectedItem()), position);
                    if (mPreviousMonthReadings == null) {
                        Log.e("Previous Reading" + 1, "is Null");

                    }
                    dbSource.close();
                    dbSource.open();
                    ArrayList<String> previousReadingDateList = dbSource.getPreviousReadingDates(String.valueOf(mMeterListSpinner.getSelectedItem()));
                    if (previousReadingDateList.size() > 1) {
                        mPreviousReadingDates.setVisibility(View.VISIBLE);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(HTMeteringActivity.this,
                                android.R.layout.simple_spinner_item, previousReadingDateList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mPreviousReadingDates.setAdapter(dataAdapter);
                        mPreviousReadingDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        //  mPreviousReadingDates.setVisibility(View.GONE);

                    }
                    dbSource.close();


                    mPreviousReadingDates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            dbSource.open();
                            ContentValues values = dbSource.getMeterReadingByTimeStamp(String.valueOf(mPreviousReadingDates.getSelectedItem()));
                            Log.e("Values-------", "" + values.size());
                            mCurrentReadingID = values.getAsString("_ID");
                            values.put(MasterDbHelper.COLUMN_NAME_METER_NO, String.valueOf(mMeterListSpinner.getSelectedItem()));
                            mTime.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_READING_DATE));
                            mKWHImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
                            mKWHExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_EXPORT));
                            mKVAHImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
                            mKVAHExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_EXPORT));
                            mKVARHImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
                            mKVARHExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_EXPORT));
                            mUCImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
                            mUCExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_UC_EXPORT));
                            mUHIImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_UHI_IMPORT));
                            mUHIExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_UHI_EXPORT));
                            mCMDImport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CMD_IMPORT));
                            mCMDExport.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CMD_EXPORT));
                            mBills.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_BILLS));
                            mPF.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_PF));
                            mVoltageR.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_R));
                            mVoltageY.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_Y));
                            mVoltageB.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_B));
                            mCurrentR.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_R));
                            mCurrentY.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_Y));
                            mCurrentB.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_B));
                            mKWHSegment1.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1));
                            mKWHSegment2.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2));
                            mKWHSegment3.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3));
                            mKWHSegment4.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4));
                            mKWHSegment5.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5));
                            mKWHSegment6.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6));
                            mKWHSegment7.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7));
                            mKWHSegment8.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8));
                            mKVAHSegment1.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1));
                            mKVAHSegment2.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2));
                            mKVAHSegment3.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3));
                            mKVAHSegment4.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4));
                            mKVAHSegment5.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5));
                            mKVAHSegment6.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6));
                            mKVAHSegment7.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7));
                            mKVAHSegment8.setText(values.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8));
                            mSealing.setText(values.getAsString("SEAL"));
                            try {
                                String month = values.getAsString("MONTH");
                                String d[] = month.split("-");
                                switch (d[0]) {
                                    case "JAN":
                                        mMonth.setSelection(0);
                                        break;

                                    case "FEB":
                                        mMonth.setSelection(1);
                                        break;

                                    case "MAR":
                                        mMonth.setSelection(2);
                                        break;

                                    case "APR":
                                        mMonth.setSelection(3);
                                        break;

                                    case "MAY":
                                        mMonth.setSelection(4);
                                        break;

                                    case "JUN":
                                        mMonth.setSelection(5);
                                        break;

                                    case "JUL":
                                        mMonth.setSelection(6);
                                        break;

                                    case "AUG":
                                        mMonth.setSelection(7);
                                        break;

                                    case "SEP":
                                        mMonth.setSelection(8);
                                        break;

                                    case "OCT":
                                        mMonth.setSelection(9);
                                        break;

                                    case "NOV":
                                        mMonth.setSelection(10);
                                        break;

                                    case "DEC":
                                        mMonth.setSelection(11);
                                        break;

                                }
                                switch (d[1]) {
                                    case "2015":
                                        mYear.setSelection(0);
                                        break;

                                    case "2016":
                                        mYear.setSelection(1);
                                        break;


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    mMonth = (Spinner) findViewById(R.id.ht_sp_month);
                    ArrayList<String> monthList = new ArrayList<String>();
                    monthList.add("JAN");
                    monthList.add("FEB");
                    monthList.add("MAR");
                    monthList.add("APR");
                    monthList.add("MAY");
                    monthList.add("JUN");
                    monthList.add("JUL");
                    monthList.add("AUG");
                    monthList.add("SEP");
                    monthList.add("OCT");
                    monthList.add("NOV");
                    monthList.add("DEC");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(HTMeteringActivity.this,
                            android.R.layout.simple_spinner_item, monthList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mMonth.setAdapter(dataAdapter);
                    mMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            mValues.put("MONTH", String.valueOf(mMonth.getSelectedItem()) + "-" + String.valueOf(mYear.getSelectedItem()));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    mYear = (Spinner) findViewById(R.id.ht_sp_year);
                    ArrayList<String> yearList = new ArrayList<String>();
                    yearList.add("2015");
                    yearList.add("2016");
                    ArrayAdapter<String> data2Adapter = new ArrayAdapter<String>(HTMeteringActivity.this,
                            android.R.layout.simple_spinner_item, yearList);
                    data2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mYear.setAdapter(data2Adapter);
                    mYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //  mValues.put("MONTH", String.valueOf(mMonth.getSelectedItem()) + "-" + String.valueOf(mYear.getSelectedItem()));

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            mEditor.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getApplicationContext(), "Please Add This Account ID First", Toast.LENGTH_SHORT).show();

        }

    }


    public static void writeBytesToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    private String millistoDate(long dateInMillis){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);


        return dateFormat.format(calendar.getTime());
    }
private long dateToMills(String text){
    long mills=0;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
    try {
        Date date = dateFormat.parse(text);
        mills=date.getTime();
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return mills;
}
    private void update() {
        if (isDataValid()) {
            try {
                ContentValues values = new ContentValues();
                values.put(MasterDbHelper.COLUMN_NAME_METER_NO, String.valueOf(mMeterListSpinner.getSelectedItem()));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
                try {
                    Date date = dateFormat.parse(mTime.getText().toString());
                    values.put(MasterDbHelper.COLUMN_NAME_READING_DATE, date.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e("Update Exception",""+e.getMessage());

                }

                values.put(MasterDbHelper.COLUMN_NAME_KWH_IMPORT, mKWHImport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_EXPORT, mKWHExport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT, mKVARHImport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVARH_EXPORT, mKVARHExport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT, mKVAHImport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_EXPORT, mKVAHExport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_UC_IMPORT, mUCImport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_UC_EXPORT, mUCExport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_UHI_IMPORT, mUHIImport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_UHI_EXPORT, mUHIExport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_CMD_IMPORT, mCMDImport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_CMD_EXPORT, mCMDExport.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_BILLS, mBills.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_PF, mPF.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_VOLTAGE_R, mVoltageR.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_VOLTAGE_Y, mVoltageY.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_VOLTAGE_B, mVoltageB.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_CURRENT_R, mCurrentR.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_CURRENT_Y, mCurrentY.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_CURRENT_B, mCurrentB.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1, mKWHSegment1.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2, mKWHSegment2.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3, mKWHSegment3.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4, mKWHSegment4.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5, mKWHSegment5.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6, mKWHSegment6.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7, mKWHSegment7.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8, mKWHSegment8.getText().toString());

                values.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1, mKVAHSegment1.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2, mKVAHSegment2.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3, mKVAHSegment3.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4, mKVAHSegment4.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5, mKVAHSegment5.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6, mKVAHSegment6.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7, mKVAHSegment7.getText().toString());
                values.put(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8, mKVAHSegment8.getText().toString());
                values.put("SEAL", mSealing.getText().toString());

                dbSource.open();

                //   insertLocation = dbSource.insertMeterReadings(mValues);
                int affectedRows = dbSource.updateReadings(mCurrentReadingID, values);
                if (affectedRows > 0) {
                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getApplicationContext(), "Updation Failed", Toast.LENGTH_SHORT).show();

                dbSource.close();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Updation failed with Exeption", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Update Data", Toast.LENGTH_SHORT).show();

        }
    }

    private void save() {
        if (isDataValid()) {
            //  Toast.makeText(getApplicationContext(), "Data is valid", Toast.LENGTH_SHORT).show();

            try {
                dbSource.open();
                long insertLocation = 0;
                mValues.put("SEAL", mSealing.getText().toString());
                insertLocation = dbSource.insertMeterReadings(mValues);
                if (insertLocation > 0) {
                    Toast.makeText(getApplicationContext(), "Reading Added Succesfully", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getApplicationContext(), "Reading Not Added", Toast.LENGTH_SHORT).show();

                dbSource.close();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Reading Not Added", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Data is Invalid", Toast.LENGTH_SHORT).show();

        }

    }

    private boolean isDataValid() {

        boolean isDataValid = true;
        Log.e("KWH on Save", "" + mValues.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));


        if (mTime.getError() != null) isDataValid = false;
        if (mKWHImport.getError() != null) isDataValid = false;
        if (mKWHExport.getError() != null) isDataValid = false;
        if (mKVARHImport.getError() != null) isDataValid = false;
        if (mKVARHExport.getError() != null) isDataValid = false;
        if (mKVAHImport.getError() != null) isDataValid = false;
        if (mKVAHExport.getError() != null) isDataValid = false;
        if (mUCImport.getError() != null) isDataValid = false;
        if (mUCExport.getError() != null) isDataValid = false;
        if (mUHIImport.getError() != null) isDataValid = false;
        if (mUHIExport.getError() != null) isDataValid = false;
        if (mCMDImport.getError() != null) isDataValid = false;
        if (mCMDExport.getError() != null) isDataValid = false;
        if (mBills.getError() != null) isDataValid = false;
        if (mPF.getError() != null) isDataValid = false;
        if (mVoltageR.getError() != null) isDataValid = false;
        if (mVoltageY.getError() != null) isDataValid = false;
        if (mVoltageB.getError() != null) isDataValid = false;
        if (mCurrentR.getError() != null) isDataValid = false;
        if (mCurrentY.getError() != null) isDataValid = false;
        if (mCurrentB.getError() != null) isDataValid = false;
        if (mKWHSegment1.getError() != null) isDataValid = false;
        if (mKWHSegment3.getError() != null) isDataValid = false;
        if (mKWHSegment4.getError() != null) isDataValid = false;
        if (mKWHSegment5.getError() != null) isDataValid = false;
        if (mKWHSegment6.getError() != null) isDataValid = false;
        if (mKWHSegment7.getError() != null) isDataValid = false;
        if (mKWHSegment2.getError() != null) isDataValid = false;
        if (mKWHSegment8.getError() != null) isDataValid = false;
        if (mKVAHSegment1.getError() != null) isDataValid = false;
        if (mKVAHSegment2.getError() != null) isDataValid = false;
        if (mKVAHSegment3.getError() != null) isDataValid = false;
        if (mKVAHSegment4.getError() != null) isDataValid = false;
        if (mKVAHSegment5.getError() != null) isDataValid = false;
        if (mKVAHSegment6.getError() != null) isDataValid = false;
        if (mKVAHSegment7.getError() != null) isDataValid = false;
        if (mKVAHSegment8.getError() != null) isDataValid = false;

        if (mTime.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHImport.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHExport.getText().toString().isEmpty()) isDataValid = false;
        if (mKVARHImport.getText().toString().isEmpty()) isDataValid = false;
        if (mKVARHExport.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHImport.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHExport.getText().toString().isEmpty()) isDataValid = false;
        if (mUCImport.getText().toString().isEmpty()) isDataValid = false;
        if (mUCExport.getText().toString().isEmpty()) isDataValid = false;
        if (mUHIImport.getText().toString().isEmpty()) isDataValid = false;
        if (mUHIExport.getText().toString().isEmpty()) isDataValid = false;
        if (mCMDImport.getText().toString().isEmpty()) isDataValid = false;
        if (mCMDExport.getText().toString().isEmpty()) isDataValid = false;
        if (mBills.getText().toString().isEmpty()) isDataValid = false;
        if (mPF.getText().toString().isEmpty()) isDataValid = false;
        if (mVoltageR.getText().toString().isEmpty()) isDataValid = false;
        if (mVoltageY.getText().toString().isEmpty()) isDataValid = false;
        if (mVoltageB.getText().toString().isEmpty()) isDataValid = false;
        if (mCurrentR.getText().toString().isEmpty()) isDataValid = false;
        if (mCurrentY.getText().toString().isEmpty()) isDataValid = false;
        if (mCurrentB.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHSegment1.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHSegment3.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHSegment4.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHSegment5.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHSegment6.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHSegment7.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHSegment2.getText().toString().isEmpty()) isDataValid = false;
        if (mKWHSegment8.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHSegment1.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHSegment2.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHSegment3.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHSegment4.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHSegment5.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHSegment6.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHSegment7.getText().toString().isEmpty()) isDataValid = false;
        if (mKVAHSegment8.getText().toString().isEmpty()) isDataValid = false;
        try {
            Double sumOfKWHSegments = Double.parseDouble(mKWHSegment1.getText().toString()) +
                    Double.parseDouble(mKWHSegment2.getText().toString()) +
                    Double.parseDouble(mKWHSegment3.getText().toString()) +
                    Double.parseDouble(mKWHSegment4.getText().toString()) +
                    Double.parseDouble(mKWHSegment5.getText().toString()) +
                    Double.parseDouble(mKWHSegment6.getText().toString()) +
                    Double.parseDouble(mKWHSegment7.getText().toString()) +
                    Double.parseDouble(mKWHSegment8.getText().toString());

            Double sumOfKVAHSegments = Double.parseDouble(mKVAHSegment1.getText().toString()) +
                    Double.parseDouble(mKVAHSegment2.getText().toString()) +
                    Double.parseDouble(mKVAHSegment3.getText().toString()) +
                    Double.parseDouble(mKVAHSegment4.getText().toString()) +
                    Double.parseDouble(mKVAHSegment5.getText().toString()) +
                    Double.parseDouble(mKVAHSegment6.getText().toString()) +
                    Double.parseDouble(mKVAHSegment7.getText().toString()) +
                    Double.parseDouble(mKVAHSegment8.getText().toString());
            Double percentageErrorInKWH = Math.abs((sumOfKWHSegments - Double.parseDouble(mKWHImport.getText().toString())) / sumOfKWHSegments);
            Double percentageErrorInKVAH = Math.abs((sumOfKWHSegments - Double.parseDouble(mKVAHImport.getText().toString())) / sumOfKVAHSegments);
            Log.e("eKWH::" + percentageErrorInKWH, "eKVAH::" + percentageErrorInKVAH);
           /* if (percentageErrorInKVAH > 0.1) {
                Toast.makeText(getApplicationContext(), "Error is large in KVAH", Toast.LENGTH_SHORT).show();
                isDataValid = false;
            }
            if (percentageErrorInKWH > 0.1) {
                Toast.makeText(getApplicationContext(), "Error is large in KWH", Toast.LENGTH_SHORT).show();
                isDataValid = false;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDataValid;
    }

    private void readEditText() {

    }

    public void clearData() {

        mKWHImport.setText("");
        mKWHExport.setText("");
        mKVAHImport.setText("");
        mKVAHExport.setText("");
        mKVARHImport.setText("");
        mKVARHExport.setText("");
        mUCImport.setText("");
        mUCExport.setText("");
        mUHIImport.setText("");
        mUHIExport.setText("");
        mCMDImport.setText("");
        mCMDExport.setText("");
        mBills.setText("");
        mPF.setText("");
        mVoltageR.setText("");
        mVoltageY.setText("");
        mVoltageB.setText("");
        mCurrentR.setText("");
        mCurrentY.setText("");
        mCurrentB.setText("");
        mKWHSegment1.setText("");
        mKWHSegment2.setText("");
        mKWHSegment3.setText("");
        mKWHSegment4.setText("");
        mKWHSegment5.setText("");
        mKWHSegment6.setText("");
        mKWHSegment7.setText("");
        mKWHSegment8.setText("");
        mKVAHSegment1.setText("");
        mKVAHSegment2.setText("");
        mKVAHSegment3.setText("");
        mKVAHSegment4.setText("");
        mKVAHSegment5.setText("");
        mKVAHSegment6.setText("");
        mKVAHSegment7.setText("");
        mKVAHSegment8.setText("");
        mSealing.setText("");
        mTime.setText("");

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
/*   */