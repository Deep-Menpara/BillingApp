package com.example.menpa.billingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class allquery extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Billingapp.db";
    public static final String TABLE_NAME = "product";
    public static final String TABLE_NAME_cart = "cartitems";
    public static final String COL_1 = "productid";
    public static final String COL_2 = "productname";
    public static final String COL_3 = "productweight";
    public static final String COL_4 = "productprice";
    public static final String cart_col1 = "billid";
    public static final String cart_col2 = "itemid";
    public static final String cart_col3 = "itemname";
    public static final String cart_col4 = "itemweight";
    public static final String cart_col5 = "itemprice";
    public static final String cart_col6 = "itemquantity";


    Context context;

    public allquery(Context context) {


        super(context, DATABASE_NAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createproduct="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "    productid     INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    productname   VARCHAR (30),\n" +
                "    productweight VARCHAR (30),\n" +
                "    productprice  VARCHAR (30)\n" +
                ");";
        db.execSQL(createproduct);

        String createcart="CREATE TABLE IF NOT EXISTS cartitems (\n" +
                "    billid     INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    itemid VARCHAR (30),\n" +
                "    itemname   VARCHAR (30),\n" +
                "    itemweight VARCHAR (30),\n" +
                "    itemquantity VARCHAR (10),\n" +
                "    itemprice  VARCHAR (30)\n" +


                ");";
        db.execSQL(createcart);

        String createcustomer="create table if not exists customer(custid INTEGER PRIMARY KEY AUTOINCREMENT,custname VARCHAR (30),custmob INTEGER(20),custtotal INTEGER(10));";
        db.execSQL(createcustomer);

        String createcustitems="create table if not exists custitems(refid INTEGER PRIMARY KEY AUTOINCREMENT,custid INTEGER (15),itemid INTEGER(10),itemquantity INTEGER(10),itemtotal INTEGER(10),itemdate VARCHAR(20));";
        db.execSQL(createcustitems);



        //String ins1="INSERT INTO product(productname,productweight,productprice) VALUES ('"+name+"','"+weight+\"','\"+price+\"')";

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_cart);
        onCreate(db);

    }

   /* public String insert(String name,String weight,String price)
    {

        *//*ContentValues value=new ContentValues();


        value.put("productname",name);
        value.put("productweight",weight);
        value.put("productprice",price);


        Boolean result;

        result = db.insert("product",null,value)>0;*//*


        String insquery="INSERT INTO product(productname,productweight,productprice) VALUES ('"+"name"+"','"+weight+"','"+price+"')";

        db.execSQL(insquery);

        return "insert executed";

    }*/

    public boolean insert(String name,String weight,String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,weight);
        contentValues.put(COL_4,price);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<mainbean> select()
    {
        ArrayList <mainbean> allproducts=new ArrayList<mainbean>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        if (res.moveToFirst())
        {
            do {

                mainbean mb=new mainbean();
                mb.setProid(res.getInt(res.getColumnIndex("productid")));
                mb.setProname(res.getString(res.getColumnIndex("productname")));
                mb.setProweight(res.getString(res.getColumnIndex("productweight")));
                mb.setProprice(res.getString(res.getColumnIndex("productprice")));

                allproducts.add(mb);

            }while (res.moveToNext());
        }

        return allproducts;
    }


    public void insertincart(String id,String name,String weight,String price,String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cart_col2,id);
        contentValues.put(cart_col3,name);
        contentValues.put(cart_col4,weight);
        contentValues.put(cart_col5,price);
        contentValues.put(cart_col6,quantity);
        long result = db.insert(TABLE_NAME_cart,null ,contentValues);

    }


    public ArrayList<cartbean> selectcart()
    {
        ArrayList <cartbean> cartitems=new ArrayList<cartbean>();
        SQLiteDatabase dbsel = this.getWritableDatabase();
        Cursor rescart = dbsel.rawQuery("select * from cartitems",null);
        if (rescart.moveToFirst())
        {
            do {

                cartbean cb=new cartbean();
                cb.setCartbillid(rescart.getInt(rescart.getColumnIndex(cart_col1)));
                cb.setCartitemid(rescart.getString(rescart.getColumnIndex(cart_col2)));
                cb.setCartitemname(rescart.getString(rescart.getColumnIndex(cart_col3)));
                cb.setCartitemweight(rescart.getString(rescart.getColumnIndex(cart_col4)));
                cb.setCartitemprice(rescart.getString(rescart.getColumnIndex(cart_col5)));
                cb.setCartitemquantity(rescart.getString(rescart.getColumnIndex(cart_col6)));

                cartitems.add(cb);

            }while (rescart.moveToNext());
        }

        return cartitems;
    }

    public Boolean checkitemincart(String id,String quantity)
    {
        Boolean ispresent=false;
        ArrayList <cartbean> cartitems=new ArrayList<cartbean>();
        int pos;
        int size;
        int intquantity=Integer.parseInt(quantity);
        SQLiteDatabase db = this.getWritableDatabase();

        cartitems.addAll(selectcart());

        size=cartitems.size();

        for (pos=0;pos<size;pos++)
        {
            if (id.equalsIgnoreCase(cartitems.get(pos).getCartitemid()+""))
            {
                intquantity=intquantity+Integer.parseInt(cartitems.get(pos).getCartitemquantity()+"");
                quantity=intquantity+"";
                String updquery="UPDATE cartitems set itemquantity='"+quantity+"' where itemid='"+id+"'";
                db.execSQL(updquery);
                ispresent=true;
            }

        }



        return ispresent;
    }


    public void delete(int billid)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String delquery="delete from "+TABLE_NAME_cart+" where "+cart_col1+"="+billid+";";
        db.execSQL(delquery);
        //return db.delete(TABLE_NAME, "item = "+name,new String[] {name});

    }

    public void deleteallfromcart()
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String delquery="delete from "+TABLE_NAME_cart+";";
        db.execSQL(delquery);
        //return db.delete(TABLE_NAME, "item = "+name,new String[] {name});

    }
    public void deleteitemfromhome(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String delquery="delete from "+TABLE_NAME+" where "+COL_1+"="+id+";";
        db.execSQL(delquery);
    }

    public int addcust(String custname,int total,long custmob)
    {

        int custid=0;
        SQLiteDatabase db = this.getWritableDatabase();
        String selquery="select * from customer where custmob="+custmob+";";
        Cursor res=db.rawQuery(selquery,null);
        if (res.moveToFirst())
        {
            int finaltotal=res.getInt(res.getColumnIndex("custtotal"));
            finaltotal+=total;
            String update="update customer set custtotal="+finaltotal+" where custmob="+custmob+" ;";
            db.execSQL(update);
        }
        else
        {
            ContentValues contentValues=new ContentValues();
            contentValues.put("custname",custname);
            contentValues.put("custtotal",total);
            contentValues.put("custmob",custmob);
            long result=db.insert("customer",null,contentValues);
        }

        Cursor getcustid=db.rawQuery("select * from customer where custmob="+custmob+";",null);
        if (getcustid.moveToFirst())
        {
            custid=getcustid.getInt(getcustid.getColumnIndex("custid"));
        }

        return custid;

    }

    public ArrayList<customerbean> getcustlist()
    {
        ArrayList<customerbean> allcustomers=new ArrayList<customerbean>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from customer;",null);

        while (res.moveToNext())
        {
            customerbean custbean=new customerbean();

            custbean.setCustid(res.getInt(res.getColumnIndex("custid")));
            custbean.setCustname(res.getString(res.getColumnIndex("custname")));
            custbean.setCustmob(res.getLong(res.getColumnIndex("custmob")));
            custbean.setCusttotal(res.getInt(res.getColumnIndex("custtotal")));

            allcustomers.add(custbean);
        }
        return allcustomers;
    }

    public void insertintocustitems(int custid,int itemid,int itemquantity,String itemdate)
    {
        int itemtotal,itemprice=1;
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from product where productid="+itemid+";",null);
        if (res.moveToFirst())
        {
            itemprice=Integer.parseInt(res.getString(res.getColumnIndex("productprice")));
        }
        itemtotal=itemprice*itemquantity;

        ContentValues contentValues=new ContentValues();
        contentValues.put("custid",custid);
        contentValues.put("itemid",itemid);
        contentValues.put("itemquantity",itemquantity);
        contentValues.put("itemtotal",itemtotal);
        contentValues.put("itemdate",itemdate);
        long id=db.insert("custitems",null,contentValues);


    }

    public ArrayList<custdetalis_bean> getcustdetails(int custid)
    {
        ArrayList<custdetalis_bean> allcustitems=new ArrayList<custdetalis_bean>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor res_custitems=db.rawQuery("select * from custitems where custid="+custid+";",null);
        while (res_custitems.moveToNext())
        {
            custdetalis_bean bean=new custdetalis_bean();
            bean.setRefcol(res_custitems.getInt(res_custitems.getColumnIndex("refid")));
            bean.setCustid(custid);
            bean.setItemdate(res_custitems.getString(res_custitems.getColumnIndex("itemdate")));
            bean.setItemid(res_custitems.getInt(res_custitems.getColumnIndex("itemid")));
            bean.setItemquantity(res_custitems.getInt(res_custitems.getColumnIndex("itemquantity")));
            Cursor res_itemdet=db.rawQuery("select * from product where productid="+bean.getItemid()+";",null);
            if (res_itemdet.moveToNext())
            {
                bean.setItemname(res_itemdet.getString(res_itemdet.getColumnIndex("productname")));
                bean.setItemweight(res_itemdet.getString(res_itemdet.getColumnIndex("productweight")));
                bean.setItemprice(Integer.parseInt(res_itemdet.getString(res_itemdet.getColumnIndex("productprice"))));
                int itemtotal=bean.getItemprice()*bean.getItemquantity();
                bean.setItemtotal(itemtotal);
            }
            allcustitems.add(bean);
        }

        return allcustitems;
    }



    public customerbean getspificcust(int custid)
    {
        customerbean specificcustdetails=new customerbean();
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from customer where custid="+custid+";",null);
        if (res.moveToNext())
        {
            specificcustdetails.setCustid(res.getInt(res.getColumnIndex("custid")));
            specificcustdetails.setCustname(res.getString(res.getColumnIndex("custname")));
            specificcustdetails.setCustmob(res.getLong(res.getColumnIndex("custmob")));
            specificcustdetails.setCusttotal(res.getInt(res.getColumnIndex("custtotal")));
        }
        return specificcustdetails;
    }



}
