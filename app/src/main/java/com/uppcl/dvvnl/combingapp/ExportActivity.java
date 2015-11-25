package com.uppcl.dvvnl.combingapp;

import android.content.ContentValues;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExportActivity extends AppCompatActivity {
    private EditText mAccountID;
    private Button mSelect;
    private Button mExport;
    private Spinner mMainSpinner;
    private Spinner mPoleSpinner;
    private Spinner mPreMainSpinner;
    private Spinner mPrePoleSpinner;
    private TextView mMainText;
    private TextView mPoleText;
    private MasterDataSource dbSource;
    private ContentValues mMainReadings;
    private ContentValues mPoleReadings;
    private ContentValues mPreMainReading;
    private ContentValues mPrePoleReading;
    private ContentValues mConsumerDetails;
    private ArrayList<String> mAllMeters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_activiry);
        dbSource = new MasterDataSource(this);

        mAccountID = (EditText) findViewById(R.id.ex_et_account);
        mSelect = (Button) findViewById(R.id.ex_bt_select);
        mExport = (Button) findViewById(R.id.ex_bt_export);
        mMainSpinner = (Spinner) findViewById(R.id.ex_sp_main_meter);
        mPoleSpinner = (Spinner) findViewById(R.id.ex_sp_pole_meter);
        mPreMainSpinner = (Spinner) findViewById(R.id.ex_sp_pre_main_meter);
        mPrePoleSpinner = (Spinner) findViewById(R.id.ex_sp_pre_pole_meter);
        mMainText = (TextView) findViewById(R.id.ex_tv_main_meter);
        mPoleText = (TextView) findViewById(R.id.ex_tv_pole_meter);
        mExport.setVisibility(View.GONE);
        mMainSpinner.setVisibility(View.GONE);
        mPoleSpinner.setVisibility(View.GONE);
        mPreMainSpinner.setVisibility(View.GONE);
        mPrePoleSpinner.setVisibility(View.GONE);
        mMainText.setVisibility(View.GONE);
        mPoleText.setVisibility(View.GONE);


        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbSource.open();
                mAllMeters = dbSource.getMeterList(mAccountID.getText().toString());
                dbSource.close();
                if (mAllMeters.size() > 0) {
                    mMainText.setVisibility(View.VISIBLE);
                    mMainText.setText(mAllMeters.get(0));
                    mPoleText.setVisibility(View.VISIBLE);
                    mPoleText.setText(mAllMeters.get(1));
                    dbSource.open();
                  ArrayList<ContentValues> allMainReadings=  dbSource.getAllPreviousMeterReading(String.valueOf(mAllMeters.get(0)));
                    ArrayList<ContentValues> allPoleReadings=  dbSource.getAllPreviousMeterReading(String.valueOf(mAllMeters.get(1)));
                    ArrayList<String> previousMainReadingList = new ArrayList<String>();
                    for (int i = 0; i < allMainReadings.size(); i++) {
                        Long dateInMillis =allMainReadings.get(i).getAsLong(MasterDbHelper.COLUMN_NAME_READING_DATE);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dateInMillis);
                        previousMainReadingList.add(dateFormat.format(calendar.getTime()));
                    }
                    ArrayList<String> previousPoleReadingList = new ArrayList<String>();

                    for (int i = 0; i < allPoleReadings.size(); i++) {
                        Long dateInMillis =allPoleReadings.get(i).getAsLong(MasterDbHelper.COLUMN_NAME_READING_DATE);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dateInMillis);
                        previousPoleReadingList.add(dateFormat.format(calendar.getTime()));
                    }


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ExportActivity.this,
                            android.R.layout.simple_spinner_item, previousMainReadingList);
                    mMainSpinner.setVisibility(View.VISIBLE);
                    mMainSpinner.setAdapter(dataAdapter);
                    mPreMainSpinner.setVisibility(View.VISIBLE);
                    mPreMainSpinner.setAdapter(dataAdapter);
                    dataAdapter = new ArrayAdapter<String>(ExportActivity.this,
                            android.R.layout.simple_spinner_item, previousPoleReadingList);
                    mPoleSpinner.setVisibility(View.VISIBLE);
                    mPoleSpinner.setAdapter(dataAdapter);
                    mPrePoleSpinner.setVisibility(View.VISIBLE);
                    mPrePoleSpinner.setAdapter(dataAdapter);


                    mConsumerDetails = dbSource.getConsumerDetailsByMeterNo(mAllMeters.get(0), 0);
                    mExport.setVisibility(View.VISIBLE);


                }


                dbSource.close();
            }
        });
        mExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   updatePDF();
                dbSource.open();
                ArrayList<ContentValues> allPoleReadings = dbSource.getAllPreviousMeterReading(mAllMeters.get(1));
                ArrayList<ContentValues> allMainReadings = dbSource.getAllPreviousMeterReading(mAllMeters.get(0));

                try {
                    mPoleReadings = allPoleReadings.get(mPoleSpinner.getSelectedItemPosition() - 1);
                    mMainReadings = allMainReadings.get(mMainSpinner.getSelectedItemPosition() - 1);
                    mPreMainReading = allMainReadings.get(mPreMainSpinner.getSelectedItemPosition() - 1);
                    mPrePoleReading = allMainReadings.get(mPrePoleSpinner.getSelectedItemPosition() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                update();
                dbSource.close();
            }
        });

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

    private String millistoDate(long dateInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);


        return dateFormat.format(calendar.getTime());
    }

    private long dateToMills(String text) {
        long mills = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm");
        try {
            Date date = dateFormat.parse(text);
            mills = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mills;
    }

    public void update() {
        PDDocument document = null;

        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS);
            String name = getTaskId() + "Report.pdf";
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + name);
            writeBytesToFile(getAssets().open("Reading Slip.pdf"), file);
            document = PDDocument.load(file);
            PDPage page = (PDPage) document.getPage(0);
            InputStream is = getAssets().open("calibri.ttf");
            PDFont font = PDType0Font.load(document, is, true);
            PDPageContentStream contentStream = new PDPageContentStream(
                    document, page, true, true);
            PDRectangle pageRectangle = page.getMediaBox();
            contentStream.beginText();
            contentStream.setFont(font, 11);
            contentStream.newLineAtOffset(55, pageRectangle.getHeight() - 81);
            contentStream.showText(mConsumerDetails.getAsString("NAME"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(362, pageRectangle.getHeight() - 81);
            contentStream.showText(mConsumerDetails.getAsString("LOAD") + " KVA");
            contentStream.endText();


            contentStream.beginText();
            contentStream.newLineAtOffset(511, pageRectangle.getHeight() - 81);
            contentStream.showText(mMainReadings.getAsString("MONTH"));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(65, pageRectangle.getHeight() - 96);
            contentStream.showText(mConsumerDetails.getAsString("PROCESS"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(340, pageRectangle.getHeight() - 96);
            contentStream.showText(mConsumerDetails.getAsString("ACCOUNT_ID"));
            contentStream.endText();

            contentStream.setFont(font, 13);

            contentStream.beginText();
            contentStream.newLineAtOffset(120, pageRectangle.getHeight() - 132);
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_METER_NO) + "  : MF-" + mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
            contentStream.setFont(font, 11);

            int line = -1;
            float linewidth = (float) 14.6;
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            long mills=Long.parseLong(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_READING_DATE));
            contentStream.showText(millistoDate(mills));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_UC_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_UHI_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_UHI_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_CMD_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_CMD_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_BILLS).substring(0, mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_BILLS).indexOf(".")));
            contentStream.endText();
            line++;
            int segmentstart = line;
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8));
            contentStream.endText();

            Double sumOfKWHSegments = Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8));


            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText("" + sumOfKWHSegments);
            contentStream.endText();
            line = segmentstart;
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8));
            contentStream.endText();
            Double sumOfKVAHSegments = Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7)) +
                    Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8));
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText("" + sumOfKVAHSegments);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_PF));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_R));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_Y));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_B));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_R));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_Y));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_B));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(180, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString("SEAL"));
            contentStream.endText();
            line = line + 4;
            // First Line Current Month Readings
            contentStream.beginText();
            contentStream.newLineAtOffset(33, pageRectangle.getHeight() - (line * linewidth + 161));
             mills=Long.parseLong(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_READING_DATE));
            contentStream.showText(millistoDate(mills));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(125, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(175, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(225, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(265, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            contentStream.endText();
// Second Line Previous Month Readings
         line = line + 2;
            contentStream.beginText();
            contentStream.newLineAtOffset(33, pageRectangle.getHeight() - (line * linewidth + 161));
             mills=Long.parseLong(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_READING_DATE));
            contentStream.showText(millistoDate(mills));

            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(125, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(175, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(225, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(265, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            contentStream.endText();

           line++;
            Double kwhNow=Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
            Double kwhPre=Double.parseDouble(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
            Double kvahNow=Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
            Double kvahPre=Double.parseDouble(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
            Double kvarhNow=Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
            Double kvarhPre=Double.parseDouble(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
            Double ucNow=Double.parseDouble(mMainReadings.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            Double ucPre=Double.parseDouble(mPreMainReading.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            // Third Line Difference Between Current Month and Previous Month Reading
            contentStream.beginText();
            contentStream.newLineAtOffset(125, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText((kwhNow - kwhPre) + "");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(175, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText((kvahNow - kvahPre) + "");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(225, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText((kvarhNow - kvarhPre) + "");
            contentStream.endText();

            // Fourth Line MF of Main Meter
            line++;
            contentStream.beginText();
            contentStream.newLineAtOffset(125, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(175, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(225, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(265, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
          // Fifth Line Calculate Consumption and demand
            line++;
            contentStream.beginText();
            contentStream.newLineAtOffset(125, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText("" + (Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * (kwhNow - kwhPre));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(175, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText("" + (Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * (kvahNow - kvahPre));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(225, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText("" + (Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * (kvarhNow - kvarhPre));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(265, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText("" + (Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * ucNow);
            contentStream.endText();
            //Fifth Line
             line++;
            contentStream.beginText();
            contentStream.newLineAtOffset(125, pageRectangle.getHeight() - (++line * linewidth + 161));
            double avgpf=(Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF")))*(kwhNow-kwhPre)/(Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * (kvahNow - kvahPre);
            contentStream.showText(avgpf+"");
            contentStream.endText();
            /* contentStream.beginText();
            contentStream.newLineAtOffset(140, pageRectangle.getHeight() - (++line * linewidth + 150));
            contentStream.showText(mMainReadings.getAsString("Deviation From POle"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(140, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString("Observatsions"));
            contentStream.endText();*//*
*//*
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (line * linewidth + 150));
            contentStream.showText(mMainReadings.getAsString("0000"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (line * linewidth + 150));
            contentStream.showText(mMainReadings.getAsString("0000"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (line * linewidth + 150));
            contentStream.showText(mMainReadings.getAsString("0000"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (line * linewidth + 150));
            contentStream.showText(mMainReadings.getAsString("0000"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(40, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mMainReadings.getAsString("01-01-2015"));
            contentStream.endText();
       contentStream.beginText();
        contentStream.newLineAtOffset(420, pageRectangle.getHeight() - 115);
        contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_METER_NO));
        contentStream.endText();*//*

*//*TODO: *//**/
            line = -1;
            linewidth = (float) 14.6;
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            mills=Long.parseLong(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_READING_DATE));
            contentStream.showText(millistoDate(mills));

            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_UC_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_UHI_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_UHI_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_CMD_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_CMD_EXPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_BILLS).substring(0, mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_BILLS).indexOf(".")));
            contentStream.endText();
            line++;
            segmentstart = line;
            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8));
            contentStream.endText();
            sumOfKWHSegments = Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_1)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_2)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_3)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_4)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_5)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_6)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_7)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_SEGMENT_8));
            contentStream.beginText();
            contentStream.newLineAtOffset(313, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText("" + sumOfKWHSegments);
            contentStream.endText();
            line = segmentstart;
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8));
            contentStream.endText();
            sumOfKVAHSegments = Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_1)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_2)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_3)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_4)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_5)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_6)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_7)) +
                    Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_SEGMENT_8));
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText("" + sumOfKVAHSegments);
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_PF));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_R));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_Y));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_VOLTAGE_B));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_R));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_Y));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_CURRENT_B));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(453, pageRectangle.getHeight() - (++line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString("SEAL"));
            contentStream.endText();

            line = line + 4;
            // First Line Current Month Readings
            contentStream.beginText();
            contentStream.newLineAtOffset(305, pageRectangle.getHeight() - (line * linewidth + 161));
            mills=Long.parseLong(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_READING_DATE));
            contentStream.showText(millistoDate(mills));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(398, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(448, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(498, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(538, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            contentStream.endText();
// Second Line Previous Month Readings
            line = line + 2;
            contentStream.beginText();
            contentStream.newLineAtOffset(305, pageRectangle.getHeight() - (line * linewidth + 161));
            mills=Long.parseLong(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_READING_DATE));
            contentStream.showText(millistoDate(mills));

            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(398, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(448, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(498, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(538, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            contentStream.endText();

            line++;
             kwhNow=Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
             kwhPre=Double.parseDouble(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_KWH_IMPORT));
             kvahNow=Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
             kvahPre=Double.parseDouble(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_KVAH_IMPORT));
             kvarhNow=Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
             kvarhPre=Double.parseDouble(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_KVARH_IMPORT));
             ucNow=Double.parseDouble(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
             ucPre=Double.parseDouble(mPrePoleReading.getAsString(MasterDbHelper.COLUMN_NAME_UC_IMPORT));
            // Third Line Difference Between Current Month and Previous Month Reading
            contentStream.beginText();
            contentStream.newLineAtOffset(398, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText((kwhNow - kwhPre) + "");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(448, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText((kvahNow - kvahPre) + "");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(498, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText((kvarhNow - kvarhPre) + "");
            contentStream.endText();

            // Fourth Line MF of Main Meter
            line++;
            contentStream.beginText();
            contentStream.newLineAtOffset(398, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(448, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(498, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(538, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText(mConsumerDetails.getAsString("MAIN_MF"));
            contentStream.endText();
            // Fifth Line Calculate Consumption and demand
            line++;
            contentStream.beginText();
            contentStream.newLineAtOffset(398, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText("" + (Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * (kwhNow - kwhPre));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(448, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText("" + (Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * (kvahNow - kvahPre));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(498, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText("" + (Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * (kvarhNow - kvarhPre));
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(538, pageRectangle.getHeight() - (line * linewidth + 161));
            contentStream.showText("" + (Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * ucNow);
            contentStream.endText();
            //Fifth Line
            line++;
            contentStream.beginText();
            contentStream.newLineAtOffset(398, pageRectangle.getHeight() - (++line * linewidth + 161));
             avgpf=(Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF")))*(kwhNow-kwhPre)/(Double.parseDouble(mConsumerDetails.getAsString("MAIN_MF"))) * (kvahNow - kvahPre);
            contentStream.showText(avgpf+"");
            contentStream.endText();
            contentStream.setFont(font, 13);
            contentStream.beginText();
            contentStream.newLineAtOffset(413, pageRectangle.getHeight() - 132);
            contentStream.showText(mPoleReadings.getAsString(MasterDbHelper.COLUMN_NAME_METER_NO) + "  : MF-" + mConsumerDetails.getAsString("POLE_MF"));
            contentStream.endText();
            contentStream.setFont(font, 11);


            contentStream.close();
            document.save(file);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e.getMessage());
        }


    }

}
