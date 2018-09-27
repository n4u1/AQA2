package com.n4u1.AQA.AQA.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.n4u1.AQA.AQA.R;
import com.n4u1.AQA.AQA.views.SearchHomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewSearchCategoryAdapter extends ArrayAdapter<String> {

    private SearchHomeActivity activity;
    private List<String> allList;
    private List<String> searchList;

    public ListViewSearchCategoryAdapter(SearchHomeActivity context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.allList = objects;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(allList);
    }

    @Override
    public int getCount() {
        return allList.size();
    }

    @Override
    public String getItem(int position) {
        return allList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.search_item_listview, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.friendName.setText(getItem(position));

        return convertView;
    }

    // Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        allList.clear();
        if (charText.length() == 0) {
            allList.addAll(searchList);
        } else {
            for (String s : searchList) {
                if (s.toLowerCase(Locale.getDefault()).contains(charText)) {
                    allList.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView friendName;

        public ViewHolder(View v) {

//            imageView =  v.findViewById(R.id.image_view);
            friendName = v.findViewById(R.id.text);
        }
    }
}
