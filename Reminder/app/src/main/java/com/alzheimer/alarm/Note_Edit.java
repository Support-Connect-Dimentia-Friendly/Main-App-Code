package com.alzheimer.alarm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Note_Edit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.icon_fanhui)ImageView icon_fanhui;
    @BindView(R.id.naozhong_icon)ImageView naozhong_icon;
    @BindView(R.id.edit_content)EditText edit_content;
    @BindView(R.id.radioGroup)RadioGroup radioGroup;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.Rb_black)RadioButton Rb_black;
    @BindView(R.id.Rb_chengse)RadioButton Rb_chengse;
    @BindView(R.id.Rb_hailu)RadioButton Rb_hailu;
    @BindView(R.id.Rb_hongse)RadioButton Rb_hongse;
    @BindView(R.id.Rb_luse)RadioButton Rb_luse;
    @BindView(R.id.Rb_qianzi)RadioButton Rb_qianzi;
    @BindView(R.id.Rb_shenglan)RadioButton Rb_shenglan;
    Context mcontext;

    int color_key;
    String alarm_date = "";
    int id = 0;
    // Flag to determine whether to re edit or re edit
    int flg = 0;

    // Timing variable
    int alarm_hour;
    int alarm_minute;
    int alarm_year;
    int alarm_month;
    int alarm_day;
    String eventname;
    String typename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mcontext = this;
        ButterKnife.bind(this);
        eventname = getIntent().getStringExtra("eventname");
        typename = getIntent().getStringExtra("typename");
        // A memo record object passed from the main interface
        Note_Info note_info = (Note_Info)getIntent().getSerializableExtra("note_table_data");
        if(!(note_info == null)){
            revise_content(note_info);
            flg = 1;
        }

        //编辑界面的颜色选定
        color_selection();


    }
    //修改备忘时，调用此方法
    private void revise_content( Note_Info note_info){
        Log.d("序列化对象",note_info.getContent().toString());
        Log.d("序列化对象",String.valueOf(note_info.getColor_key()));
        Log.d("序列化对象",String.valueOf(note_info.getId()));
        Log.d("序列化对象",note_info.getAlarm_key());

        id = note_info.getId();
        edit_content.setText(note_info.getContent().toString());
        color_key = note_info.getColor_key();
        alarm_date = note_info.getAlarm_key();

        beijing_shezhi(color_key);

    }
    //设置背景
    void beijing_shezhi(int color_key){
        switch (color_key){
            case 0:
                edit_content.setBackgroundResource(R.color.black);
                Rb_black.setChecked(true);
                //Toast.makeText(mcontext,"黑色",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                edit_content.setBackgroundResource(R.color.chengse);
                Rb_chengse.setChecked(true);
                //Toast.makeText(mcontext,"chengse",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Rb_hailu.setChecked(true);
                edit_content.setBackgroundResource(R.color.hailu);
                //Toast.makeText(mcontext,"hailu",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Rb_hongse.setChecked(true);
                edit_content.setBackgroundResource(R.color.hongse);
                //Toast.makeText(mcontext,"hongse",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Rb_qianzi.setChecked(true);
                edit_content.setBackgroundResource(R.color.qianzi);
                //Toast.makeText(mcontext,"qianzi",Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Rb_shenglan.setChecked(true);
                edit_content.setBackgroundResource(R.color.shenglan);
                //Toast.makeText(mcontext,"shenglan",Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Rb_luse.setChecked(true);
                edit_content.setBackgroundResource(R.color.luse);
               // Toast.makeText(mcontext,"luse",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    //返回图标，返回后是否自动保存？
    @OnClick(R.id.icon_fanhui)
    public void icon_fanhui(View view){
        finish();

    }

    //点击图标，进行定时操作
    @OnClick(R.id.naozhong_icon)
    public void naozhong_icon(View view){
        Calendar c = Calendar.getInstance();
        if(alarm_date.length()>1) {
            //if no alarm clock has been set up before
            //show the current time
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date time = simpleDateFormat.parse(alarm_date);

                c.setTime(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        alarm_hour=c.get(Calendar.HOUR_OF_DAY);
        alarm_minute=c.get(Calendar.MINUTE);

        alarm_year=c.get(Calendar.YEAR);
        alarm_month=c.get(Calendar.MONTH)+1;
        alarm_day=c.get(Calendar.DAY_OF_MONTH);
        Log.d("月份",String.valueOf(alarm_month));
        new TimePickerDialog(this,this,alarm_hour,alarm_minute,true).show();
        new DatePickerDialog(this,this,alarm_year,alarm_month-1,alarm_day).show();




    }

    //运行在主线程的Handler：使用Android默认的UI线程中的Looper
    public Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    id = Integer.parseInt(strData);
                    //进行闹钟的设置
                    setAlarm();
                    Intent intent = new Intent(mcontext,Note_Home.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    //保存点击事件
    @OnClick(R.id.fab)
    public void fab(View view){
            if(alarm_date.equals("")){
                Toast.makeText(this,"please choose alarm",Toast.LENGTH_LONG).show();
                return;
            }
            final String content = edit_content.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        //进行post请求数据库保存
                        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
                        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                        formBody.add("description",content);//传递键值对参数
                        formBody.add("time",alarm_date);//传递键值对参数
                        formBody.add("colorkey", String.valueOf(color_key));//传递键值对参数
                        if(eventname!=null){
                            formBody.add("eventname",eventname);
                        }
                        if(typename!=null){
                            formBody.add("typename",typename);
                        }
                        if(id!=0){
                            formBody.add("id", String.valueOf(id));
                        }

                        Request request = new Request.Builder()//创建Request 对象。
                                .url("http://39.107.109.210:13522/alzheimer/user-event")
                                .post(formBody.build())//传递请求体
                                .build();
                        Response response = client.newCall(request).execute();//回调方法的使用与get异步请求相同，此时略。
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

    public void setAlarm(){
        if(alarm_date.length()>1) {
            //if no alarm clock has been set up before
            //show the current time
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date time = simpleDateFormat.parse(alarm_date);
                //从数据库中拿到alarm_date数据进行定时操作
                Intent intent = new Intent(mcontext, BroadcastAlarm.class);
                intent.putExtra("alarmId",id);
                PendingIntent sender = PendingIntent.getBroadcast(mcontext,id, intent, 0);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                //calendar.add(Calendar.SECOND, 5);

                Calendar alarm_time = Calendar.getInstance();
                alarm_time.setTime(time);
                // Schedule the alarm!
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                //if(interval==0)
                am.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), sender);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //编辑界面的颜色选定
    private void color_selection(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
               switch (checkedId){
                   case R.id.Rb_black:
                       edit_content.setBackgroundResource(R.color.black);
                       //Toast.makeText(mcontext,"black",Toast.LENGTH_SHORT).show();
                       color_key = 0;
                       break;
                   case R.id.Rb_chengse:
                       edit_content.setBackgroundResource(R.color.chengse);
                       //Toast.makeText(mcontext,"chengse",Toast.LENGTH_SHORT).show();
                       color_key = 1;
                       break;
                   case R.id.Rb_hailu:
                       edit_content.setBackgroundResource(R.color.hailu);
                       //Toast.makeText(mcontext,"hailu",Toast.LENGTH_SHORT).show();
                       color_key = 2;
                       break;
                   case R.id.Rb_hongse:
                       edit_content.setBackgroundResource(R.color.hongse);
                       //Toast.makeText(mcontext,"hongse",Toast.LENGTH_SHORT).show();
                       color_key = 3;
                       break;
                   case R.id.Rb_qianzi:
                       edit_content.setBackgroundResource(R.color.qianzi);
                       //Toast.makeText(mcontext,"qianzi",Toast.LENGTH_SHORT).show();
                       color_key = 4;
                       break;
                   case R.id.Rb_shenglan:
                       edit_content.setBackgroundResource(R.color.shenglan);
                       //Toast.makeText(mcontext,"shenglan",Toast.LENGTH_SHORT).show();
                       color_key = 5;
                       break;
                   case R.id.Rb_luse:
                       edit_content.setBackgroundResource(R.color.luse);
                       //Toast.makeText(mcontext,"luse",Toast.LENGTH_SHORT).show();
                       color_key = 6;
                       break;
                   default:
                       break;
               }
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        alarm_year=year;
        alarm_month=monthOfYear+1;
        alarm_day=dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        alarm_hour=hourOfDay;
        alarm_minute=minute;
        Calendar calendar = Calendar.getInstance();
        calendar.set(alarm_year,alarm_month-1,alarm_day,alarm_hour,alarm_minute);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        alarm_date=simpleDateFormat.format(calendar.getTime());
//        av.setText("Alert at "+alarm+"!");
//        av.setVisibility(View.VISIBLE);
//        Toast.makeText(this,"",Toast.LENGTH_LONG).show();
    }
}
