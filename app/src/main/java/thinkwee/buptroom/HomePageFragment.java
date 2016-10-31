package thinkwee.buptroom;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by think on 20162016/10/8 000821:13
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:主页界面
 */

public class HomePageFragment extends Fragment {
    private View v;
    private RecyclerView recyclerview;
    private MyRecyclerAdapter mAdapter;
    private  List<Map<String,Object>> mDatas;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.homepage, container, false);
        mDatas=getData();
        recyclerview = (RecyclerView)v.findViewById(R.id.recycler_view );

        mAdapter= new MyRecyclerAdapter(super.getActivity(),mDatas );
        LinearLayoutManager layoutManager = new LinearLayoutManager(super.getActivity());
        recyclerview.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerview.setAdapter(mAdapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerview,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mDatas.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mDatas.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        recyclerview.addOnItemTouchListener(swipeTouchListener);
        return v;

    }


    private List<Map<String,Object>> getData(){
        /**
         * Created by Thinkwee on 2016/10/12 0012 10:01
         * Parameter []
         * Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
         * CLASS:SettingActivity
         * FILE:SettingActivity.java
         * TODO:绑定每一栏的各个组件
         */

        List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
        Map<String,Object> map= new HashMap<String, Object>();
        map.put("img",R.drawable.hp1);
        map.put("text","1\n点开左侧菜单，选择空闲教室即可查看教室");
        list.add(map);

        map= new HashMap<String, Object>();
        map.put("img",R.drawable.hp2);
        map.put("text","2\n摇一摇可以摇出此时此刻适合您的空闲教室");
        list.add(map);

        map= new HashMap<String, Object>();
        map.put("img",R.drawable.hp3);
        map.put("text","3\n在主题设置中可以自己选取菜单的颜色\n然后系统自动适配一张适合您所选颜色的背景图片");
        list.add(map);

        map= new HashMap<String, Object>();
        map.put("img",R.drawable.hp4);
        map.put("text","4\n当然如果你知道这些\n就把这几张卡片划掉，直接去学习吧\n去吧学霸~");
        list.add(map);
        return list;
    }


}
