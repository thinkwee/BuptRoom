package thinkwee.buptroom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import android.view.View;

/**
 * Created by think on 20162016/10/9 00098:48
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:设置界面处理
 */

public class SettingActivity extends AppCompatActivity {

    private Button colorbt;
    private thinkwee.buptroom.ColorPicker colorpicker;
    private int getcolorint;
    private LinearLayout settinglayout;
    private String alpha,red,green,blue;
    private Toolbar toolbar;
    private static String TAG="COLOR";
    private int colorr[]={161,241,179,194,71,63,23,236,168,202,139,117,216,210,177,167,255,157,65,239,216,118};
    private int colorg[]={226,248,93,40,65,80,125,87,130,105,66,101,183,242,109,133,51,42,74,223,237,146};
    private int colorb[]={196,240,68,42,103,100,174,54,119,36,85,75,20,231,98,98,0,49,79,174,242,97};
    private int mindis=195075;//255*255*3
    private int maxnum=0;
    private int btflag=0;

    private ArrayList<Integer> wallpapers=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        this.setTitle("主题选择");
        wallpapersinit();

        //添加toolbar返回
        toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle("主题设置");
        toolbar.setSubtitle("好看吗");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
        colorbt=(Button)findViewById(R.id.getcolor);
        colorpicker=(thinkwee.buptroom.ColorPicker)findViewById(R.id.colorPicker);
        settinglayout=(LinearLayout)findViewById(R.id.settinglayout);
        colorbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btflag==0){
                    getcolorint=colorpicker.getColor();
                    Log.i(TAG,convertToARGB(getcolorint));//不可以删除,用于产生十六进制redbluegreen设置标题栏
                    mindis=195075;
                    maxnum=findmaxnum(Color.red(getcolorint),Color.green(getcolorint),Color.blue(getcolorint));
                    settinglayout.setBackgroundResource(wallpapers.get(maxnum));
                    toolbar.setBackgroundColor(Color.parseColor("#"+red+green+blue));
                    colorbt.setText("再选一次");
                    colorpicker.setVisibility(View.INVISIBLE);
                    btflag=1;
                }else{
                    colorpicker.setVisibility(View.VISIBLE);
                    colorbt.setText("选取颜色");
                    btflag=0;
                }
            }
        });
    }

    public int findmaxnum(int r,int g,int b){
        /**
         * Created by Thinkwee on 2016/10/26 0026 17:07
         * Parameter [r, g, b] 当前颜色的rgb
         * Return int
         * CLASS:SettingActivity
         * FILE:SettingActivity.java
         * TODO:与预设值比较，在RGB空间里找距离最短的图片作为适配图片
         */

        int i;
        int maxi=0;
        int temp=0;
        for (i=0;i<22;i++){
            temp=(r-colorr[i])*(r-colorr[i])+(g-colorg[i])*(g-colorg[i])+(b-colorb[i])*(b-colorb[i]);
            if (temp<mindis){
                maxi=i;
                mindis=temp;
            }
        }
        return maxi;
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

    /** 转化为ARGB格式字符串
     * For custom purposes. Not used by ColorPickerPreferrence
     * @param color
     * @author Unknown
     */
    public  String convertToARGB(int color) {
        alpha = Integer.toHexString(Color.alpha(color));
        red = Integer.toHexString(Color.red(color));
        green = Integer.toHexString(Color.green(color));
        blue = Integer.toHexString(Color.blue(color));

        if (alpha.length() == 1) {
            alpha = "0" + alpha;
        }

        if (red.length() == 1) {
            red = "0" + red;
        }

        if (green.length() == 1) {
            green = "0" + green;
        }

        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        return "#" + alpha + " "+red + " "+ green+ " " + blue;
    }




}
