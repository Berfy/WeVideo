package we.video.wevideo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import we.video.wevideo.util.LogUtil;

/**
 * Created by Berfy on 2016/4/20.
 * 广告链接
 */
public class AdWebActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.activity_web);
    }

    @Override
    protected void initView() {
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setScrollbarFadingEnabled(false);

        setTopTitle(getIntent().getStringExtra("title"));
        webView.loadUrl(getIntent().getStringExtra("url"));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("跳转", url);
                webView.loadUrl(url);
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTopTitle(title);
            }
        });
    }

    @Override
    protected void doClickEvent(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
        return false;
    }
}
