package thinkwee.buptroom;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String htmlbody=null;
    private String[] interesting={" ヾ(o◕∀◕)ﾉ新的一周新的开始!",
                                    " π__π默默学习不说话",
                                    " （╯－＿－）╯╧╧ 学海无涯苦作舟",
                                    " (´･ω･｀)转眼间一周就过了一半了呢",
                                    " ( ￣ ▽￣)o╭╯明天就是周末了！",
                                    " o(*≧▽≦)ツ周六浪起来~",
                                    " (╭￣3￣)╭♡ 忘记明天是周一吧"};

    private int WrongNet=1;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private BuildingFragment buildingfragment;
    private Button snackbartemp;
    private ImageButton ProfileBt;
    public static final String TAG = "MainActivity";
    private int startcounts=0;

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
        ProfileBt=(ImageButton)headview.findViewById(R.id.profile);

        ProfileBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.putExtra("StartCounts",startcounts);
                intent.setClassName(MainActivity.this,"thinkwee.buptroom.ProfileActivity");
                startActivity(intent);
            }
        });

        AppStartCounts(MainActivity.this);




    }

    public void AppStartCounts(Context context){
        /**
         * Created by Thinkwee on 2016/10/13 0013 11:12
         * Parameter [context]上下文
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:统计软件启动次数 startcounts传到个人统计页面
         */
        try {
            File file=new File(context.getCacheDir(),"StartCounts.txt");
            if (file.exists()){
                FileInputStream fis=new FileInputStream(file);
                BufferedReader br=new BufferedReader(new InputStreamReader(fis));
                String temp=br.readLine();
                if (temp==null){
                    startcounts=1;
                    Log.i(TAG,"文件读错误");
                }
                else{
                    startcounts=Integer.parseInt(temp);
                    Log.i(TAG,"第"+temp+"次创建");
                }
                fis.close();
                br.close();
                FileOutputStream fos=new FileOutputStream(file);
                startcounts++;
                fos.write(Integer.toString(startcounts).getBytes());
                fos.close();
            }else{
                file.createNewFile();
                FileOutputStream fos=new FileOutputStream(file);
                FileInputStream fis=new FileInputStream(file);
                BufferedReader br=new BufferedReader(new InputStreamReader(fis));
                Log.i(TAG,"首次创建");
                fos.write(Integer.toString(startcounts).getBytes());
                fos.close();
                Log.i(TAG,br.readLine());
                br.close();
                fis.close();
            }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /**
         * Created by Thinkwee on 2016/10/12 0012 9:58
         * Parameter [item]
         * Return boolean
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:右上角菜单处理
         */

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
            Snackbar.make(snackbartemp, "今天是"+timeinfo.Timestring+" "+timeinfo.nowtime+"\n"+interesting[timeinfo.daycount], Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
                    if (isServiceWork(MainActivity.this,"thinkwee.buptroom.ShakeService")==true) {
                        Intent stopintent=new Intent(this,ShakeService.class);
                        stopService(stopintent);
                    }
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
            }
            else if (id == R.id.developer_opensource) {
                if (isServiceWork(MainActivity.this,"thinkwee.buptroom.ShakeService")==true) {
                    Intent stopintent=new Intent(this,ShakeService.class);
                    stopService(stopintent);
                }
                this.setTitle("作者及开源信息");
                AboutFragment aboutfragment= new AboutFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, aboutfragment);
            }
            else if (id == R.id.version) {
                if (isServiceWork(MainActivity.this,"thinkwee.buptroom.ShakeService")==true) {
                    Intent stopintent=new Intent(this,ShakeService.class);
                    stopService(stopintent);
                }
                this.setTitle("版本说明");
                VersionFragment versionfragment= new VersionFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, versionfragment);
                transaction.commit();
            }
            else if (id == R.id.homepage) {
                if (isServiceWork(MainActivity.this,"thinkwee.buptroom.ShakeService")==true) {
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
                if (isServiceWork(MainActivity.this,"thinkwee.buptroom.ShakeService")==true) {
                    Intent stopintent=new Intent(this,ShakeService.class);
                    stopService(stopintent);
                }
                Intent intent= new Intent();
                intent.setClassName(this,"thinkwee.buptroom.SettingActivity");
                startActivity(intent);
            }
            else  if (id==R.id.shake){
                this.setTitle("摇一摇");
                Intent intent = new Intent();
                intent.setClass(this, ShakeService.class);
                startService(intent);
                ShakeFragment shakefragment=new ShakeFragment();
                manager = this.getFragmentManager();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, shakefragment);
                transaction.commit();
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
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


}
