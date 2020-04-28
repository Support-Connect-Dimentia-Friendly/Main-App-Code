package com.alzheimer.alarm;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yao on 2020/4/25.
 */

public class SharedHelper {

    Context mcontext;

    public SharedHelper( Context mcontext){
        this.mcontext = mcontext;
    }

    // Store the searched City to SharedPreferences
    public void save(String city){

        SharedPreferences sp = mcontext.getSharedPreferences("city",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("local",city);
        editor.commit();
    }

    public String read(){
        String city;
        SharedPreferences sp = mcontext.getSharedPreferences("city",Context.MODE_PRIVATE);
        city = sp.getString("local","Melbourne");
        return city;
    }



}
