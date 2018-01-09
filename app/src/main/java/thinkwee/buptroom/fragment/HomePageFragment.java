package thinkwee.buptroom.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thinkwee.buptroom.utils.MyRecyclerAdapter;
import thinkwee.buptroom.R;
import thinkwee.buptroom.utils.SwipeableRecyclerViewTouchListener;

/**
 * Created by think on 20162016/10/8 000821:13
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 */

public class HomePageFragment extends Fragment {
    private MyRecyclerAdapter mAdapter;
    private List<Map<String, Object>> mDatas;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.homepage, container, false);
        mDatas = getData();
        RecyclerView recyclerview = (RecyclerView) v.findViewById(R.id.recycler_view);

        mAdapter = new MyRecyclerAdapter(super.getActivity(), mDatas);
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


    private List<Map<String, Object>> getData() {
        /*
         * Created by Thinkwee on 2016/10/12 0012 10:01
         * Parameter []
         * Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
         * CLASS:SettingActivity
         * FILE:SettingActivity.java
         */

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("img", R.drawable.hp1);
        map.put("text", "1\n点开左侧菜单，选择空闲教室即可查看教室");
        list.add(map);

        map = new HashMap<>();
        map.put("img", R.drawable.hp2);
        map.put("text", "2\n摇一摇可以摇出此时此刻适合您的空闲教室");
        list.add(map);

        map = new HashMap<>();
        map.put("img", R.drawable.hp3);
        map.put("text", "3\n在主题设置可以自定义菜单栏颜色");
        list.add(map);

        return list;
    }


}
