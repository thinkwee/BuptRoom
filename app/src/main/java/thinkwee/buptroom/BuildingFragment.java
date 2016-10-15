package thinkwee.buptroom;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 20162016/10/6 000614:05
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:教室查询界面
 */



public  class BuildingFragment extends Fragment {
    @Nullable
    private View v;
    private EmptyRoom emptyroom=new EmptyRoom();
    private PagerTabStrip strip;
    String htmlbody;
    View view1,view2,view3,view4,view5;
    ViewPager viewpager;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_building, container, false);
        viewpager = (ViewPager) v.findViewById(R.id.viewpager);
        strip=(PagerTabStrip)v.findViewById(R.id.pager_title);
        strip.setDrawFullUnderline(false);
        strip.setTextSpacing(5);
        strip.setTabIndicatorColorResource(R.color.colorPrimary);

       //把各个layout转成view,加入ViewPager中
        view1 = inflater.inflate(R.layout.building_1, container, false);
        view1.setScrollContainer(true);
        SetPage(view1,"教一楼");

        view2 = inflater.inflate(R.layout.building_2, container, false);
        view2.setScrollContainer(true);
        SetPage(view2,"教二楼");

        view3 = inflater.inflate(R.layout.building_3, container, false);
        view3.setScrollContainer(true);
        SetPage(view3,"教三楼");

        view4 = inflater.inflate(R.layout.building_4, container, false);
        view4.setScrollContainer(true);
        SetPage(view4,"教四楼");

        view5 = inflater.inflate(R.layout.building_library, container, false);
        view5.setScrollContainer(true);
        SetPage(view5,"图书馆");

        ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);

        //加标题
        ArrayList<String> titles= new ArrayList<String>();
        titles.add("教一楼空闲教室");
        titles.add("教二空闲教室");
        titles.add("教三空闲教室");
        titles.add("教四空闲教室");
        titles.add("图书馆空闲教室");

        MYViewPagerAdapter adapter = new MYViewPagerAdapter(views,titles);
        viewpager.setAdapter(adapter);
        return v;
    }

    public void  Init(){
        htmlbody=getArguments().getString("htmlbody");
    }

    public class MYViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> views;
        private ArrayList<String> titles;

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {

            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }

        //设置view和标题
        public MYViewPagerAdapter(ArrayList<View> views,ArrayList<String> titles){
            this.views=views;
            this.titles=titles;
        }

        @Override
        //设置标题字体和颜色
        public CharSequence getPageTitle(int position){
            SpannableStringBuilder ssb = new SpannableStringBuilder(titles.get(position));
            ssb.setSpan(new RelativeSizeSpan(1.4f), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(242,242,242));
            ssb.setSpan(fcs, 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }
    }

    public void SetPage(View view,String buildingname) {
        /**
         * Created by Thinkwee on 2016/10/12 0012 9:56
         * Parameter [view, buildingname]要显示文字的view,显示内容所属的楼名
         * Return void
         * CLASS:BuildingFragment
         * FILE:BuildingFragment.java
         * TODO:给每个楼的view(layout)添加内容，直接调用封装好的EmptyRoom
         */

        TextView t12, t34, t56, t78, t9, t1011;
        ArrayList<String> tempclass = new ArrayList<String>();
        tempclass.clear();
        tempclass = emptyroom.get_show_content(buildingname, htmlbody);
        t12 = (TextView) view.findViewById(R.id.jie12);
        t12.setText(tempclass.get(0));
        t34 = (TextView) view.findViewById(R.id.jie34);
        t34.setText(tempclass.get(1));
        t56 = (TextView) view.findViewById(R.id.jie56);
        t56.setText(tempclass.get(2));
        t78 = (TextView) view.findViewById(R.id.jie78);
        t78.setText(tempclass.get(3));
        t9 = (TextView) view.findViewById(R.id.jie9);
        t9.setText(tempclass.get(4));
        t1011 = (TextView) view.findViewById(R.id.jie1011);
        t1011.setText(tempclass.get(5));
    }
}
