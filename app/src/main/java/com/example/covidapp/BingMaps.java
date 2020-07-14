package com.example.covidapp;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BingMaps extends AppCompatActivity {
    WebView mwebView;
    private String url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/media-resources/news";
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_maps);

        mwebView = (WebView) findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.progressText);



        final WebSettings webSettings = mwebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mwebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mwebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mwebView.getSettings().setAppCacheEnabled(true);
        mwebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);

        //load url into the app
        mwebView.loadUrl(url);

        mwebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        mwebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);

                if(newProgress == 100){
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    mwebView.setVisibility(View.VISIBLE);

                }

                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mwebView.canGoBack())
        {
            mwebView.goBack();
        }
        else
        super.onBackPressed();
    }


}