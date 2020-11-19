package id.alfarizi.babymonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LihatBayi extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference();
    private WebView wew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_bayi);
        Toolbar mMyToolbar = (Toolbar) findViewById(R.id.tolbar);

        mMyToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }
                destroyWebView();
                LihatBayi.super.onBackPressed();
            }
        });

        wew = findViewById(R.id.webView);
        clearCookies();
        wew.getSettings().setJavaScriptEnabled(true);
        wew.getSettings().setLoadWithOverviewMode(true);
        wew.getSettings().setUseWideViewPort(true);
        wew.getSettings().setBuiltInZoomControls(true);
        wew.setInitialScale(1);
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);


        db.child("url_camera").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ip = dataSnapshot.getValue(String.class);
                Log.d("urlll", ip);
                final String alamat = "http://"+ip+"/";

                refreshLayout.setRefreshing(true);
                LoadUrl(alamat, wew, refreshLayout);

                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        LoadUrl(alamat, wew, refreshLayout);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void LoadUrl(final String alamat, WebView wew, final SwipeRefreshLayout refreshLayout) {
        clearCookies();
        wew.loadUrl(alamat);
        wew.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d("eror_url", failingUrl);
                Log.d("eror_desc", description);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);

                refreshLayout.setRefreshing(true);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Page load finished
                super.onPageFinished(view, url);
                Log.d("onfinish", url);
                refreshLayout.setRefreshing(false);
            }

        });

    }

    public void destroyWebView() {
        wew.clearHistory();

        // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
        // Probably not a great idea to pass true if you have other WebViews still alive.
        wew.clearCache(true);

        // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
        wew.loadUrl("about:blank");

        wew.onPause();
        wew.removeAllViews();
        wew.destroyDrawingCache();

        // NOTE: This can occasionally cause a segfault below API 17 (4.2)
        wew.destroy();

        // Null out the reference so that you don't end up re-using it.
        wew = null;
    }

    private void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyWebView();
    }
}
