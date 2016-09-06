package ru.startandroid.apartmentrentals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebFragment extends Fragment {

    public static final String ARG_WEB_URL = "arg_web_url";

    private String mWebUrl;
    private WebView mWebView;

    public static Fragment newInstance(String webUrl){
        Bundle args = new Bundle();
        args.putString(ARG_WEB_URL, webUrl);
        WebFragment webFragment = new WebFragment();
        webFragment.setArguments(args);
        return webFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebUrl = getArguments().getString(ARG_WEB_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_view_fragment, container, false);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        mWebView.loadUrl(mWebUrl);

        return view;
    }
}
