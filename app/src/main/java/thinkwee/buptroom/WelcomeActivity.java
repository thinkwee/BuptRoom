package thinkwee.buptroom;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by think on 20162016/10/9 000920:57
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:欢迎界面，在Manifest中设置此Activity为启动Activity,设置为全屏，设置为一段时间后跳转到主Activity
 */

public class WelcomeActivity extends Activity {

    private int HaveNetFlag=0;
    private WebView webView;
    private org.jsoup.nodes.Document document;
    private String htmlbody=null;
    private int WrongNet=1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        webView = (WebView)findViewById(R.id.GetHtml);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo("thinkwee.buptroom", 0);
            WebInit();
            ImageView img=(ImageView)findViewById(R.id.welcomeimg);
            Animation animation= AnimationUtils.loadAnimation(this,R.anim.enlarge);
            animation.setFillAfter(true);
            img.startAnimation(animation);
            AssetManager mgr=getAssets();//得到AssetManager
            Typeface tf=Typeface.createFromAsset(mgr, "fonts/Roboto-Italic.ttf");//根据路径得到Typeface
            TextView chickensoup=(TextView)findViewById(R.id.chicken_soup);
            chickensoup.setTypeface(tf);//设置字体
            tf=Typeface.createFromAsset(mgr, "fonts/Roboto-MediumItalic.ttf");
            TextView Title = (TextView) findViewById(R.id.welcome_title);
            Title.setTypeface(tf);//设置字体
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                intent.putExtra("WrongNet",WrongNet);
                intent.putExtra("HtmlBody",htmlbody);

                startActivity(intent);
                WelcomeActivity.this.finish();

            }

        }, 2500);
    }

    public void WebInit(){
        /**
         * Created by Thinkwee on 2016/10/8 0008 21:23
         * Parameter []
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:网络环境初始化
         */

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WelcomeActivity.JavascriptHandler(), "handler");
        webView.setWebViewClient(new WelcomeActivity.MyWebViewClient());
        webView.loadUrl("http://jwxt.bupt.edu.cn/zxqDtKxJas.jsp");
        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                if (HaveNetFlag==0) Notification_show("网络错误，请确保在校园网环境下使用本软件");
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,3000);
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
         * TODO:HANDLER，用于接受Webview执行javascrpit语句后得到的返回值
         */

        @JavascriptInterface
        public void getContent(String htmlContent){
            document= Jsoup.parse(htmlContent);
            htmlbody=document.getElementsByTag("body").text();
            HaveNetFlag=1;
            if (htmlbody.contains("楼"))   {WrongNet=0;Notification_show("教室信息拉取完成，可以查看空闲教室");}
            else Notification_show("没有网络，请确保在校园网环境下使用本软件");


        }
    }

    public void Notification_show(String Notification_content){
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:26
         * Parameter [Notification_content] 显示的正文
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:网络信息拉取完成的通知消息
         */

        Notification.Builder mBuilder = new Notification.Builder(this);
        Bitmap LB= BitmapFactory.decodeResource(getResources(),R.mipmap.launcher);
        Intent resultIntent = new Intent(WelcomeActivity.this,MainActivity.class);
        resultIntent.putExtra("WrongNet",WrongNet);
        resultIntent.putExtra("HtmlBody",htmlbody);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                WelcomeActivity.this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle("BuptRoom")                        //标题
                .setContentText(Notification_content)             //内容
                .setSubText("滑动取消此消息")                    //内容下面的一小段文字
                .setTicker("BuptRoom提醒")                      //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())              //设置通知时间
                .setSmallIcon(R.mipmap.launcher)             //设置小图标
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
