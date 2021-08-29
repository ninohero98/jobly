package com.Engineering.jobly;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
    private static ArrayList<JobOffer> itemDetailsrrayList;

    private LayoutInflater l_Inflater;

    public ListAdapter(Context context, ArrayList<JobOffer> results) {
        itemDetailsrrayList = results;
        l_Inflater = LayoutInflater.from(context);
    }



    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public Object getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.job_offer_item, null);
            holder = new ViewHolder();
            holder.txt_itemName = (TextView) convertView.findViewById(R.id.JobTitile);
            holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.SubTilte);
            holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.BudgetRange);

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }
if(itemDetailsrrayList.get(position).Title.length() >24) {
    holder.txt_itemName.setText(itemDetailsrrayList.get(position).Title.substring(0, 23)  +" ....");
}
else
{
    holder.txt_itemName.setText(itemDetailsrrayList.get(position).Title);
}
if(itemDetailsrrayList.get(position).Description.length() >24) {
    holder.txt_itemDescription.setText(itemDetailsrrayList.get(position).Description.substring(0, 23) + " ....");
}
else{
    holder.txt_itemDescription.setText(itemDetailsrrayList.get(position).Description);
}
        holder.txt_itemPrice.setText(itemDetailsrrayList.get(position).Budget);

        return convertView;
    }

    static class ViewHolder {
        TextView txt_itemName;
        TextView txt_itemDescription;
        TextView txt_itemPrice;
    }
}

