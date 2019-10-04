package com.example.jsbridge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;

import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DWebView webView;
    private EditText editText;
    private Button refreshBtn;
    private Button inputBtn;
    private MainActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        editText = findViewById(R.id.editText);
        refreshBtn = findViewById(R.id.refreshBtn);
        inputBtn = findViewById(R.id.inputBtn);

        webView.loadUrl("http://192.168.1.9:8080?timestamp=" + new Date().getTime());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptObject(new JSApi(this), null);

        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.callHandler("getWebEditTextValue", null, new OnReturnValue<String>() {
                    public void onValue(String retValue) {
                        new AlertDialog.Builder(self).setMessage("Web 输入值: " + retValue).create().show();
                    }
                });
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("http://192.168.1.9:8080?timestamp=" + new Date().getTime());
            }
        });

    }

    class JSApi {
        private Context ctx;

        public JSApi(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void getNativeEditTextValue(Object msg, CompletionHandler<String> handler) {
            String value = ((MainActivity)ctx).editText.getText().toString();
            handler.complete(value);
        }
    }
}
