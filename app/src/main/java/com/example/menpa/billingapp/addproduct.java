package com.example.menpa.billingapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addproduct extends AppCompatActivity {

    Button a_home,a_cart,a_add,a_view;
    EditText et_name,et_weight,et_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);


        init();


        a_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(addproduct.this,cart.class);
                startActivity(i);
            }
        });


        a_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(addproduct.this,MainActivity.class);
                startActivity(i);
            }
        });


        a_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                allquery queryobj=new allquery(addproduct.this);
                String name=et_name.getText().toString().trim();
                String weight=et_weight.getText().toString().trim();
                String strprice=et_price.getText().toString().trim();
                int price=0;


                Boolean flag=true;
                if (name.equalsIgnoreCase(""))
                {
                    flag=false;
                    et_name.setError("Enter Product name");
                }
                if (weight.equalsIgnoreCase(""))
                {
                    flag=false;
                    et_weight.setError("Enter Product weight");
                }
                if (strprice.equalsIgnoreCase(""))
                {
                    flag=false;

                    et_price.setError("Enter Product price");
                }
                if (flag==true)
                {

                    Boolean isInserted=queryobj.insert(name,weight,strprice);
                    if(isInserted == true)
                        Toast.makeText(addproduct.this,"Data Inserted",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(addproduct.this,"Data not Inserted",Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    void init()
    {
        a_home=findViewById(R.id.add_btn_home);
        a_cart=findViewById(R.id.add_btn_cart);
        a_add=findViewById(R.id.add_btn_add);
        et_name=findViewById(R.id.add_et_name);
        et_weight=findViewById(R.id.add_et_weight);
        et_price=findViewById(R.id.add_et_price);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.refreshmain();
    }
}
