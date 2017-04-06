package thinkwee.buptroom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by thinkwee on 2017/4/6.
 * TODO:取消webview，改用爬虫
 */

public class Webget {
    private WebView webView;
    private int HaveNetFlag = 0;
    private String htmlbody = null;
    private int WrongNet = -1;

    public void init(WebView wb) {
        webView = wb;
    }

    public int getWrongnet() {
        return WrongNet;
    }

    public String getHtmlbody() {
        return htmlbody;
    }

    public int getHaveNetFlag() {
        return HaveNetFlag;
    }

    final class JavascriptHandler {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:27
         * Parameter
         * Return
         * CLASS:JavascriptHandler
         * FILE:MainActivity.java
         */

        @JavascriptInterface
        public void getContent(String htmlContent) {
            org.jsoup.nodes.Document document = Jsoup.parse(htmlContent);
            htmlbody = document.getElementsByTag("body").text();
            HaveNetFlag = 1;
            if (htmlbody.contains("楼")) {
                WrongNet = 0;
            } else
                WrongNet = 1;
            Log.i("welcome","a"+WrongNet);
            Log.i("welcome","html: "+htmlbody);
        }
    }


    public void WebInit() {
        /**
         * Created by Thinkwee on 2016/10/8 0008 21:23
         * Parameter []
         * Return void
         * CLASS:MainActivity
         * FILE:MainActivity.java
         */

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavascriptHandler(), "handler");
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://jwxt.bupt.edu.cn/zxqDtKxJas.jsp");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (HaveNetFlag == 0) ; //Notification_show("无网络或非校园网,请重启或者离线");
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    private final class MyWebViewClient extends WebViewClient {
        /**
         * Created by Thinkwee on 2016/9/28 0028 9:27
         * Parameter
         * Return
         * CLASS:MyWebViewClient
         * FILE:MainActivity.java
         */

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i("WebView", "onPageStarted");
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.handler.getContent(document.body.innerHTML);");
            Log.i("WebView", "onPageFinished ");
            super.onPageFinished(view, url);
        }
    }


}
