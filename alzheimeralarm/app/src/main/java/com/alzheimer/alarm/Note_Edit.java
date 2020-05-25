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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Note_Edit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.icon_fanhui)
    ImageView icon_fanhui;
    @BindView(R.id.naozhong_icon)
    ImageView naozhong_icon;
    @BindView(R.id.edit_content)
    EditText edit_content;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Context mcontext;


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
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mcontext = this;
        ButterKnife.bind(this);
        eventname = getIntent().getStringExtra("eventname");
        typename = getIntent().getStringExtra("typename");
        title.setText(typename);
        // A memo record object passed from the main interface
        Note_Info note_info = (Note_Info) getIntent().getSerializableExtra("note_table_data");
        if (!(note_info == null)) {
            revise_content(note_info);
            flg = 1;
        }

    }

    // Call this method when modifying a note
    private void revise_content(Note_Info note_info) {
        Log.d("序列化对象", note_info.getContent().toString());
        Log.d("序列化对象", String.valueOf(note_info.getColor_key()));
        Log.d("序列化对象", String.valueOf(note_info.getId()));
        Log.d("序列化对象", note_info.getAlarm_key());
        id = note_info.getId();
        edit_content.setText(note_info.getContent().toString());
        alarm_date = note_info.getAlarm_key();
    }


    //Return icon, save automatically after returning？
    @OnClick(R.id.icon_fanhui)
    public void icon_fanhui(View view) {
        finish();

    }


    // Click the icon for timing operation
    @OnClick(R.id.naozhong_icon)
    public void naozhong_icon(View view) {
        Calendar c = Calendar.getInstance();
        if (alarm_date.length() > 1) {
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

        alarm_hour = c.get(Calendar.HOUR_OF_DAY);
        alarm_minute = c.get(Calendar.MINUTE);

        alarm_year = c.get(Calendar.YEAR);
        alarm_month = c.get(Calendar.MONTH) + 1;
        alarm_day = c.get(Calendar.DAY_OF_MONTH);
        Log.d("月份", String.valueOf(alarm_month));
        new TimePickerDialog(this, this, alarm_hour, alarm_minute, true).show();
        new DatePickerDialog(this, this, alarm_year, alarm_month - 1, alarm_day).show();


    }

    // Handler running in the main thread: use looper in Android's default UI thread
    public Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    id = Integer.parseInt(strData);
                    //进行闹钟的设置
                    setAlarm();
                    Intent intent = new Intent(mcontext, Note_Home.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    // Save click event
    @OnClick(R.id.fab)
    public void fab(View view) {
        if (alarm_date.equals("")) {
            Toast.makeText(this, "please choose alarm", Toast.LENGTH_LONG).show();
            return;
        }
        final String content = edit_content.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // Post request database save
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
                    FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                    formBody.add("description", content);//传递键值对参数
                    formBody.add("time", alarm_date);//传递键值对参数
//                        formBody.add("colorkey", String.valueOf(color_key));//传递键值对参数
                    if (eventname != null) {
                        formBody.add("eventname", eventname);
                    }
                    if (typename != null) {
                        formBody.add("typename", typename);
                    }
                    if (id != 0) {
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void setAlarm() {
        if (alarm_date.length() > 1) {
            //if no alarm clock has been set up before
            //show the current time
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date time = simpleDateFormat.parse(alarm_date);
                //Get alarm from the database_ Date data for timing operation
                Intent intent = new Intent(mcontext, BroadcastAlarm.class);
                intent.putExtra("alarmId", id);
                PendingIntent sender = PendingIntent.getBroadcast(mcontext, id, intent, 0);

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


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        alarm_year = year;
        alarm_month = monthOfYear + 1;
        alarm_day = dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        alarm_hour = hourOfDay;
        alarm_minute = minute;
        Calendar calendar = Calendar.getInstance();
        calendar.set(alarm_year, alarm_month - 1, alarm_day, alarm_hour, alarm_minute);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        alarm_date = simpleDateFormat.format(calendar.getTime());
    }

    @OnClick(R.id.home)
    public void onHomeClicked() {
        Intent intent = new Intent(this, Note_Home.class);
        startActivity(intent);
    }
}
