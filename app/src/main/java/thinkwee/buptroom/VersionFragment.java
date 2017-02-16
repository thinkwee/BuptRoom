package thinkwee.buptroom;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jsoup.Jsoup;

/**
 * Created by think on 20162016/10/8 000818:35
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:版本信息
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
