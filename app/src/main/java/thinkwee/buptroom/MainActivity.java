package thinkwee.buptroom;

import android.app.Notification;
import android.app.NotificationManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.TextView;


import org.jsoup.Jsoup;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private TextView tv;
    private org.jsoup.nodes.Document document;
    private String htmlbody=null;
    private String[] interesting={" ヾ(o◕∀◕)ﾉ新的一周新的开始!",
                                    " π__π默默学习不说话",
                                    " （╯－＿－）╯╧╧ 学海无涯苦作舟",
                                    " (´･ω･｀)转眼间一周就过了一半了呢",
                                    " ( ￣ ▽￣)o╭╯明天就是周末了！",
                                    " o(*≧▽≦)ツ周六浪起来~",
                                    " (╭￣3￣)╭♡ 忘记明天是周一吧"};
    private int HaveNetFlag=0;
    private int WrongNet=1;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private BuildingFragment buildingfragment;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.setTitle("主页");
        HomePageFragment homepagefragment= new HomePageFragment();
        manager = this.getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, homepagefragment);
        transaction.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebInit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

        webView = (WebView)findViewById(R.id.web);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bug_deliver) {
            String[] email = {"thinkwee2767@gmail.com"}; // 需要注意，email必须以数组形式传入
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822"); // 设置邮件格式
            intent.putExtra(Intent.EXTRA_EMAIL, email); // 接收人
            intent.putExtra(Intent.EXTRA_SUBJECT, "BuptRoom错误报告"); // 主题
            intent.putExtra(Intent.EXTRA_TEXT, "Hi~ o(*￣▽￣*)ブ我在使用BuptRoom时遇到了以下问题，请尽快解决:\n"); // 正文
            startActivity(Intent.createChooser(intent, "请选择邮件类应用以发送错误报告"));
            return true;
        }else
        if(id==R.id.timeinfo){
            final TimeInfo timeinfo= new TimeInfo();
            timeinfo.timesetting();
            Snackbar.make(webView, "今天是"+timeinfo.Timestring+" "+timeinfo.nowtime+"\n"+interesting[timeinfo.daycount], Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        return super.onOptionsItemSelected(item);
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
            if (id == R.id.jiaoshi) {
              showAlertDialog();
            }
            else if (id == R.id.developer_opensource) {
                this.setTitle("作者及开源信息");
                AboutFragment aboutfragment= new AboutFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, aboutfragment);
            }
            else if (id == R.id.version) {
                this.setTitle("版本说明");
                VersionFragment versionfragment= new VersionFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, versionfragment);
                transaction.commit();
            }
            else if (id == R.id.homepage) {
                this.setTitle("主页");
                HomePageFragment homepagefragment= new HomePageFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, homepagefragment);
                transaction.commit();
            }
            else if (id==R.id.theme_choose){
                Intent intent= new Intent();
                intent.setClassName(this,"thinkwee.buptroom.SettingActivity");
                startActivity(intent);
            }
        }else{
            if (id == R.id.jiaoshi) {
                this.setTitle("教室查询");
                buildingfragment= new BuildingFragment();
                Bundle bundle=new Bundle();
                bundle.putString("htmlbody",htmlbody);
                buildingfragment.setArguments(bundle);
                buildingfragment.Init();;
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, buildingfragment);
                transaction.commit();
            }
            else if (id == R.id.developer_opensource) {
                this.setTitle("作者及开源信息");
                AboutFragment aboutfragment= new AboutFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, aboutfragment);
                transaction.commit();
            }
            else if (id == R.id.version) {
                this.setTitle("版本说明");
                VersionFragment versionfragment= new VersionFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, versionfragment);
                transaction.commit();

            }
            else if (id == R.id.homepage) {
                this.setTitle("主页");
                HomePageFragment homepagefragment= new HomePageFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, homepagefragment);
                transaction.commit();
            }
            else if (id==R.id.theme_choose){
                Intent intent= new Intent();
                intent.setClassName(this,"thinkwee.buptroom.SettingActivity");
                startActivity(intent);
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
            //Log.i(Tag,"html content: "+htmlContent);
            document= Jsoup.parse(htmlContent);
            htmlbody=document.getElementsByTag("body").text();
            HaveNetFlag=1;
            if (htmlbody.contains("楼"))   {Notification_show("教室信息拉取完成，可以查看空闲教室");WrongNet=0;}
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
         * TODO:NOTIFICATION
         */

        String MainText=null;
        Notification.Builder mBuilder = new Notification.Builder(this);
        Bitmap LB=BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        mBuilder.setContentTitle("BuptRoom")                        //标题
                .setContentText(Notification_content)             //内容
                .setSubText("滑动取消此消息")                    //内容下面的一小段文字
                .setTicker("BuptRoom提醒")                      //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())              //设置通知时间
                .setSmallIcon(R.mipmap.ic_launcher)             //设置小图标
                .setLargeIcon(LB)                     //设置大图标
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                .setAutoCancel(true)                           //设置点击后取消Notification
                .setOngoing(false);                        //设置正在进行
        NotificationManager mNManager;
        mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNManager.notify(1, mBuilder.build());

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
        builder.setMessage("您所在网络环境不是校园网，无法得到空闲教室信息" +
                           "\n请连接到校园网后重启软件");
        builder.setNegativeButton("我知道了", null);
        builder.show();
    }


}
