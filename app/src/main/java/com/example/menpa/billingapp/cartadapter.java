package com.example.menpa.billingapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class cartadapter extends BaseAdapter implements Filterable {

    ArrayList<cartbean> cartitemsarray,orig;
    allquery db;
    Activity context;
    public cartadapter(Activity context, ArrayList<cartbean> cartitemsarray) {
        this.context=context;
        this.cartitemsarray=cartitemsarray;
        db=new allquery(context);
    }


    @Override
    public int getCount()    {
        return cartitemsarray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return cartitemsarray.get(position).getCartbillid();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<cartbean> results = new ArrayList<cartbean>();
                if (orig == null)
                    orig = cartitemsarray;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final cartbean g : orig) {
                            if (g.getCartitemname().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                cartitemsarray = (ArrayList<cartbean>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class viewholder
    {
        TextView tv_cname;
        TextView tv_cweight;
        TextView tv_cprice;
        TextView tv_cquantity;
        Button btn_remove;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final viewholder cartitems;

        LayoutInflater inflater=context.getLayoutInflater();

        if (convertView==null)
        {
            convertView=inflater.inflate(R.layout.cart_listitems,parent,false);

            cartitems=new viewholder();

            cartitems.tv_cname=convertView.findViewById(R.id.cartlist_tv_name);
            cartitems.tv_cweight=convertView.findViewById(R.id.cartlist_tv_weight);
            cartitems.tv_cprice=convertView.findViewById(R.id.cartlist_tv_price);
            cartitems.tv_cquantity=convertView.findViewById(R.id.cartlist_tv_quantity);
            cartitems.btn_remove=convertView.findViewById(R.id.cart_list_itemremove);
            convertView.setTag(cartitems);
        }
        else
        {
            cartitems=(viewholder)convertView.getTag();
        }

        final int itemid=position;
        cartitems.tv_cname.setText("NAME :- "+cartitemsarray.get(position).getCartitemname());
        cartitems.tv_cweight.setText("WEIGHT :- "+cartitemsarray.get(position).getCartitemweight()+" g");
        cartitems.tv_cprice.setText("MRP :- "+cartitemsarray.get(position).getCartitemprice()+"rs");
        cartitems.tv_cquantity.setText("QUANTITY :- "+cartitemsarray.get(position).getCartitemquantity());

        cartitems.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //int res=db.delete(String.valueOf(cartitemsarray.get(itemid).getCartitemname()));



                db.delete(cartitemsarray.get(itemid).getCartbillid());
                cart.refreshcart();

            }
        });

        return convertView;
    }
}
