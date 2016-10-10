package thinkwee.buptroom;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by think on 20162016/9/29 002917:03
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:获取空闲教室信息工具
 * 调用方法:
 * EmptyRoom emptyroom=new EmprtRoom();
 * String Building_Room_empty=emptyroom.get_show_content("楼名",htmlbody);
 */

public class EmptyRoom {

    private String[] RawRoomInfo=null;//将htmlbody去逗号，按空格和冒号分割后得到的优化信息
    private int count=0;
    private ArrayList<String> j12=new ArrayList<String>();
    private ArrayList<String> j34=new ArrayList<String>();
    private ArrayList<String> j56=new ArrayList<String>();
    private ArrayList<String> j78=new ArrayList<String>();
    private ArrayList<String> j9=new ArrayList<String>();
    private ArrayList<String> j1011=new ArrayList<String>();
    private int tsgflag=0;
    private int cishu=0;

    public EmptyRoom() {
    }

    public ArrayList<String> get_show_content(String keyword, String htmlbody){
        /**
         * Created by Thinkwee on 2016/9/29 0029 17:54
         * Parameter [keyword, htmlbody] 楼名，网页拉取到的html_body内容
         * Return java.lang.String
         * CLASS:EmptyRoom
         * FILE:EmptyRoom.java
         * TODO:
         */

        String htmlbodytemp=htmlbody;
        ArrayList<String> result= new ArrayList<String>();
        htmlbody="";
        String[] contentstemp=htmlbodytemp.split(",");
        for (String temp:contentstemp){
            htmlbody=htmlbody+temp;
        }
        RawRoomInfo=htmlbody.split(" |:");
        String showcontent="";
        count=0;
        tsgflag=0;
        cishu=0;
        j12.clear();
        j34.clear();
        j56.clear();
        j78.clear();
        j9.clear();
        j1011.clear();
        if (keyword.contains("图书馆")) tsgflag=1;
        for (String temp:RawRoomInfo){
            if (temp.contains("2节")) cishu=1;else
            if (temp.contains("4节")) cishu=2;else
            if (temp.contains("6节")) cishu=3;else
            if (temp.contains("8节")) cishu=4;else
            if (temp.contains("9节")) cishu=5;else
            if (temp.contains("11节")) cishu=6;
            if (temp.contains(keyword)){
                if (tsgflag==1&&temp.length()<5) {
                    switch (cishu) {
                        case 1:
                            j12.add("图书馆一层");
                            break;
                        case 2:
                            j34.add("图书馆一层");
                            break;
                        case 3:
                            j56.add("图书馆一层");
                            break;
                        case 4:
                            j78.add("图书馆一层");
                            break;
                        case 5:
                            j9.add("图书馆一层");
                            break;
                        case 6:
                            j1011.add("图书馆一层");
                            break;
                        default:
                            break;
                    }
                }else if (tsgflag==0)  SaveBuidlingInfo(count,cishu);
            }
            count++;
        }
        result.add("第1|2节\n"+settlecontent(j12));
        result.add("第3|4节\n"+settlecontent(j34));
        result.add("第5|6节\n"+settlecontent(j56));
        result.add("第7|8节\n"+settlecontent(j78));
        result.add("第9节\n"+settlecontent(j9));
        result.add("第10|11节\n"+settlecontent(j1011));
        return result;
    }



    public void SaveBuidlingInfo(int k,int c){
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:28
         * Parameter [k, c]当前字符串数组下标，节次
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:分节次处理数据
         */

        k++;
        while (1 == 1) {
            if (RawRoomInfo[k].contains("楼") || RawRoomInfo[k].contains("节") || RawRoomInfo[k].contains("图"))         break;
            switch (c) {
                case 1:
                    j12.add(RawRoomInfo[k]);
                    break;
                case 2:
                    j34.add(RawRoomInfo[k]);
                    break;
                case 3:
                    j56.add(RawRoomInfo[k]);
                    break;
                case 4:
                    j78.add(RawRoomInfo[k]);
                    break;
                case 5:
                    j9.add(RawRoomInfo[k]);
                    break;
                case 6:
                    j1011.add(RawRoomInfo[k]);
                    break;
                default:
                    break;
            }
            k++;
        }
    }

    public String settlecontent(ArrayList<String> target){

        /**
         * Created by Thinkwee on 2016/9/28 0028 9:30
         * Parameter [target]
         * Return java.lang.String
         * CLASS:MainActivity
         * FILE:MainActivity.java
         * TODO:分楼层优化显示
         */


        if (target.isEmpty()) return "无空闲教室";

        int len=target.size();
        String f1="";
        String f2="";
        String f3="";
        String f4="";
        String f5="";
        String result="";

        for (int i=0;i<len;i++)
        {
            if (target.get(i).contains("-1")) f1=f1+target.get(i)+" ";else
            if (target.get(i).contains("-2")) f2=f2+target.get(i)+" ";else
            if (target.get(i).contains("-3")) f3=f3+target.get(i)+" ";else
            if (target.get(i).contains("-4")) f4=f4+target.get(i)+" ";else
            if (target.get(i).contains("-5")) f5=f5+target.get(i)+" ";else
            if (target.get(i).contains("图书")) result="图书馆一楼";
        }
        if (f1!="") result=result+"一楼："+f1+"\n";
        if (f2!="") result=result+"二楼："+f2+"\n";
        if (f3!="") result=result+"三楼："+f3+"\n";
        if (f4!="") result=result+"四楼："+f4+"\n";
        if (f5!="") result=result+"五楼："+f5+"\n";
        return result;

    }
}
