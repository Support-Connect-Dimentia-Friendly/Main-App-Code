package com.alzheimer.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SportActivity extends AppCompatActivity {
    @BindView(R.id.icon_fanhui)
    ImageView iconFanhui;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sportList)
    ListView sportList;
    Sport_Adapter sport_adapter;
    List<Sport> list = new ArrayList<>();
    @BindView(R.id.subname)
    EditText subname;
    @BindView(R.id.go)
    Button go;
    @BindView(R.id.nodata)
    TextView nodata;
    private Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        ButterKnife.bind(this);
        mcontext = this;
        Data_loading();
        sportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SportActivity.this, SportDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });
    }

    // Handler running in the main thread: use looper in Android's default UI thread
    public Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    list = JSONObject.parseArray(strData, Sport.class);
                    Log.d("这里是ONSTART", String.valueOf(list.size()));
                    sport_adapter = new Sport_Adapter(mcontext, (ArrayList<Sport>) list);
                    sportList.setAdapter(sport_adapter);
                    break;
                case 2:
                    String searchData = (String) msg.obj;
                    List<Sport> getData = JSONObject.parseArray(searchData, Sport.class);
                    if(getData.size()==0){
                        nodata.setVisibility(View.VISIBLE);
                        sportList.setVisibility(View.GONE);
                    }else{
                        nodata.setVisibility(View.GONE);
                        sportList.setVisibility(View.VISIBLE);
                        list.clear();
                        list.addAll(getData);
                        sport_adapter.notifyDataSetChanged();
                    }

                    break;
                default:
                    break;
            }
        }
    };

    // Load data from database
    private void Data_loading() {
//        list = DataSupport.findAll(Note_table.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://39.107.109.210:13522/alzheimer/sport")//request interface
                            .build();// Create request object
                    Response response = client.newCall(request).execute();// Get request object
                    if (response.isSuccessful()) {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = response.body().string();
                        handlerUI.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    //Load data from database
    private void searchData() {
//        list = DataSupport.findAll(Note_table.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://39.107.109.210:13522/alzheimer/sport?suburb=" + subname.getText().toString())//request interface
                            .build();//Create request object
                    Response response = client.newCall(request).execute();//Get Response object
                    if (response.isSuccessful()) {
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = response.body().string();
                        handlerUI.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @OnClick({R.id.icon_fanhui, R.id.go, R.id.home})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.icon_fanhui:
                intent = new Intent(this, FacilitiesActivity.class);
                startActivity(intent);
                break;
            case R.id.go:
                searchData();
                break;
            case R.id.home:
                intent = new Intent(this, Note_Home.class);
                startActivity(intent);
                break;
        }
    }

    @OnClick(R.id.icon_fanhui)
    public void icon_fanhui(View view) {
        Intent intent = new Intent(this, FacilitiesActivity.class);
        startActivity(intent);
    }

}
