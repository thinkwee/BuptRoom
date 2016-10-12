package thinkwee.buptroom;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by think on 20162016/10/9 000920:57
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:欢迎界面，在Manifest中设置此Activity为启动Activity,设置为全屏，设置为一段时间后跳转到主Activity
 */

public class WelcomeActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo("thinkwee.buptroom", 0);

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
                startActivity(intent);
                WelcomeActivity.this.finish();
            }

        }, 2500);
    }
}
