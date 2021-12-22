package com.example.menpa.billingapp;

public class custdetalis_bean {

    int refcol;
    int custid;
    int itemid;
    int itemquantity;
    String itemname;
    String itemweight;
    String itemdate;
    int itemprice;
    int itemtotal;


    public String getItemdate() {
        return itemdate;
    }

    public void setItemdate(String itemdate) {
        this.itemdate = itemdate;
    }



    public int getRefcol() {
        return refcol;
    }

    public void setRefcol(int refcol) {
        this.refcol = refcol;
    }

    public int getCustid() {
        return custid;
    }

    public void setCustid(int custid) {
        this.custid = custid;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(int itemquantity) {
        this.itemquantity = itemquantity;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemweight() {
        return itemweight;
    }

    public void setItemweight(String itemweight) {
        this.itemweight = itemweight;
    }

    public int getItemprice() {
        return itemprice;
    }

    public void setItemprice(int itemprice) {
        this.itemprice = itemprice;
    }

    public int getItemtotal() {
        return itemtotal;
    }

    public void setItemtotal(int itemtotal) {
        this.itemtotal = itemtotal;
    }
}
