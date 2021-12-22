package com.example.menpa.billingapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class cart extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Button c_home,c_add,btn_submit;
    SearchView sv_cart;
    private static final int PERMISSION_REQUEST_CODE = 200;
    static TextView tv_total;
    static ListView c_list;
    static ArrayList<cartbean> cartitems;
    static allquery cartdbobj;
    static int carttotal;
    static int itemprice,itemquantity,itemtotal,arraysize;
    static Context context;
    static cartadapter adapter;
    String custnumstr;
    String custname;
    long custnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context=cart.this;
        init();



        //strict mode is for intent to create intent chooser to open pdf file
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());




        refreshcart();



        c_list.setTextFilterEnabled(true);

        sv_cart.setIconifiedByDefault(false);
        sv_cart.setOnQueryTextListener(this);
        sv_cart.setSubmitButtonEnabled(true);
        sv_cart.setQueryHint("Search Here");



        c_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(cart.this,addproduct.class);
                startActivity(i);
            }
        });


        c_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(cart.this,MainActivity.class);
                startActivity(i);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {


                customdialogbox custdialog=new customdialogbox(cart.this);

                custdialog.show();
                }
        });



    }

    static public void maketotal()
    {

        arraysize=cartitems.size();
        int i=0;
        carttotal=0;
        itemprice=0;
        itemquantity=0;
        itemtotal=0;

        while (i<arraysize)
        {
            itemprice=Integer.parseInt(cartitems.get(i).getCartitemprice()+"");
            itemquantity=Integer.parseInt(cartitems.get(i).getCartitemquantity()+"");
            itemtotal=itemprice*itemquantity;
            carttotal=carttotal+itemtotal;
            i++;
        }

        tv_total.setText("Total :- "+carttotal+"");

    }

    static public void refreshcart()
    {
        cartdbobj=new allquery(context);
        cartitems=cartdbobj.selectcart();
        adapter=new cartadapter((Activity) context,cartitems);

        c_list.setAdapter(adapter);
        maketotal();
    }

    void init()
    {
        sv_cart=findViewById(R.id.sv_cart);
        c_home=findViewById(R.id.cart_btn_home);
        c_add=findViewById(R.id.cart_btn_additem);
        c_list=findViewById(R.id.cart_list);
        btn_submit=findViewById(R.id.cart_btn_submit);
        tv_total=findViewById(R.id.cart_tv_total);
    }





    class customdialogbox extends Dialog {

        EditText et_custname,et_custmob;
        Button btn_cancel,btn_generatebill;

        public customdialogbox(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.cart_custom_dialogbox);

            et_custmob=findViewById(R.id.cart_custdialog_et_custmob);
            et_custname=findViewById(R.id.cart_custdialog_et_custname);
            btn_cancel=findViewById(R.id.cart_custdialog_btn_cancel);
            btn_generatebill=findViewById(R.id.cart_custdialog_btn_Generatebill);






            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });




            btn_generatebill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    custname=et_custname.getText().toString().trim();
                    custnumstr=et_custmob.getText().toString().trim();


                    if (!MainActivity.isstorageper)
                    {
                        ActivityCompat.requestPermissions(cart.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

                    }
                    Boolean flag=true;
                    if (custname.equalsIgnoreCase(""))
                    {
                        flag=false;
                        et_custname.setError("Enter Customer name");
                    }
                    if (custnumstr.equalsIgnoreCase(""))
                    {
                        flag=false;
                        et_custmob.setError("Enter Customer mobile number");
                    }

                    if(!Pattern.matches("[a-zA-Z]+", custnumstr)) {
                        if(custnumstr.length() < 6 || custnumstr.length() > 13) {
                            // if(phone.length() != 10) {
                            flag = false;
                            et_custmob.setError("Not Valid Number");
                        } else {
                            flag = true;
                        }
                    } else {
                        flag=false;
                    }





                    if (flag==true && MainActivity.isstorageper)
                    {

                        custnum=Long.parseLong(custnumstr);

                        int custid=cartdbobj.addcust(custname,carttotal,custnum);

                        java.text.SimpleDateFormat todaysdate=new java.text.SimpleDateFormat("dd/MM/yy");

                        String todaystr=todaysdate.format(new Date());


                        for (int i=0;i<cartitems.size();i++)
                        {
                            int itemid,itemquantity;
                            itemid=Integer.parseInt(cartitems.get(i).getCartitemid());
                            itemquantity=Integer.parseInt(cartitems.get(i).getCartitemquantity());
                            cartdbobj.insertintocustitems(custid,itemid,itemquantity,todaystr);
                        }


                        File makedirectory=new File(Environment.getExternalStorageDirectory()+"/BillingApp/");

                        if(makedirectory.exists()==false)
                        {
                            makedirectory.mkdir();
                        }

                        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");



                        String date=sdf.format(new Date());

                        String filename="bill_"+custname+"_"+date+".pdf";

                        String path=Environment.getExternalStorageDirectory()+"/BillingApp/"+filename;


                        createPdf(path);


                        File filepath=new File(path);


                        Intent myIntent = new Intent(Intent.ACTION_VIEW);
                        myIntent.setDataAndType(Uri.fromFile(filepath),"application/pdf");
                        Intent j = Intent.createChooser(myIntent, "Choose an application to open with:");
                        startActivity(j);


                        //Toast.makeText(context, "Bill pdf created at :- Internal storage/BillingApp/"+filename, Toast.LENGTH_LONG).show();

                        cartdbobj.deleteallfromcart();
                        refreshcart();
                        dismiss();
                    }



                }
            });


        }

    }

    public void createPdf(String path) {


        try {


            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Biling");
            document.addCreator("Billing App");

            Font extbold = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);


            LineSeparator newline = new LineSeparator();
            newline.setLineColor(new BaseColor(0, 0, 0, 0));




            Chunk chunk_title = new Chunk("NP MEENA COLORS",extbold);
            Paragraph para_title = new Paragraph(chunk_title);
            para_title.setAlignment(Element.ALIGN_CENTER);
            document.add(para_title);

            document.add(new Paragraph(""));
            document.add(new Chunk(newline));
            document.add(new Paragraph(""));
            document.add(new Chunk(newline));
            document.add(new Paragraph(""));



            Chunk mOrderDetailsTitleChunk = new Chunk("ORDER DETAILS");
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);

            // Fields of Order Details...
            // Adding Chunks for Title and value

            Chunk chunk_custname = new Chunk("Customer Name:"+custname);
            Paragraph para_custname = new Paragraph(chunk_custname);
            document.add(para_custname);

            Chunk chunk_custnum = new Chunk("Customer Mobile Number:"+custnum);
            Paragraph para_custnum = new Paragraph(chunk_custnum);
            document.add(para_custnum);



            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(new Chunk(newline));
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);




            PdfPTable table = new PdfPTable(6); // 3 columns.

            PdfPCell cell1 = new PdfPCell(new Paragraph("Item No"));
            PdfPCell cell2 = new PdfPCell(new Paragraph("Item Name"));
            PdfPCell cell3 = new PdfPCell(new Paragraph("Item Weight"));
            PdfPCell cell4 = new PdfPCell(new Paragraph("Item Price"));
            PdfPCell cell5 = new PdfPCell(new Paragraph("Item Quantity"));
            PdfPCell cell6 = new PdfPCell(new Paragraph("Item Total"));


            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);


            cell1.setBorderWidth      (3f);
            cell2.setBorderWidth      (3f);
            cell3.setBorderWidth      (3f);
            cell4.setBorderWidth      (3f);
            cell5.setBorderWidth      (3f);
            cell6.setBorderWidth      (3f);



            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);



            for (int i=0;i<cart.arraysize;i++) {

                int total=Integer.parseInt(cartitems.get(i).getCartitemprice())*Integer.parseInt(cartitems.get(i).getCartitemquantity());
                table.addCell(new PdfPCell(new Paragraph((i+1)+"")));
                table.addCell(new PdfPCell(new Paragraph(cartitems.get(i).getCartitemname()+"")));
                table.addCell(new PdfPCell(new Paragraph(""+cartitems.get(i).getCartitemweight())));
                table.addCell(new PdfPCell(new Paragraph(""+cartitems.get(i).getCartitemprice())));
                table.addCell(new PdfPCell(new Paragraph(""+cartitems.get(i).getCartitemquantity())));
                table.addCell(new PdfPCell(new Paragraph(""+total)));

            }


            PdfPCell celltotal = new PdfPCell(new Paragraph("Total amount :- "+cart.carttotal,boldFont));
            celltotal.setColspan(6);
            table.addCell(celltotal);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            document.add(table);


            document.close();






        } catch (IOException | DocumentException ie) {

            Toast.makeText(context, "createPdf: Error " + ie.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }






    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        MainActivity.isstorageper=grantResults[0]==PackageManager.PERMISSION_GRANTED;

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sv_cart != null) {
            sv_cart.setQuery("", false);
            sv_cart.clearFocus();
            sv_cart.onActionViewCollapsed();
        }

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {



        if(newText.length() == 0){
            adapter = new cartadapter((Activity) context,cartitems);

            c_list.setAdapter(adapter);
        }
        else{
            c_list.setFilterText(newText);
        }


        return true;
    }


}
