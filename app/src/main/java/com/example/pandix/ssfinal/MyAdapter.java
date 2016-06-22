package com.example.pandix.ssfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shawn on 2016/6/22.
 */
public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Result> results;

    private class ViewHolder{
        TextView bookName;
        TextView au;
        TextView cat;
        TextView si;
        TextView ind;
        public ViewHolder(TextView bookName, TextView au, TextView cat, TextView si, TextView ind){
            this.bookName = bookName;
            this.au = au;
            this.cat = cat;
            this.si = si;
            this.ind = ind;
        }
    }

    public MyAdapter(Context context, List<Result> result){
        myInflater = LayoutInflater.from(context);
        this.results = result;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int arg0) {
        return results.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return results.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.book_name),
                    (TextView) convertView.findViewById(R.id.author),
                    (TextView) convertView.findViewById(R.id.catorgary),
                    (TextView) convertView.findViewById(R.id.site),
                    (TextView) convertView.findViewById(R.id.index)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Result result = (Result)getItem(position);
        holder.bookName.setText(result.getBook_name());
        holder.au.setText(result.getAuthor());
        holder.cat.setText(result.getCatorgary());
        holder.si.setText(result.getSite());
        holder.ind.setText(result.getIndex());
        return convertView;
    }
}