package com.shafernotes.freemindreader;

import android.widget.ArrayAdapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.shafernotes.freemindreader.R;

public class CustomListViewAdapter extends ArrayAdapter<Node> {
    Context context;

    public CustomListViewAdapter(Context context, int resourceId, List<Node> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView mainText;
        ImageView childImage;
        ImageView linkImage;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Node rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.mainText = (TextView) convertView.findViewById(R.id.mainText);
            holder.childImage = (ImageView) convertView.findViewById(R.id.childImage);
            holder.linkImage = (ImageView) convertView.findViewById(R.id.linkImage);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.mainText.setText(rowItem.getAttributes().getNamedItem("TEXT").getNodeValue());
        holder.childImage.setVisibility(View.GONE);
        holder.linkImage.setVisibility(View.GONE);
        NodeList childNodes = rowItem.getChildNodes();
        for (int j = 0; j < childNodes.getLength();j++){
            if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                if (childNodes.item(j).getNodeName().equals("node"))  {
                    holder.childImage.setVisibility(View.VISIBLE);
                }
            }
        }

        if (rowItem.getAttributes() != null) {
            if (rowItem.getAttributes().getNamedItem("LINK") != null)
                holder.linkImage.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
