package thinkwee.buptroom;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by think on 20162016/10/9 000920:57
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 */

public class WelcomeActivity extends Activity {

    private Webget webget;
    private WebView webView;
    private int HaveNetFlag = 0;
    private String htmlbody = null;
    private int WrongNet = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        webView = (WebView) findViewById(R.id.GetHtml);
        AssetManager mgr = getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/Roboto-Italic.ttf");//根据路径得到Typeface
        TextView chickensoup = (TextView) findViewById(R.id.chicken_soup);
        chickensoup.setTypeface(tf);//设置字体
        tf = Typeface.createFromAsset(mgr, "fonts/Roboto-MediumItalic.ttf");
        TextView Title = (TextView) findViewById(R.id.welcome_title);
        Title.setTypeface(tf);//设置字体
        webget = new Webget();


        webget.init(webView);
        webget.WebInit();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                ImageView img = (ImageView) findViewById(R.id.welcomeimg);
                Animation animation = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.enlarge);
                animation.setFillAfter(true);
                img.startAnimation(animation);
            }
        }, 500);

        new Handler().postDelayed(new Runnable(){
            public void run() {
                //execute the task
                WrongNet = webget.getWrongnet();
                HaveNetFlag = webget.getHaveNetFlag();
                htmlbody = webget.getHtmlbody();
                Log.i("welcome", "2a" + WrongNet);
                Log.i("welcome", "2html: " + htmlbody);

            }
        }, 1000);


        if (WrongNet == 0)
            Notification_show("教室信息拉取完成，可以查看空闲教室");
        else
            Notification_show("无网络或非校园网，请重启或离线");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("WrongNet", WrongNet);
                intent.putExtra("HtmlBody", htmlbody);
                startActivity(intent);
                WelcomeActivity.this.finish();

            }

        }, 2500);
    }

    public void Notification_show(String Notification_content) {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:26
         * Parameter [Notification_content] 显示的正文
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         */

        Notification.Builder mBuilder = new Notification.Builder(this);
        Bitmap LB = BitmapFactory.decodeResource(getResources(), R.drawable.logoko);
        Intent resultIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        resultIntent.putExtra("WrongNet", WrongNet);
        resultIntent.putExtra("HtmlBody", htmlbody);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                WelcomeActivity.this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle("BuptRoom")                        //标题
                .setContentText(Notification_content)             //内容
                .setSubText("滑动取消此消息")                    //内容下面的一小段文字
                .setTicker("BuptRoom提醒")                      //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())              //设置通知时间
                .setSmallIcon(R.drawable.logoko)             //设置小图标
                .setLargeIcon(LB)                     //设置大图标
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                .setAutoCancel(true)                           //设置点击后取消Notification
                .setOngoing(false)                        //设置正在进行
                .setContentIntent(resultPendingIntent);
        NotificationManager mNManager;
        mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNManager.notify(1, mBuilder.build());
    }

}
