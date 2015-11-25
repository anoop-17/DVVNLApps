package com.uppcl.dvvnl.combingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int SELECT_POLE_CODE = 1;
    public static String POLE_CODE_VALUE;
    FragmentManager fragmentManager;
    private Button mPickPoleCode;
    private Button mSearchByAccountId;
    private Button mSearchByMeterNo;
    private TextView mPoleCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write Below Your Code
//exportAsPDF();

        fragmentManager = getSupportFragmentManager();

        mPickPoleCode = (Button) findViewById(R.id.pole_button);
        mSearchByAccountId = (Button) findViewById(R.id.bt_search_by_id);
        mSearchByMeterNo = (Button) findViewById(R.id.bt_search_by_meter);
        mPoleCode = (TextView) findViewById(R.id.pole_textview);
        mPickPoleCode.setVisibility(View.GONE);
        mPoleCode.setVisibility(View.GONE);
        mSearchByMeterNo.setVisibility(View.GONE);
        mSearchByAccountId.setVisibility(View.GONE);
        mPickPoleCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SelectPoleActivity.class);
                startActivityForResult(i, SELECT_POLE_CODE);


            }
        });


        // Do not write any things after this in this method
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.e("OnCreate", "");
    }

    public static void clearTheFile(String fileName) {
        try {
            FileWriter fwOb = new FileWriter(fileName, false);
            PrintWriter pwOb = new PrintWriter(fwOb, false);
            pwOb.flush();
            pwOb.close();
            fwOb.close();
        } catch (IOException e) {
            e.printStackTrace();
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





    public void exportAsPDF() {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Report.pdf");

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);

        // pageRectangle can be used to get the page width and height
        PDRectangle pageRectangle = page.getMediaBox();

// Create a new font object selecting one of the PDF base fonts


        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            inputStream = getAssets().open("Reading Slip.pdf");
            document.addPage(page);

            InputStream is = getAssets().open("calibri.ttf");
            PDFont font = PDType0Font.load(document, is, true);


            PDPageContentStream cos = new PDPageContentStream(document, page);
            int line = 0;
            cos.beginText();
            cos.setFont(font, 20);
            cos.newLineAtOffset(100, pageRectangle.getHeight() - 35 * (++line));
            cos.showText("ELECTRICITY URBAN TEST DIVISION ALIGARH");
            cos.endText();
            cos.beginText();
            cos.setFont(font, 12);
            cos.newLineAtOffset(150, pageRectangle.getHeight() - 45);
            cos.showText("CONSUMER METER READING STATEMENT");
            cos.endText();

            cos.close();
            document.save(file);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        Log.e("Backpressed", "");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("OnCreateOptionmenu", "");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e("OnOptionItem Seleccted", "");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.combing) {
            /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_content, importMasterFragment);
            fragmentTransaction.commit();*/

        } else if (id == R.id.ht_reading) {
            Intent i = new Intent(getApplicationContext(), HTMeteringActivity.class);
            startActivity(i);

        } else if (id == R.id.import_master) {

            /**/
            Intent i = new Intent(getApplicationContext(), ImportActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.query_builder) {

            Intent i = new Intent(getApplicationContext(), QueryManagerActivity.class);
            startActivity(i);

        } else if (id == R.id.import_gis) {


        }

        Log.e("On NavItemSelected", "" + id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.e("Act_Res Request Code "+requestCode, "Result"+resultCode);
        //   importMasterFragment.onCreate( );

        if (requestCode == SELECT_POLE_CODE) {


            mPoleCode.setText(POLE_CODE_VALUE);


        }
    }


    public class ElectricalAssets {
        private Date installationDate;
        private String purchaseOrderNo;
        private Date commisioningDate;
        private String gisID;
        private String divisionCode;
        private String substationName;
        private String feederName;
        private String owner;
    }

    public class Pole extends ElectricalAssets {
        private int height;
        private String type;
        private String structure;
        private String paintcode;

    }

    public class DistribitionTransformer extends ElectricalAssets {
        private String DTRating;
        private String location;
        private String MF;
        private String meterNo;
        private String modemNo;
        private String meterMake;
        private String oilevel;
        private String silicaGetStatus;
        private String windingTemp;
        private String oilTemp;
        private String insulationStatus;
        private String getInsulationStatus;
        private String earthingStatus;
        private String meterStatus;
        private String modemStatus;

    }

}
