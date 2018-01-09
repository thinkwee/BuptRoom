package thinkwee.buptroom;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import thinkwee.buptroom.services.ShakeService;
import thinkwee.buptroom.services.TimeInfo;
import thinkwee.buptroom.services.Webget;
import thinkwee.buptroom.fragment.AboutFragment;
import thinkwee.buptroom.fragment.BuildingFragment;
import thinkwee.buptroom.fragment.HomePageFragment;
import thinkwee.buptroom.fragment.ShakeFragment;
import thinkwee.buptroom.fragment.VersionFragment;
import thinkwee.buptroom.utils.CustomPopDialog;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String htmlbody = "";
    private String[] interesting = {" ヾ(o◕∀◕)ﾉ新的一周新的开始!",
            " π__π默默学习不说话",
            " （╯－＿－）╯╧╧ 学海无涯苦作舟",
            " (´･ω･｀)转眼间一周就过了一半了呢",
            " ( ￣ ▽￣) o╭╯明天就是周末了！",
            " o(*≧▽≦)ツ周六浪起来~",
            " (╭￣3￣)╭♡ 忘记明天是周一吧"};

    private int WrongNet = 1;//网络状况标志位
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Button snackbartemp;
    public static final String TAG = "MainActivity";
    private int startcounts = 0;

    private TimeInfo timeshow;
    private WebView webview;
    private Webget webget;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        htmlbody = intent.getStringExtra("HtmlBody");
        WrongNet = intent.getIntExtra("WrongNet", 1);

        if (WrongNet == 0) {
            showAlertDialog(8);
        } else {
            showAlertDialog(9);
        }

        this.setTitle("首页");
        HomePageFragment homepagefragment = new HomePageFragment();
        manager = this.getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, homepagefragment);
        transaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        snackbartemp = (Button) findViewById(R.id.snackbar_temp);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headview = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageButton profileBt = (ImageButton) headview.findViewById(R.id.profile);

        SharedPreferences sharedPreferences = getSharedPreferences("colorsave", Context.MODE_PRIVATE);
        int maincolor = sharedPreferences.getInt("maincolor", 0);
        Log.i("maincolor", maincolor + "");

        Window window = MainActivity.this.getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色

        if (maincolor != 0) {
            window.setStatusBarColor(maincolor);
            toolbar.setBackgroundColor(maincolor);
        }

        timeshow = new TimeInfo();

        profileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("StartCounts", startcounts);
                intent.setClassName(MainActivity.this, "thinkwee.buptroom.activity.ProfileActivity");
                startActivity(intent);
            }
        });
        AppStartCounts();
        if (WrongNet == 1) {
            try {
                if (htmlbody == null || !htmlbody.contains("楼"))
                    if (CheckDownloadHtml(MainActivity.this)) {
                        showAlertDialog(1);
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*用于支持自动更新*/
        UpdateKey.API_TOKEN = "70b578b5b400d811889ded55b450435e";
        UpdateKey.APP_ID = "580582f5959d69785500182a";
        UpdateKey.DialogOrNotification = UpdateKey.WITH_DIALOG;
        UpdateFunGO.init(this);

        webview = (WebView) findViewById(R.id.GetHtml2);
        webget = new Webget();


    }

    public boolean CheckDownloadHtml(Context context) throws IOException {
        /*
         * Created by Thinkwee on 2016/10/15 0015 11:42
         * Parameter [context] 上下文
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         */
        File file = new File(context.getCacheDir(), "DownloadHtml.txt");
        if (!file.exists()) return false;
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String temp = br.readLine();
        timeshow = new TimeInfo();
        timeshow.timesetting();
        if (temp.equals(timeshow.Timestring)) {
//            Log.i(TAG, "从离线文件中获取内容");
//            Log.i(TAG,"读取离线测试"+temp+"\n");
            WrongNet = 0;
            htmlbody = br.readLine();
            return true;
        } else
            return false;
    }

    public void AppStartCounts() {
        /*
         * Created by Thinkwee on 2016/10/13 0013 11:12
         * Parameter [context]上下文
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         */

        SharedPreferences sharedPreferences = getSharedPreferences("startcount", Context.MODE_PRIVATE);
        startcounts = sharedPreferences.getInt("startcount", 0);
        startcounts++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("startcount", startcounts);
        editor.apply();//提交修改
    }

    public void DownloadHtml(Context context) {
        /*
         * Created by Thinkwee on 2016/10/13 0013 11:12
         * Parameter [context]上下文
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         */
        try {
            File file = new File(context.getCacheDir(), "DownloadHtml.txt");
            if (!file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, false);
            final TimeInfo timeinfo = new TimeInfo();
            timeinfo.timesetting();
            fos.write((timeinfo.Timestring + "\n").getBytes());
            fos.write((htmlbody).getBytes());
//            Log.i(TAG,"已离线htmlbody"+ htmlbody);
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
        /*
         * Created by Thinkwee on 2016/10/12 0012 9:58
         * Parameter [item]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
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
        } else if (id == R.id.timeinfo) {
            timeshow.timesetting();
            showAlertDialog(2);
        } else if (id == R.id.download) {
            if (WrongNet == 1) {
//                Log.i(TAG, "离线下载错误");
                showAlertDialog(3);

            } else {
                try {
                    if (CheckDownloadHtml(MainActivity.this)) {
//                        Log.i(TAG, "今日已经离线");
                        showAlertDialog(4);
                    } else {
                        DownloadHtml(MainActivity.this);
//                        Log.i(TAG, "离线成功");
                        showAlertDialog(5);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (id == R.id.update) {
            UpdateFunGO.manualStart(this);
        } else if (id == R.id.share) {
            Resources r = MainActivity.this.getResources();
            Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.share_app);
            CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(MainActivity.this);
            dialogBuild.setImage(bmp);
            CustomPopDialog dialog = dialogBuild.create();
            dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
            dialog.show();
        } else if (id == R.id.refresh) {
            webget.init(webview);
            webget.WebInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //do something
                    WrongNet = webget.getWrongnet();
                    htmlbody = webget.getHtmlbody();
                }
            }, 2000);
            if (WrongNet == 0) {
                showAlertDialog(8);
            } else {
                showAlertDialog(9);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /*
         * Created by Thinkwee on 2016/10/12 0012 9:58
         * Parameter []
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {
            showAlertDialog(6);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*
         * Created by Thinkwee on 2016/9/28 0028 9:27
         * Parameter [item]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         */

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.jiaoshi) {
            if (WrongNet == 1) {
                showAlertDialog(7);
            } else {
                if (isServiceWork(MainActivity.this)) {
                    Intent stopintent = new Intent(this, ShakeService.class);
                    stopService(stopintent);
                }
                this.setTitle("教室查询");
                BuildingFragment buildingfragment = new BuildingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("htmlbody", htmlbody);
                buildingfragment.setArguments(bundle);
                buildingfragment.Init();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, buildingfragment);
                transaction.commit();
            }
        } else if (id == R.id.developer_opensource) {
            if (isServiceWork(MainActivity.this)) {
                Intent stopintent = new Intent(this, ShakeService.class);
                stopService(stopintent);
            }
            this.setTitle("软件信息");
            AboutFragment aboutfragment = new AboutFragment();
            manager = this.getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.frame, aboutfragment);
            transaction.commit();
        } else if (id == R.id.version) {
            if (isServiceWork(MainActivity.this)) {
                Intent stopintent = new Intent(this, ShakeService.class);
                stopService(stopintent);
            }
            this.setTitle("版本介绍");
            VersionFragment versionfragment = new VersionFragment();
            manager = this.getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.frame, versionfragment);
            transaction.commit();
        } else if (id == R.id.homepage) {
            if (isServiceWork(MainActivity.this)) {
                Intent stopintent = new Intent(this, ShakeService.class);
                stopService(stopintent);
            }
            this.setTitle("首页");
            HomePageFragment homepagefragment = new HomePageFragment();
            manager = this.getFragmentManager();
            transaction = manager.beginTransaction();
            transaction.replace(R.id.frame, homepagefragment);
            transaction.commit();
        } else if (id == R.id.theme_choose) {
            if (isServiceWork(MainActivity.this)) {
                Intent stopintent = new Intent(this, ShakeService.class);
                stopService(stopintent);
            }
            Intent intent = new Intent();
            intent.setClassName(this, "thinkwee.buptroom.activity.SettingActivity");
            startActivity(intent);
        } else if (id == R.id.shake) {
            if (WrongNet == 1) {
                showAlertDialog(7);
            } else {
                this.setTitle("摇一摇");
                Intent intent = new Intent();
                intent.putExtra("htmlbody", htmlbody);
                intent.setClass(this, ShakeService.class);
                startService(intent);
                ShakeFragment shakefragment = new ShakeFragment();
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

    private void showAlertDialog(int wrongnum) {
        /*
         * Created by Thinkwee on 2016/9/28 0028 9:29
         * Parameter []
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         */
        Handler dishandler;
        dishandler = new DisplayHandler();
        Message msg = dishandler.obtainMessage();
        msg.what = wrongnum;
        dishandler.sendMessage(msg);

    }

    public boolean isServiceWork(Context mContext) {
        /*
         * Created by Thinkwee on 2016/10/12 0012 19:30
         * Parameter [mContext, serviceName]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
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
        assert myAM != null;
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals("thinkwee.buptroom.services.ShakeService")) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

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

    class DisplayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Snackbar.make(snackbartemp, "已从离线内容中加载", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                case 2:
                    Snackbar.make(snackbartemp, "今天是" + timeshow.Timestring + " " + timeshow.nowtime + "\n" + interesting[timeshow.daycount], Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                case 3:
                    Snackbar.make(snackbartemp, "离线下载错误", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                case 4:
                    Snackbar.make(snackbartemp, "今日已经离线", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                case 5:
                    Snackbar.make(snackbartemp, "离线成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                case 6:
                    new AlertDialog.Builder(MainActivity.this).setTitle("确认退出吗？")
                            .setIcon(R.drawable.logoko)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    if (isServiceWork(MainActivity.this)) {
                                        Intent stopintent = new Intent(MainActivity.this, ShakeService.class);
                                        Log.i(TAG, "The ShakeService has been closed");
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
                    break;
                case 7:
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("网络错误");
                    builder.setMessage("您所在网络环境不是校园网，无法得到空闲教室信息" +
                            "\n请连接到校园网后重启软件");
                    builder.setNegativeButton("我知道了", null);
                    builder.show();
                    break;
                case 8:
                    Toast netcheckok = Toast.makeText(getApplicationContext(),
                            "", Toast.LENGTH_LONG);
                    netcheckok.setGravity(Gravity.TOP, 0, 200);
                    netcheckok.setText("教室信息已更新");
                    netcheckok.show();
                    break;
                case 9:
                    Toast netcheckwrong = Toast.makeText(getApplicationContext(),
                            "", Toast.LENGTH_LONG);
                    netcheckwrong.setGravity(Gravity.TOP, 0, 200);
                    netcheckwrong.setText("教室信息更新失败，请刷新");
                    netcheckwrong.show();
                default:
                    break;

            }
        }
    }

}
