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
    private TextView tv;
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
         view1 = inflater.inflate(R.layout.building_1, container, false);
         view2 = inflater.inflate(R.layout.building_2, container, false);
         view3 = inflater.inflate(R.layout.building_3, container, false);
         view4 = inflater.inflate(R.layout.building_4, container, false);
         view5 = inflater.inflate(R.layout.building_library, container, false);
        tv=(TextView)view1.findViewById(R.id.show_content);
        tv.setText(emptyroom.get_show_content("教一楼",htmlbody));
        tv=(TextView)view2.findViewById(R.id.show_content);
        tv.setText(emptyroom.get_show_content("教二楼",htmlbody));
        tv=(TextView)view3.findViewById(R.id.show_content);
        tv.setText(emptyroom.get_show_content("教三楼",htmlbody));
        tv=(TextView)view4.findViewById(R.id.show_content);
        tv.setText(emptyroom.get_show_content("教四楼",htmlbody));
        tv=(TextView)view5.findViewById(R.id.show_content);
        tv.setText(emptyroom.get_show_content("图书馆",htmlbody));


        ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
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

        public MYViewPagerAdapter(ArrayList<View> views,ArrayList<String> titles){
            this.views=views;
            this.titles=titles;
        }

        @Override
        public CharSequence getPageTitle(int position){
            SpannableStringBuilder ssb = new SpannableStringBuilder(titles.get(position));
            ssb.setSpan(new RelativeSizeSpan(1.4f), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(242,242,242));
            ssb.setSpan(fcs, 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }
    }

}
