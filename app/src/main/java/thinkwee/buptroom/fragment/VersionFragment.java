package thinkwee.buptroom.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import thinkwee.buptroom.R;

/*
 * Created by think on 20162016/10/8 000818:35
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 */

public class VersionFragment extends Fragment {
    @Nullable
    private View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.version, container, false);
        TextView tv = (TextView) v.findViewById(R.id.show_content);
        tv.setText(R.string.version);
        TextView versiontv = (TextView) v.findViewById(R.id.history_version);
        versiontv.setText(R.string.version_history);
        return v;


    }
}
