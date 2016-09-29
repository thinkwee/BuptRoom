package thinkwee.buptroom;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private TextView tv;
    private String Tag="jsptohtml";
    private String title=null;
    private Button bt;
    private String htmlstring=null;
    private org.jsoup.nodes.Document document;
    private String content=null;
    private String[] contents;
    private int count;
    private ArrayList<String> j12=new ArrayList<String>();
    private ArrayList<String> j34=new ArrayList<String>();
    private ArrayList<String> j56=new ArrayList<String>();
    private ArrayList<String> j78=new ArrayList<String>();
    private ArrayList<String> j9=new ArrayList<String>();
    private ArrayList<String> j1011=new ArrayList<String>();
    private TextView timeinfo;
    private String Timestring=null;
    private String[] interesting={" ヾ(o◕∀◕)ﾉ新的一周新的开始!",
                                    " π__π默默学习不说话",
                                    " （╯－＿－）╯╧╧ 学海无涯苦作舟",
                                    " (´･ω･｀)转眼间一周就过了一半了呢",
                                    " ( ￣ ▽￣)o╭╯明天就是周末了！",
                                    " o(*≧▽≦)ツ周六浪起来~",
                                    " (╭￣3￣)╭♡ 忘记明天是周一吧"};
    private String nowtime=null;
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;
    private static int mHour;
    private static int mMinute;
    private int daycount=0;
    private int HaveNetFlag=0;
    private int WrongNet=1;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       /**
        * Created by Thinkwee on 2016/9/28 0028 9:23
        * Parameter [savedInstanceState]
        * Return void
        * CLASS:MainActivity
        * FILE:MainActivity.java
        * TODO:沉浸式状态栏
        */

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);


        setContentView(R.layout.activity_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView = (WebView)findViewById(R.id.web);
        tv=(TextView)findViewById(R.id.show_content);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavascriptHandler(), "handler");
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://jwxt.bupt.edu.cn/zxqDtKxJas.jsp");
        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                if (HaveNetFlag==0) Notification_show("网络错误，请确保在校园网环境下使用本软件");
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,3000);

        timesetting();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }




        @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:27
         * Parameter [item]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:版本信息点击事件
         */

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showVersionDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:27
         * Parameter [item]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:侧边栏点击事件
         */

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (WrongNet==1){
            if (id == R.id.jiao1||id ==R.id.jiao2||id == R.id.jiao3||id ==R.id.jiao4||id == R.id.tushuguan) {
              showAlertDialog();
            }else if (id == R.id.about) {
                this.setTitle("关于");
                tv.setText("作者：Thinkwee\nQQ:1920911744\nEMail:thinkwee2767@gmail.com\n\n\n\n\n版本:V1.1.3");
            }
        }else{
            if (id == R.id.jiao1) {
                tv.setText(get_show_content("教一楼"));
                this.setTitle("教一楼空闲教室");
            } else if (id == R.id.jiao2) {
                tv.setText(get_show_content("教二楼"));
                this.setTitle("教二楼空闲教室");
            } else if (id == R.id.jiao3) {
                tv.setText(get_show_content("教三楼"));
                this.setTitle("教三楼空闲教室");
            } else if (id == R.id.jiao4) {
                tv.setText(get_show_content("教四楼"));
                this.setTitle("教四楼空闲教室");
            }else if (id==R.id.tushuguan){
                tv.setText(get_show_content("图书馆"));
                this.setTitle("图书馆空闲教室");
            } else if (id == R.id.about) {
                this.setTitle("关于");
                tv.setText("作者：Thinkwee\nEMail:thinkwee2767@gmail.com\n本项目开源，Github地址\nhttps://github.com/thinkwee/BuptRoom.git\n\n\n\n\n版本:V1.1.3");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    final class MyWebViewClient extends WebViewClient {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:27
         * Parameter
         * Return
         * CLASS:MyWebViewClient
         * FILE:MainActivity.java
         * TODO:获取网页内容
         */

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("WebView","onPageStarted");
            super.onPageStarted(view, url, favicon);
        }
        public void onPageFinished(WebView view, String url) {
            Log.d("WebView","onPageFinished ");
            view.loadUrl("javascript:window.handler.getContent(document.body.innerHTML);");
            super.onPageFinished(view, url);
        }
    }


    final  class JavascriptHandler{
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:27
         * Parameter
         * Return
         * CLASS:JavascriptHandler
         * FILE:MainActivity.java
         * TODO:HANDLER
         */

        @JavascriptInterface
        public void getContent(String htmlContent){
            Log.i(Tag,"html content: "+htmlContent);
            document= Jsoup.parse(htmlContent);
            htmlstring=htmlContent;
            content=document.getElementsByTag("body").text();
            HaveNetFlag=1;
            if (content.contains("楼"))   {Notification_show("教室信息拉取完成，可以查看空闲教室");WrongNet=0;}
             else Notification_show("没有网络，请确保在校园网环境下使用本软件");


        }
    }


    public void Notification_show(String flag){
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:26
         * Parameter [flag] 显示的正文
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:NOTIFICATION
         */

        String MainText=null;
        Notification.Builder mBuilder = new Notification.Builder(this);
        Bitmap LB=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        mBuilder.setContentTitle("BuptRoom")                        //标题
                .setContentText(flag)      //内容
                .setSubText("滑动取消此消息")                    //内容下面的一小段文字
                .setTicker("BuptRoom提醒")             //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())           //设置通知时间
                .setSmallIcon(R.mipmap.ic_launcher)            //设置小图标
                .setLargeIcon(LB)                     //设置大图标
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                .setAutoCancel(true)                           //设置点击后取消Notification
                .setOngoing(false);                        //设置正在进行
        NotificationManager mNManager;
        mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNManager.notify(1, mBuilder.build());

    }




    public String get_show_content(String keyword){
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:26
         * Parameter [keyword]楼名
         * Return java.lang.String
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:某栋楼某一天数据
         */

        String contenttemp=content;
        content="";
        String[] contentstemp=contenttemp.split(",");
        for (String temp:contentstemp){
            content=content+temp;
        }
        contents=content.split(" |:");
        String showcontent="";
        count=0;
        int tsgflag=0;
        int cishu=0;
        j12.clear();
        j34.clear();
        j56.clear();
        j78.clear();
        j9.clear();
        j1011.clear();
        if (keyword.contains("图书馆")) tsgflag=1;
        for (String temp:contents){
            if (temp.contains("2节")) cishu=1;else
            if (temp.contains("4节")) cishu=2;else
            if (temp.contains("6节")) cishu=3;else
            if (temp.contains("8节")) cishu=4;else
            if (temp.contains("9节")) cishu=5;else
            if (temp.contains("11节")) cishu=6;
            if (temp.contains(keyword)){
                if (tsgflag==1&&temp.length()<5) {
                    Log.i(Tag,"!!!!!!!!!!!!!!!!!!!!!!"+temp.length()+"!!!!!"+cishu);
                    switch (cishu) {
                        case 1:
                            j12.add("图书馆一层");
                            break;
                        case 2:
                            j34.add("图书馆一层");
                            break;
                        case 3:
                            j56.add("图书馆一层");
                            break;
                        case 4:
                            j78.add("图书馆一层");
                            break;
                        case 5:
                            j9.add("图书馆一层");
                            break;
                        case 6:
                            j1011.add("图书馆一层");
                            break;
                        default:
                            break;
                    }
                }else if (tsgflag==0)  SaveBuidlingInfo(count,cishu);
            }
            count++;
        }

        return "————————第1|2节————————\n"+settlecontent(j12)+"\n\n"+"————————第3|4节————————\n"+settlecontent(j34)+"\n\n"+
                "————————第5|6节————————\n"+settlecontent(j56)+"\n\n"+"————————第7|8节————————\n"+settlecontent(j78)+"\n\n"+
                "—————————第9节—————————\n"+settlecontent(j9)+"\n\n"+"————————第10|11节————————\n"+settlecontent(j1011)+"\n\n\n\n\n\n\n\n\n已经到底了！！";
    }



    public void SaveBuidlingInfo(int k,int c){
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:28
         * Parameter [k, c]当前字符串数组下标，节次
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:分节次处理数据
         */

        k++;
        while (1 == 1) {
            if (contents[k].contains("楼") || contents[k].contains("节") || contents[k].contains("图"))         break;
            switch (c) {
                case 1:
                    j12.add(contents[k]);
                    break;
                case 2:
                    j34.add(contents[k]);
                    break;
                case 3:
                    j56.add(contents[k]);
                    break;
                case 4:
                    j78.add(contents[k]);
                    break;
                case 5:
                    j9.add(contents[k]);
                    break;
                case 6:
                    j1011.add(contents[k]);
                    break;
                default:
                    break;
            }
            k++;
        }
    }

    private void showVersionDialog() {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:29
         * Parameter []
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:版本信息
         */


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("版本说明");
        builder.setMessage("版本号:V1.1.3\n1. 增加了网络错误情况提醒\n2. 增加了启动通知");
        builder.setNegativeButton("原来如此", null);
        builder.show();
    }

    private void showAlertDialog() {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:29
         * Parameter []
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:网络错误警告信息
         */


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("网络错误");
        builder.setMessage("您所在网络环境不是校园网，无法得到空闲教室信息\n请连接到校园网后重启软件");
        builder.setNegativeButton("我知道了", null);
        builder.show();
    }


    private void timesetting(){
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "今天是"+Timestring+" "+nowtime+"\n"+interesting[daycount], Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public String settlecontent(ArrayList<String> target){

        /**
         * Created by Thinkwee on 2016/9/28 0028 9:30
         * Parameter [target]
         * Return java.lang.String
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:分楼层优化显示
         */


        if (target.isEmpty()) return "无空闲教室";

        int len=target.size();
        String f1="";
        String f2="";
        String f3="";
        String f4="";
        String f5="";
        String result="";

        for (int i=0;i<len;i++)
        {
          if (target.get(i).contains("-1")) f1=f1+target.get(i)+" ";else
          if (target.get(i).contains("-2")) f2=f2+target.get(i)+" ";else
          if (target.get(i).contains("-3")) f3=f3+target.get(i)+" ";else
          if (target.get(i).contains("-4")) f4=f4+target.get(i)+" ";else
          if (target.get(i).contains("-5")) f5=f5+target.get(i)+" ";else
          if (target.get(i).contains("图书")) result="图书馆一楼";
        }
        if (f1!="") result=result+"一楼："+f1+"\n";
        if (f2!="") result=result+"二楼："+f2+"\n";
        if (f3!="") result=result+"三楼："+f3+"\n";
        if (f4!="") result=result+"四楼："+f4+"\n";
        if (f5!="") result=result+"五楼："+f5+"\n";
        return result;

    }


}
