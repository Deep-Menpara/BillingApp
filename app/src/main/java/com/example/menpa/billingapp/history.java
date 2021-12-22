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
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.menpa.billingapp.MainActivity.m_list;

public class history extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView hist_list;
    allquery db;
    ArrayList<customerbean> allcustomers;
    history_adapter histadapt;
    SearchView history_sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        hist_list=findViewById(R.id.hist_list);
        db=new allquery(history.this);
        allcustomers=db.getcustlist();
        histadapt=new history_adapter(history.this,allcustomers);
        hist_list.setAdapter(histadapt);
        hist_list.setTextFilterEnabled(true);


        history_sv=findViewById(R.id.history_sv);
        history_sv.setIconifiedByDefault(false);
        history_sv.setOnQueryTextListener(this);
        history_sv.setQueryHint("Search Here");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (history_sv != null) {
            history_sv.setQuery("", false);
            history_sv.clearFocus();
            history_sv.onActionViewCollapsed();
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        if(TextUtils.isEmpty(newText))
        {
            hist_list.clearTextFilter();
        }
        if(newText.length() == 0){
            histadapt = new history_adapter(this,allcustomers);

            hist_list.setAdapter(histadapt);
        }
        else{
            hist_list.setFilterText(newText);
        }

        return true;
    }
}
