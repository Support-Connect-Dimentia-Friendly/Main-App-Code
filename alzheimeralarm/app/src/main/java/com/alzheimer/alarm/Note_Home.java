package com.alzheimer.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
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
    //    @BindView(R.id.icon)
//    ImageView icon;
    @BindView(R.id.fab)
    Button fab;
    @BindView(R.id.fab2)
    Button fab2;


    private Context mcontext;
    Note_Adapter note_adapter;
    List<Note_table> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
        ButterKnife.bind(this);
//        Drawable drawable1 = getResources().getDrawable(R.drawable.myremind);
//        drawable1.setBounds(0, 0, 60, 50);
//        fab.setCompoundDrawables(null, drawable1, null, null);
//        Drawable drawable2 = getResources().getDrawable(R.drawable.facilities);
//        drawable2.setBounds(0, 20, 90, 100);
//        fab2.setCompoundDrawables(null, drawable2, null, null);
        listview_click();  //listview点击事件
        listview_longPress(); //listview长按事件
    }

    //Add a new note
    @OnClick(R.id.fab)
    public void fab(View view) {
//        Intent intent = new Intent(mcontext,Note_Edit.class);
//        startActivityForResult(intent,1);
        Intent intent = new Intent(mcontext, TypeActivity.class);
        startActivity(intent);
    }
//    //天气
//    @OnClick(R.id.icon)
//    public void icon(View view){
//        Intent intent = new Intent(mcontext,MapsActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        Data_loading();

    }


    // Handler running in the main thread: use looper in Android's default UI thread
    public Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    list = JSONObject.parseArray(strData, Note_table.class);
                    Log.d("这里是ONSTART", String.valueOf(list.size()));
                    note_adapter = new Note_Adapter(mcontext, (ArrayList<Note_table>) list);
                    listView.setAdapter(note_adapter);
                    break;
                case 2:
                    Data_loading();
                    Toast.makeText(mcontext, "delete success～", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //Load data from database
    private void Data_loading() {
//        list = DataSupport.findAll(Note_table.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://39.107.109.210:13522/alzheimer/user-event")// Request interface
                            .build();//Create request object
                    Response response = client.newCall(request).execute();//Get response object
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
    private void delete(final String deleteId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Post request database save
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
                    FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                    Request request = new Request.Builder()//创建Request 对象。
                            .url("http://39.107.109.210:13522/alzheimer/user-event/" + deleteId)
                            .post(formBody.build())//传递请求体
                            .build();
                    Response response = client.newCall(request).execute();//回调方法的使用与get异步请求相同，此时略。
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

    //listview click event
    private void listview_click() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Note_Info note_info = new Note_Info();

                note_info.setId(list.get(position).getId());
//                note_info.setColor_key(list.get(position).getColorkey());
                note_info.setContent(list.get(position).getDescription());
                note_info.setAlarm_key(list.get(position).getTime());
                note_info.setTime(list.get(position).getTime());

                Intent intent = new Intent(mcontext, Note_Edit.class);
                intent.putExtra("note_table_data", note_info);
                startActivity(intent);

            }
        });
    }


    //listview long press event
    private void listview_longPress() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alert = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

                alert = builder.setIcon(R.drawable.jinggao)
                        .setTitle("system hint：")
                        .setMessage("You want to delete this note？")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mcontext, "cancel delete～", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                cancelAlarm(position);
                                delete(list.get(position).getId() + "");


                            }
                        }).create();

                alert.show();

                return true;
            }
        });
    }

    //cancel the alarm
    private void cancelAlarm(int num) {


        Intent intent = new Intent(mcontext, BroadcastAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(mcontext, list.get(num).getId(), intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

    @OnClick(R.id.fab2)
    public void onViewClicked() {
        Intent intent = new Intent(Note_Home.this,FacilitiesActivity.class);
        startActivity(intent);
    }
}
