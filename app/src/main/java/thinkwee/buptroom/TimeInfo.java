package thinkwee.buptroom;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by think on 20162016/9/29 002917:32
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:时间小助手工具类...
 */

public class TimeInfo {

    public String nowtime=null;//现在是第几节课
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;
    private static int mHour;
    private static int mMinute;
    public int daycount=0;//周几代号，配合MainActivity中的字符串数组显示每日问候
    public String Timestring=null;//年月日星期几

    public TimeInfo() {
    }

    public void timesetting(){
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:30
         * Parameter []
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:SNACKBAR时间助手
         */

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        mHour= c.get(Calendar.HOUR_OF_DAY);
        mMinute= c.get(Calendar.MINUTE);

        if (mHour>=8&&mHour<10){
            nowtime="现在是12节课";
        }else
        if (mHour>=10&&mHour<12){
            nowtime="现在是34节课";
        }else
        if ((mHour==13&&mMinute>=30)||(mHour==14)||(mHour==15&&mMinute<30)){
            nowtime="现在是56节课";
        }else
        if ((mHour==15&&mMinute>=30)||(mHour==16)||(mHour==17&&mMinute<30)){
            nowtime="现在是78节课";
        }else
        if ((mHour==17&&mMinute>=30)||(mHour==18&&mMinute<30)){
            nowtime="现在是第9节课";
        }else
        if ((mHour==18&&mMinute>=30)||(mHour==19)||(mHour==20&&mMinute<30)){
            nowtime="现在是10,11节课";
        }else
            nowtime="现在是休息时间";

        if("1".equals(mWay)){
            mWay ="天";
            daycount=6;
        }else if("2".equals(mWay)){
            mWay ="一";
            daycount=0;
        }else if("3".equals(mWay)){
            mWay ="二";
            daycount=1;
        }else if("4".equals(mWay)){
            mWay ="三";
            daycount=2;
        }else if("5".equals(mWay)){
            mWay ="四";
            daycount=3;
        }else if("6".equals(mWay)){
            mWay ="五";
            daycount=4;
        }else if("7".equals(mWay)){
            mWay ="六";
            daycount=5;
        }
        Timestring=mYear + "年" + mMonth + "月" + mDay+"日"+"星期"+mWay;


    }
}
