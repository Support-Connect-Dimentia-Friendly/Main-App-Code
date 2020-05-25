package com.alzheimer.alarm;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Note_Taskdetail extends AppCompatActivity {

    @BindView(R.id.content)TextView edit_content;
    @BindView(R.id.time)TextView edit_time;
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.icon_fanhui)ImageView icon_fanhui;
    Context mcontext;
    int id;
    private Ringtone mRingTone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taskdetail);
        mcontext = this;
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("alarmId",0);
//        content = getIntent().getStringExtra("content");
//        time = getIntent().getStringExtra("time");
//        color_key = getIntent().getIntExtra("color_key",0);
        Data_loading();

    }

    // Handler running in the main thread: use looper in Android's default UI thread
    public Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    Note_table note_table = JSON.parseObject(strData,Note_table.class);
                    edit_content.setText(note_table.getDescription());
                    edit_time.setText(note_table.getTime());
                    //播放闹钟铃声
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    mRingTone = RingtoneManager.getRingtone(Note_Taskdetail.this, notification);
                    if (mRingTone != null && !mRingTone.isPlaying()) {
                        mRingTone.play();
                    }
                    break;

                default:
                    break;
            }
        }
    };



    //Load data from database
    private void Data_loading(){
//        list = DataSupport.findAll(Note_table.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://39.107.109.210:13522/alzheimer/user-event/"+id)// Request interface
                            .build();//Create request object
                    Response response = client.newCall(request).execute();//Get response object
                    if (response.isSuccessful()) {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = response.body().string();
                        handlerUI.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }


    @OnClick(R.id.icon_fanhui)
    public void icon_fanhui(View view){
        //Stop alarm
        mRingTone.stop();
        Intent intent = new Intent(this,Note_Home.class);
        startActivity(intent);
    }
}
