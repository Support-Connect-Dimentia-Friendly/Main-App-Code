package com.alzheimer.alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 *
 */
public class Vic_Adapter extends BaseAdapter {

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    public ArrayList<Vic> getList() {
        return list;
    }

    public void setList(ArrayList<Vic> list) {
        this.list = list;
    }

    private Context mcontext;
    private ArrayList<Vic> list;


    public Vic_Adapter(Context mcontext, ArrayList<Vic> list){
        this.mcontext = mcontext;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mcontext).inflate(R.layout.vic_item,parent,false);
        Log.d("这里是Vic_adapter",String.valueOf(list.size()));
        TextView name = (TextView) convertView.findViewById(R.id.vic_name);
//        TextView des = (TextView) convertView.findViewById(R.id.vic_des);
        name.setText(list.get(position).getName());
//        des.setText(list.get(position).getDescription());
        Button remind = convertView.findViewById(R.id.vic_remind);
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Note_Edit.class);
                intent.putExtra("typename","outdoor");
                intent.putExtra("eventname","go to victoria");
                mcontext.startActivity(intent);
            }
        });
        return convertView;
    }
}
