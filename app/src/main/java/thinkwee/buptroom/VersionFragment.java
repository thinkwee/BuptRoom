package thinkwee.buptroom;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by think on 20162016/10/8 000818:35
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:版本信息
 */

public class VersionFragment extends Fragment {
    @Nullable
    private View v;
    private TextView tv;
    private WebView webViewgit;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.version, container, false);
        tv=(TextView)v.findViewById(R.id.show_content);
        webViewgit=(WebView)v.findViewById(R.id.webgit);
        webViewgit.setVisibility(View.INVISIBLE);
        tv.setText(R.string.version );
        webViewgit = (WebView)v.findViewById(R.id.webgit);
        webViewgit.getSettings().setJavaScriptEnabled(true);
        webViewgit.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                view.loadUrl(url);
                return true;
            }
        });
        webViewgit.loadUrl("https://github.com/thinkwee/BuptRoom/wiki/INTRODUCTION");
        webViewgit.setVisibility(View.VISIBLE);
        return v;
    }
}
