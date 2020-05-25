package com.alzheimer.alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;


/**
 *
 */
public class Library_Adapter extends BaseAdapter {

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    public ArrayList<Library> getList() {
        return list;
    }

    public void setList(ArrayList<Library> list) {
        this.list = list;
    }

    private Context mcontext;
    private ArrayList<Library> list;


    public Library_Adapter(Context mcontext, ArrayList<Library> list){
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

        convertView = LayoutInflater.from(mcontext).inflate(R.layout.library_item,parent,false);
        Log.d("这里是Library_adapter",String.valueOf(list.size()));
        TextView name = (TextView) convertView.findViewById(R.id.library_name);
//        TextView address = (TextView) convertView.findViewById(R.id.library_address);
        Button remind = convertView.findViewById(R.id.library_remind);
//        Drawable drawable1 = convertView.getResources().getDrawable(R.drawable.reminder);
//        drawable1.setBounds(0, 30, 90, 120);
//        remind.setCompoundDrawables(null, drawable1, null, null);

        name.setText(list.get(position).getLibraryName());
//        address.setText(list.get(position).getAddress());
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Note_Edit.class);
                intent.putExtra("typename","outdoor");
                intent.putExtra("eventname","go to library");
                mcontext.startActivity(intent);
            }
        });
        return convertView;
    }
}
