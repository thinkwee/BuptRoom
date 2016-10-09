package thinkwee.buptroom;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by think on 20162016/10/8 000821:13
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:主页界面
 */

public class HomePageFragment extends Fragment {
    private View v;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.homepage, container, false);
        return v;

    }
}
