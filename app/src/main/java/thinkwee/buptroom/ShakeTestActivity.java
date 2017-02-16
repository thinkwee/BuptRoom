package thinkwee.buptroom;

import android.app.Activity;
import android.content.Intent;
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
 * TODO:摇一摇推荐教室，推荐方法是从摇的时刻拥有最多空闲教室的教学楼中随机出一个教室
 */

public class ShakeTestActivity extends AppCompatActivity {

    private Random random = new Random();
    private int nowclass=0;//现在时间所处节数，用于emptyroom.get_show_content返回数组的下标
    private TimeInfo timeinfo=new TimeInfo();
    private String htmlbody="";
    private ArrayList<String> BuildingName=new ArrayList<String>();
    private ArrayList<String> tempclass = new ArrayList<String>();
    private EmptyRoom emptyroom=new EmptyRoom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_test);
        Intent intent=getIntent();
        htmlbody=intent.getStringExtra("htmlbody");
        //添加toolbar返回
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle("摇一摇");
        toolbar.setSubtitle("你摇出来的结果是");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        TextView recommandroom = (TextView) findViewById(R.id.RecommandRoom);
        Init();
        recommandroom.setText(Handle());
    }

    private void Init(){
        BuildingName.add("教一楼");
        BuildingName.add("教二楼");
        BuildingName.add("教三楼");
        BuildingName.add("教四楼");
        BuildingName.add("图书馆");
        timeinfo.timesetting();
        if (timeinfo.nowtime.contains("12")){
            nowclass=0;
        }else
        if (timeinfo.nowtime.contains("34")){
            nowclass=1;
        }else
        if (timeinfo.nowtime.contains("56")){
            nowclass=2;
        }else
        if (timeinfo.nowtime.contains("78")){
            nowclass=3;
        }else
        if (timeinfo.nowtime.contains("9")){
            nowclass=4;
        }else
        if (timeinfo.nowtime.contains("10")){
            nowclass=5;
        }else
        if (timeinfo.nowtime.contains("休息")){
            nowclass=6;
        }
    }

    private String Handle(){
        int countmax=0;
        int maxi=0;
        String temp="";
        String result="";
        int roomcount=0;
        ArrayList<String> resultroom=new ArrayList<String>();
        int len=0;
        if (nowclass==6)
            return "现在是休息时间";
        else{
            for (int i=0;i<5;i++) {
                tempclass.clear();
                tempclass = emptyroom.get_show_content(BuildingName.get(i), htmlbody);
                if (tempclass.get(nowclass).length() > countmax) {
                    countmax = tempclass.get(nowclass).length();
                    maxi = i;
                }
            }
            tempclass.clear();
            tempclass=emptyroom.get_show_content(BuildingName.get(maxi), htmlbody);
            temp=tempclass.get(nowclass);
            for (int i=0;i<countmax;i++)
            {
                if (temp.charAt(i)=='-'){
                    resultroom.add(temp.substring(i+1,i+4));
                    roomcount++;
                }
            }
            result=BuildingName.get(maxi)+"\n"+resultroom.get(random.nextInt(roomcount));
            return result;
        }
    }

}
