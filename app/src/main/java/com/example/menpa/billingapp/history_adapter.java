package com.example.menpa.billingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class history_adapter extends BaseAdapter implements Filterable {

    ArrayList<customerbean> allcustomer,orig;
    Activity context;

    history_adapter(Activity context,ArrayList<customerbean> allcustomer)
    {
        this.allcustomer=allcustomer;
        this.context=context;
    }



    @Override
    public int getCount() {
        return allcustomer.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return allcustomer.get(position).getCustid();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint1) {
                String constraint = constraint1.toString();
                final FilterResults oReturn = new FilterResults();
                final ArrayList<customerbean> results = new ArrayList<customerbean>();
                if (orig == null)
                    orig = allcustomer;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final customerbean g : orig) {
                            if (g.getCustname().toLowerCase()
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
                allcustomer = (ArrayList<customerbean>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    class ViewHolder
    {
        TextView tv_custname;
        TextView tv_custmob;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder controls;
        LayoutInflater inflater=context.getLayoutInflater();

        if (convertView==null)
        {
            controls=new ViewHolder();
            convertView=inflater.inflate(R.layout.history_customlistitem,parent,false);

            controls.tv_custname=convertView.findViewById(R.id.historylist_tv_custname);
            controls.tv_custmob=convertView.findViewById(R.id.historylist_tv_custmob);


            convertView.setTag(controls);

        }
        else
        {
            controls= (ViewHolder) convertView.getTag();
        }

        controls.tv_custname.setText(allcustomer.get(position).getCustname()+"");
        controls.tv_custmob.setText(allcustomer.get(position).getCustmob()+"");
        final int custid=allcustomer.get(position).getCustid();
        controls.tv_custname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(context,custitems.class);
                i.putExtra("custid",custid);
                context.startActivity(i);
            }
        });

        controls.tv_custmob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,custitems.class);
                i.putExtra("custid",custid);
                context.startActivity(i);
            }
        });



        return convertView;
    }
}
