package com.example.menpa.billingapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;

public class mainadapter extends BaseAdapter implements Filterable {

    ArrayList <mainbean> allproducts,orig;
    Activity context;
    String id;
    String name;
    String weight;
    String price;
    String quantity;
    allquery dbobj;

    public mainadapter(Activity context,ArrayList <mainbean> allproducts)
    {
        this.context=context;
        this.allproducts=allproducts;
        dbobj=new allquery(context);
    }

    @Override
    public int getCount() {
        return allproducts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return allproducts.get(position).getProid();
    }

    @Override
    public Filter getFilter() {

        Toast.makeText(context, "inside filter", Toast.LENGTH_SHORT).show();
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint1) {
                String constraint = constraint1.toString();
                final FilterResults oReturn = new FilterResults();
                final ArrayList<mainbean> results = new ArrayList<mainbean>();
                if (orig == null)
                    orig = allproducts;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final mainbean g : orig) {
                            if (g.getProname().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                if(constraint == null) {
                    oReturn.values = orig;
                }
                    return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                allproducts = (ArrayList<mainbean>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class viewholder
    {
        TextView tv_name;
        TextView tv_weight;
        TextView tv_price;
        Button btn_addtocart;
        NumberPicker np_quantity;
        Button btn_remove;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final viewholder listcontrols;

        LayoutInflater inflater=context.getLayoutInflater();

        if (convertView==null)
        {
            convertView=inflater.inflate(R.layout.home_listitems,null);

            listcontrols=new viewholder();

            listcontrols.tv_name=convertView.findViewById(R.id.homelist_tv_name);
            listcontrols.tv_weight=convertView.findViewById(R.id.homelist_tv_weight);
            listcontrols.tv_price=convertView.findViewById(R.id.homelist_tv_price);
            listcontrols.btn_addtocart=convertView.findViewById(R.id.homelist_but_addtocart);
            listcontrols.np_quantity=convertView.findViewById(R.id.homelist_np_quantity);
            listcontrols.btn_remove=convertView.findViewById(R.id.hme_btn_remove);

            convertView.setTag(listcontrols);
        }
        else
        {
            listcontrols=(viewholder)convertView.getTag();
        }

        listcontrols.tv_name.setText("NAME :- "+allproducts.get(position).getProname() + "");
        listcontrols.tv_weight.setText("WEIGHT :- "+allproducts.get(position).getProweight() + " g");
        listcontrols.tv_price.setText("MRP :- "+allproducts.get(position).getProprice() + "rs");







        listcontrols.btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=allproducts.get(position).getProid() + "";
                quantity =listcontrols.np_quantity.getValue()+"";


                Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();

                dbobj=new allquery(context);

                Boolean ispresent;
                ispresent=dbobj.checkitemincart(id,quantity);


                if(ispresent)
                {
                }
                else
                {



                allquery insertcart=new allquery(context);


                name=allproducts.get(position).getProname() + "";
                weight=allproducts.get(position).getProweight() + "";
                price=allproducts.get(position).getProprice() + "";



                insertcart.insertincart(id,name,weight,price, quantity);

                }

            }
        });


        listcontrols.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int id=allproducts.get(position).getProid();

                dbobj.deleteitemfromhome(id);

                MainActivity.refreshmain();


            }
        });

        return convertView;
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }
}
