package thinkwee.buptroom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by think on 20162016/10/12 001211:10
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:
 */

public class ShakeTestActivity extends AppCompatActivity {

    private ArrayList<String> text1 = new ArrayList<String>();
    private ArrayList<String> text2 = new ArrayList<String>();
    private ArrayList<String> text3 = new ArrayList<String>();

    private TextView tv1, tv2, tv3;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_test);

        //添加toolbar返回
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle("摇一摇(恶搞测试)");
        toolbar.setSubtitle("你摇出来的结果是");

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        text1.add("一只");
        text1.add("一打");
        text1.add("一坨");
        text1.add("一堆");
        text1.add("一条");

        text2.add("清秀的");
        text2.add("骨骼惊奇的");
        text2.add("精壮的");
        text2.add("风流的");
        text2.add("性感的");

        text3.add("傅里叶");
        text3.add("高斯");
        text3.add("拉普拉斯");
        text3.add("香农");
        text3.add("苏格拉底");


        tv1 = (TextView) findViewById(R.id.shaketext1);
        tv2 = (TextView) findViewById(R.id.shaketext2);
        tv3 = (TextView) findViewById(R.id.shaketext3);

        tv1.setText(text1.get(random.nextInt(5)));
        tv2.setText(text2.get(random.nextInt(5)));
        tv3.setText(text3.get(random.nextInt(5)));
    }
}
