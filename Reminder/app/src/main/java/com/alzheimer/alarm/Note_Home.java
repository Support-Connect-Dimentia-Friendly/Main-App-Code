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
        listview_click();  //listviewClick event
        listview_longPress(); //listview Long press event
    }
    //Add a new note
    @OnClick(R.id.fab)
    public void fab(View view){
//        Intent intent = new Intent(mcontext,Note_Edit.class);
//        startActivityForResult(intent,1);
        Intent intent = new Intent(mcontext,TypeActivity.class);
        startActivity(intent);
    }
    //Weather
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


    //Handler running in the main thread: use looper in Android's default UI thread
    public Handler handlerUI = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String strData = (String) msg.obj;
                    list = JSONObject.parseArray(strData,Note_table.class);
                    Log.d("This is ONSTART",String.valueOf(list.size()));
                    note_adapter = new Note_Adapter(mcontext,(ArrayList<Note_table>)list);
                    listView.setAdapter(note_adapter);
                    break;
                case 2:
                    Data_loading();
                    Toast.makeText(mcontext,"Delete successful～",Toast.LENGTH_SHORT).show();
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
                            .url("http://10.0.2.2:13522/alzheimer/user-event")//Request interface. If you need to splice parameters to the back of the interface.
                            .build();//Create request object
                    Response response = client.newCall(request).execute();//Get Response object
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

    //Load data from database
    private void delete(final String deleteId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Post request database save
                    OkHttpClient client = new OkHttpClient();//Create the okhttpclient object.
                    FormBody.Builder formBody = new FormBody.Builder();//Create form request body
                    Request request = new Request.Builder()//Create Request object.
                            .url("http://10.0.2.2:13522/alzheimer/user-event/"+deleteId)
                            .post(formBody.build())//Delivery request body
                            .build();
                    Response response = client.newCall(request).execute();//The use of callback method is the same as that of get asynchronous request, omitted at this time.
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

    //listview click event
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


    //listview Long press event
    private void listview_longPress(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog alert = null;
                AlertDialog.Builder builder  = new AlertDialog.Builder(mcontext);

                alert = builder.setIcon(R.drawable.jinggao)
                        .setTitle("System prompt：")
                        .setMessage("Do you want to delete this note？")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mcontext,"cancel delete～",Toast.LENGTH_SHORT).show();
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
        Log.d("cancel reminder","Cancel cancel");
    }
}
