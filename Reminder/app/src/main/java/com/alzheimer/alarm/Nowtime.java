package com.alzheimer.alarm;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yao on 2020/4/25.
 */

public class Nowtime {
    public static String Now() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//Format date
        return (df.format(new Date()));// new Date()To get the current system time
    }
}
