package com.example.menpa.billingapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    FloatingActionButton fb_main_history;
    Button m_cart,m_add;
    FloatingActionButton btn_history;
    static ListView m_list;
    SearchView main_sv;
    static ArrayList <mainbean> allproduct,original;
    long back;
    long TIME_DELAY=1500;
    static allquery dbobj;
    static mainadapter adapter;
    static boolean isstorageper;
    private static final int PERMISSION_REQUEST_CODE = 200;
    static Context context;


    Toolbar toolbar;


    static public void refreshmain()
    {
        allproduct=dbobj.select();
        adapter = new mainadapter((Activity) context,allproduct);
        m_list.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        context = MainActivity.this;
        dbobj = new allquery(this);
        allproduct = dbobj.select();
        m_cart = findViewById(R.id.main_btn_cart);
        m_add = findViewById(R.id.main_btn_additem);
        m_list = findViewById(R.id.main_list);
        btn_history=findViewById(R.id.main_fb_history);
        main_sv = findViewById(R.id.main_sv);
        m_list.setTextFilterEnabled(true);

        main_sv.setIconifiedByDefault(false);
        main_sv.setOnQueryTextListener(this);
        main_sv.setQueryHint("Search Here");

        adapter = new mainadapter(this, allproduct);
        m_list.setAdapter(adapter);

        m_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, cart.class);
                startActivity(i);
            }
        });


        m_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, addproduct.class);
                startActivity(i);
            }
        });

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);


        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,history.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (back + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press Once Again To Exit.!", Toast.LENGTH_SHORT).show();
        }
        back = System.currentTimeMillis();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (main_sv != null) {
            main_sv.setQuery("", false);
            main_sv.clearFocus();
            main_sv.onActionViewCollapsed();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

            if(TextUtils.isEmpty(s))
            {
                m_list.clearTextFilter();
            }
            if(s.length() == 0){
                adapter = new mainadapter(this,allproduct);

                m_list.setAdapter(adapter);
            }
            else{
                m_list.setFilterText(s);
            }





        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        isstorageper=false;
        if (!isstorageper)
        {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        isstorageper=grantResults[0]==PackageManager.PERMISSION_GRANTED;

    }
}

