package thinkwee.buptroom;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * Created by think on 20162016/10/9 000920:57
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:
 */

public class WelcomeActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo("thinkwee.buptroom", 0);
            TextView versionNumber = (TextView) findViewById(R.id.version_number);
            versionNumber.setText("v1.2.1");
            AssetManager mgr=getAssets();//得到AssetManager
            Typeface tf=Typeface.createFromAsset(mgr, "fonts/Gabriola.ttf");//根据路径得到Typeface
            TextView Title = (TextView) findViewById(R.id.welcome_title);
            Title.setTypeface(tf);//设置字体
            versionNumber.setTypeface(tf);
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

        }, 1200);
    }
}
