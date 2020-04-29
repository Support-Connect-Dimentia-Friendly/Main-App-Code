package com.alzheimer.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Note_Home extends AppCompatActivity {

    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    private Context mcontext;
    Note_Adapter note_adapter;
    List<Note_table> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
        ButterKnife.bind(this);
        listview_click();  //listview点击事件
        listview_longPress(); //listview长按事件
    }
    //添加新备忘
    @OnClick(R.id.fab)
    public void fab(View view){
//        Intent intent = new Intent(mcontext,Note_Edit.class);
//        startActivityForResult(intent,1);
        Intent intent = new Intent(mcontext,TypeActivity.class);
        startActivity(intent);
    }
    //天气
    @OnClick(R.id.icon)
    public void icon(View view){
        Intent intent = new Intent(mcontext,MapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        Data_loading();

    }


    //运行在主线程的Handler：使用Android默认的UI线程中的Looper
    public Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    list = JSONObject.parseArray(strData,Note_table.class);
                    Log.d("这里是ONSTART",String.valueOf(list.size()));
                    note_adapter = new Note_Adapter(mcontext,(ArrayList<Note_table>)list);
                    listView.setAdapter(note_adapter);
                    break;
                case 2:
                    Data_loading();
                    Toast.makeText(mcontext,"删除成功～",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //从数据库中加载数据
    private void Data_loading(){
//        list = DataSupport.findAll(Note_table.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://39.107.109.210:13522/alzheimer/user-event")//请求接口。如果需要传参拼接到接口后面。
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

    //从数据库中加载数据
    private void delete(final String deleteId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //进行post请求数据库保存
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
                    FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                    Request request = new Request.Builder()//创建Request 对象。
                            .url("http://39.107.109.210:13522/alzheimer/user-event/"+deleteId)
                            .post(formBody.build())//传递请求体
                            .build();
                    Response response = client.newCall(request).execute();//回调方法的使用与get异步请求相同，此时略。
                    if (response.isSuccessful()) {
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = response.body().string();
                        handlerUI.sendMessage(message);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //listview点击事件
    private void listview_click(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Note_Info note_info =  new Note_Info();

                note_info.setId(list.get(position).getId());
                note_info.setColor_key(list.get(position).getColorkey());
                note_info.setContent(list.get(position).getDescription());
                note_info.setAlarm_key(list.get(position).getTime());
                note_info.setTime(list.get(position).getTime());

                Intent intent = new Intent(mcontext,Note_Edit.class);
                intent.putExtra("note_table_data", note_info);
                startActivity(intent);

            }
        });
    }


    //listview长按事件
    private void listview_longPress(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog alert = null;
                AlertDialog.Builder builder  = new AlertDialog.Builder(mcontext);

                alert = builder.setIcon(R.drawable.jinggao)
                        .setTitle("tips：")
                        .setMessage("Delete this？")
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mcontext,"cancel～",Toast.LENGTH_SHORT).show();
                            }

                        })
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    cancelAlarm(position);
                                    delete(list.get(position).getId()+"");


                                }
                            }).create();

                alert.show();

                return true;
            }
        });
    }

    //cancel the alarm
    private void cancelAlarm(int num) {


        Intent intent = new Intent(mcontext,BroadcastAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(mcontext,list.get(num).getId(),intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        Log.d("cancel alarm","cancel cancel");
    }
}
