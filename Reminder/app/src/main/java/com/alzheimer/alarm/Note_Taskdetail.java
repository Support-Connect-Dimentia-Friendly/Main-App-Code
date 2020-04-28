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
    @BindView(R.id.gp)ImageView gpImage;
    int color_key;
    int id;
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
                    setDraw(note_table.getEventname());
                    setColor();
                    // Play alarm
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    Ringtone mRingTone = RingtoneManager.getRingtone(Note_Taskdetail.this, notification);
                    if (mRingTone != null && !mRingTone.isPlaying()) {
                        mRingTone.play();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void setDraw(String type){
        if(type.equals("Insulin shot")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.shot)));
        }else if(type.equals("make the bed")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.bed)));
        }else if(type.equals("clean dishes")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.clean)));
        }else if(type.equals("grocery shopping")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.shopping)));
        }else if(type.equals("Take the rubbish")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.rubbish)));
        }else if(type.equals("take a bath")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.bath)));
        }else if(type.equals("hair")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.hair)));
        }else if(type.equals("GP appointment")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.gp)));
        }else if(type.equals("Dentist Appointment")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.dentist)));
        }else if(type.equals("Going to the park")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.park)));
        }else if(type.equals("Visiting Friends")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.friend)));
        }else if(type.equals("Walking a dog")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.dog)));
        }else if(type.equals("Going out for dinner")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.dinner)));
        }else if(type.equals("Participate in community cooking competition")){
            gpImage.setImageDrawable(getResources().getDrawable((R.drawable.cooking)));
        }

    }

    private void setColor(){
        if(color_key==0){
            card.setBackgroundResource(R.drawable.card_radius_black);

        }
        else if(color_key==1){

            card.setBackgroundResource(R.drawable.card_radius_chengse);
        }
        else if(color_key==2){
            card.setBackgroundResource(R.drawable.card_radius_hailu);
        }
        else if(color_key==3){
            card.setBackgroundResource(R.drawable.card_radius_red);
        }
        else if(color_key==4){
            card.setBackgroundResource(R.drawable.card_radius_qianzi);
        }
        else if(color_key==5){
            card.setBackgroundResource(R.drawable.card_radius_shenglan);
        }
        else if(color_key==6){
            card.setBackgroundResource(R.drawable.card_radius_luse);
        }
    }
    //从数据库中加载数据
    private void Data_loading(){
//        list = DataSupport.findAll(Note_table.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:13522/alzheimer/user-event/"+id)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = client.newCall(request).execute();//得到Response 对象
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
        Intent intent = new Intent(this,Note_Home.class);
        startActivity(intent);
    }
}
