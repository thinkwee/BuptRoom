package thinkwee.buptroom;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String htmlbody="";
    private String[] interesting={" ヾ(o◕∀◕)ﾉ新的一周新的开始!",
                                    " π__π默默学习不说话",
                                    " （╯－＿－）╯╧╧ 学海无涯苦作舟",
                                    " (´･ω･｀)转眼间一周就过了一半了呢",
                                    " ( ￣ ▽￣) o╭╯明天就是周末了！",
                                    " o(*≧▽≦)ツ周六浪起来~",
                                    " (╭￣3￣)╭♡ 忘记明天是周一吧"};

    private int WrongNet=1;//网络状况标志位
    private int maincolor=0,imgnum=0;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Button snackbartemp;
    public static final String TAG = "MainActivity";
    private int startcounts=0;
    private ArrayList<Integer> wallpapers=new ArrayList<>();
    private Toolbar toolbar;
    private LinearLayout mainlayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        htmlbody=intent.getStringExtra("HtmlBody");
        WrongNet=intent.getIntExtra("WrongNet",0);
        this.setTitle("首页");
        HomePageFragment homepagefragment= new HomePageFragment();
        manager = this.getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, homepagefragment);
        transaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        snackbartemp=(Button)findViewById(R.id.snackbar_temp);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headview=navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageButton profileBt = (ImageButton) headview.findViewById(R.id.profile);

        wallpapersinit();
        SharedPreferences sharedPreferences = getSharedPreferences("colorsave", Context.MODE_APPEND);
        maincolor = sharedPreferences.getInt("maincolor", 0);
        imgnum = sharedPreferences.getInt("imgnum", -1);
        mainlayout = (LinearLayout)findViewById(R.id.content_main);

        Window window = MainActivity.this.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色

        if (maincolor!=0) {
            window.setStatusBarColor(maincolor);
            toolbar.setBackgroundColor(maincolor);
        }

//        if (imgnum!=-1) {
//            mainlayout.setBackgroundResource(wallpapers.get(imgnum));
//        }


        profileBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.putExtra("StartCounts",startcounts);
                intent.setClassName(MainActivity.this,"thinkwee.buptroom.ProfileActivity");
                startActivity(intent);
            }
        });
        AppStartCounts();
        if (WrongNet==1){
            try {
                if (htmlbody == null || !htmlbody.contains("楼"))
                    if(CheckDownloadHtml(MainActivity.this))
                        Snackbar.make(snackbartemp, "已从离线内容中加载", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*用于支持自动更新*/
        UpdateKey.API_TOKEN = "70b578b5b400d811889ded55b450435e";
        UpdateKey.APP_ID = "580582f5959d69785500182a";
        UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;
        UpdateFunGO.init(this);
    }

    public boolean CheckDownloadHtml(Context context) throws IOException {
        /**
         * Created by Thinkwee on 2016/10/15 0015 11:42
         * Parameter [context] 上下文
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:检查离线内容，若有今天离线则从离线文件中更新htmlbody
         */
        File file=new File(context.getCacheDir(),"DownloadHtml.txt");
        if(!file.exists()) return false;
        FileInputStream fis=new FileInputStream(file);
        BufferedReader br=new BufferedReader(new InputStreamReader(fis));
        String temp=br.readLine();
        final TimeInfo timeinfo= new TimeInfo();
        timeinfo.timesetting();
        if (temp.equals(timeinfo.Timestring)) {
            Log.i(TAG, "从离线文件中获取内容");
            Log.i(TAG,"读取离线测试"+temp+"\n");
            WrongNet=0;
            htmlbody=br.readLine();
            return true;
        }else
            return false;
    }

    public void AppStartCounts(){
        /**
         * Created by Thinkwee on 2016/10/13 0013 11:12
         * Parameter [context]上下文
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:统计软件启动次数 startcounts传到个人统计页面
         */

        SharedPreferences sharedPreferences = getSharedPreferences("startcount", Context.MODE_APPEND);
        startcounts = sharedPreferences.getInt("startcount", 0);
        startcounts++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("startcount", startcounts);
        editor.commit();//提交修改
    }

    public void DownloadHtml(Context context){
        /**
         * Created by Thinkwee on 2016/10/13 0013 11:12
         * Parameter [context]上下文
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:离线下载html
         */
        try {
            File file=new File(context.getCacheDir(),"DownloadHtml.txt");
            if (!file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos=new FileOutputStream(file,false);
            final TimeInfo timeinfo= new TimeInfo();
            timeinfo.timesetting();
            fos.write((timeinfo.Timestring+"\n").getBytes());
            fos.write((htmlbody).getBytes());
            Log.i(TAG,"已离线htmlbody"+ htmlbody);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        int id = item.getItemId();
        /**
         * Created by Thinkwee on 2016/10/12 0012 9:58
         * Parameter [item]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:右上角菜单处理
         */
        if (id == R.id.feedback) {
            String[] email = {"thinkwee2767@gmail.com"}; // 需要注意，email必须以数组形式传入
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822"); // 设置邮件格式
            intent.putExtra(Intent.EXTRA_EMAIL, email); // 接收人
            intent.putExtra(Intent.EXTRA_SUBJECT, "BuptRoom反馈"); // 主题
            intent.putExtra(Intent.EXTRA_TEXT, "Hi~ o(*￣▽￣*)ブ我在使用BuptRoom时遇到了以下问题，请尽快解决:\n"); // 正文
            startActivity(Intent.createChooser(intent, "请选择邮件类应用以发送错误报告"));
            return true;
        }else
        if(id==R.id.timeinfo){
            final TimeInfo timeinfo= new TimeInfo();
            timeinfo.timesetting();
            Snackbar.make(snackbartemp, "今天是"+timeinfo.Timestring+" "+timeinfo.nowtime+"\n"+interesting[timeinfo.daycount], Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else
        if (id==R.id.download){
            if (WrongNet==1){
                Log.i(TAG,"离线下载错误");
                Snackbar.make(snackbartemp, "离线下载错误", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else{
                try {
                    if (CheckDownloadHtml(MainActivity.this)){
                        Log.i(TAG,"今日已经离线");
                        Snackbar.make(snackbartemp, "今日已经离线", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else{
                        DownloadHtml(MainActivity.this);
                        Log.i(TAG,"离线成功");
                        Snackbar.make(snackbartemp, "离线成功", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else
        if (id==R.id.update){
            UpdateFunGO.manualStart(this);
        }else
        if (id==R.id.share){
            Resources r = MainActivity.this.getResources();
            Bitmap bmp=BitmapFactory.decodeResource(r, R.drawable.share_app);
            CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(MainActivity.this);
            dialogBuild.setImage(bmp);
            CustomPopDialog dialog = dialogBuild.create();
            dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
            /**
             * Created by Thinkwee on 2016/10/12 0012 9:58
             * Parameter []
             * Return void
             * CLASS:MainActivity
             * FILE:MainActivity.java
             * TODO:对实体后退键处理，分抽屉打开和关上两种情况
             */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this).setTitle("确认退出吗？")
                    .setIcon(R.mipmap.launcher)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“确认”后的操作
                            if (isServiceWork(MainActivity.this,"thinkwee.buptroom.ShakeService")) {
                                Intent stopintent=new Intent(MainActivity.this,ShakeService.class);
                                Log.i(TAG,"The ShakeService has been closed");
                                stopService(stopintent);
                            }
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                        }
                    }).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:27
         * Parameter [item]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:侧边栏点击事件 分Wrongnet即网络连接正确或错误
         */

        // Handle navigation view item clicks here.
        int id = item.getItemId();
            if (id == R.id.jiaoshi) {
                if (WrongNet==1){
                    showAlertDialog();
                }
                else{
                    if (isServiceWork(MainActivity.this, "thinkwee.buptroom.ShakeService")) {
                        Intent stopintent=new Intent(this,ShakeService.class);
                        stopService(stopintent);
                    }
                    this.setTitle("教室查询");
                    BuildingFragment buildingfragment = new BuildingFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("htmlbody",htmlbody);
                    buildingfragment.setArguments(bundle);
                    buildingfragment.Init();
                    manager = this.getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, buildingfragment);
                    transaction.commit();
                }
            }
            else if (id == R.id.developer_opensource) {
                if (isServiceWork(MainActivity.this, "thinkwee.buptroom.ShakeService")) {
                    Intent stopintent=new Intent(this,ShakeService.class);
                    stopService(stopintent);
                }
                this.setTitle("软件信息");
                AboutFragment aboutfragment= new AboutFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, aboutfragment);
                transaction.commit();
            }
            else if (id == R.id.version) {
                if (isServiceWork(MainActivity.this, "thinkwee.buptroom.ShakeService")) {
                    Intent stopintent=new Intent(this,ShakeService.class);
                    stopService(stopintent);
                }
                this.setTitle("版本介绍");
                VersionFragment versionfragment= new VersionFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, versionfragment);
                transaction.commit();
            }
            else if (id == R.id.homepage) {
                if (isServiceWork(MainActivity.this, "thinkwee.buptroom.ShakeService")) {
                    Intent stopintent=new Intent(this,ShakeService.class);
                    stopService(stopintent);
                }
                this.setTitle("首页");
                HomePageFragment homepagefragment= new HomePageFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, homepagefragment);
                transaction.commit();
            }
            else if (id==R.id.theme_choose){
                if (isServiceWork(MainActivity.this, "thinkwee.buptroom.ShakeService")) {
                    Intent stopintent=new Intent(this,ShakeService.class);
                    stopService(stopintent);
                }
                Intent intent= new Intent();
                intent.setClassName(this,"thinkwee.buptroom.SettingActivity");
                startActivity(intent);
            }
            else  if (id==R.id.shake){
                if (WrongNet==1){
                    showAlertDialog();
                }
                else{
                    this.setTitle("摇一摇");
                    Intent intent = new Intent();
                    intent.putExtra("htmlbody",htmlbody);
                    intent.setClass(this, ShakeService.class);
                    startService(intent);
                    ShakeFragment shakefragment=new ShakeFragment();
                    manager = this.getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, shakefragment);
                    transaction.commit();
                }

            }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public boolean isServiceWork(Context mContext, String serviceName) {
        /**
         * Created by Thinkwee on 2016/10/12 0012 19:30
         * Parameter [mContext, serviceName]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:/**
         * 判断某个服务是否正在运行的方法
         *
         * @param mContext
         * @param serviceName
         *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
         * @return true代表正在运行，false代表服务没有正在运行
         */

        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public void wallpapersinit(){
        wallpapers.add(R.drawable.ailv);
        wallpapers.add(R.drawable.chabai);
        wallpapers.add(R.drawable.chase);
        wallpapers.add(R.drawable.chi);
        wallpapers.add(R.drawable.dai);
        wallpapers.add(R.drawable.dailan);
        wallpapers.add(R.drawable.dianqing);
        wallpapers.add(R.drawable.feise);
        wallpapers.add(R.drawable.guan);
        wallpapers.add(R.drawable.hupo);
        wallpapers.add(R.drawable.jiangzi);
        wallpapers.add(R.drawable.li);
        wallpapers.add(R.drawable.qiuxiangse);
        wallpapers.add(R.drawable.shuilv);
        wallpapers.add(R.drawable.tan);
        wallpapers.add(R.drawable.tuose);
        wallpapers.add(R.drawable.yan);
        wallpapers.add(R.drawable.yanzhi);
        wallpapers.add(R.drawable.yaqing);
        wallpapers.add(R.drawable.yase);
        wallpapers.add(R.drawable.yuebai);
        wallpapers.add(R.drawable.zhuqing);
    }


    //用于支持自动更新
    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }

}
