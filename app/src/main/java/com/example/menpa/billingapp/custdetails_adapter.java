package com.example.menpa.billingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class custdetails_adapter extends BaseAdapter {

    Activity context;
    ArrayList<custdetalis_bean> allcustitems;

    custdetails_adapter(Activity context,ArrayList<custdetalis_bean> allcustitems)
    {
        this.context=context;
        this.allcustitems=allcustitems;
    }



    @Override
    public int getCount() {
        return allcustitems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return allcustitems.get(position).getRefcol();
    }

    class ViewHolder
    {
        TextView tv_name;
        TextView tv_weight;
        TextView tv_price;
        TextView tv_quantity;
        TextView tv_itemtotal;
        TextView tv_itemdate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder controls;
        LayoutInflater inflater=context.getLayoutInflater();

        if (convertView==null)
        {
            controls=new ViewHolder();
            convertView=inflater.inflate(R.layout.custdetails_listitems,parent,false);

            controls.tv_itemtotal=convertView.findViewById(R.id.custdetails_tv_itemtotal);
            controls.tv_name=convertView.findViewById(R.id.custdetails_tv_itemname);
            controls.tv_weight=convertView.findViewById(R.id.custdetails_tv_itemweight);
            controls.tv_price=convertView.findViewById(R.id.custdetails_tv_itemprice);
            controls.tv_quantity=convertView.findViewById(R.id.custdetails_tv_itemquantity);
            controls.tv_itemdate=convertView.findViewById(R.id.custdetails_tv_itemdate);

            convertView.setTag(controls);


        }
        else
        {
            controls= (ViewHolder) convertView.getTag();
        }


        controls.tv_name.setText("NAME :- "+allcustitems.get(position).getItemname()+"");
        controls.tv_weight.setText("WEIGHT :- "+allcustitems.get(position).getItemweight()+" g");
        controls.tv_price.setText("PRICE :- "+allcustitems.get(position).getItemprice()+"rs");
        controls.tv_quantity.setText("QUANTITY :- "+allcustitems.get(position).getItemquantity()+"");
        controls.tv_itemtotal.setText("ITEM TOTAL :- "+allcustitems.get(position).getItemtotal()+"rs");
        controls.tv_itemdate.setText(""+allcustitems.get(position).getItemdate());


        return convertView;
    }
}
