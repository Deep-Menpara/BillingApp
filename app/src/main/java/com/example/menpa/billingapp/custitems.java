package com.example.menpa.billingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class custitems extends AppCompatActivity {


    ListView custitems_lv;
    custdetails_adapter custdetadapter;
    ArrayList<custdetalis_bean> allcustitems;
    allquery db;
    int custid;
    TextView tv_custname,tv_custmob,tv_total;
    customerbean custdetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custitems);



        tv_custname=findViewById(R.id.custitems_tv_custname);
        tv_custmob=findViewById(R.id.custitems_tv_custmob);
        tv_total=findViewById(R.id.custitems_tv_total);
        custid=getIntent().getIntExtra("custid",20);


        custitems_lv=findViewById(R.id.custdetails_listview);
        db=new allquery(custitems.this);

        allcustitems=db.getcustdetails(custid);
        custdetadapter=new custdetails_adapter(custitems.this,allcustitems);
        custitems_lv.setAdapter(custdetadapter);
        custdetails=new customerbean();
        custdetails=db.getspificcust(custid);


        tv_total.setText("TOTAL :- "+custdetails.getCusttotal()+"");
        tv_custmob.setText(custdetails.getCustmob()+"");
        tv_custname.setText(custdetails.getCustname()+"");
    }
}
