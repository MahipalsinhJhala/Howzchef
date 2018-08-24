package com.houz.chef.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.houz.chef.R;

/**
 * Created by Kiran Zinzuvadia on 5/4/16.
 */
public class TermsConditionActivity extends Activity implements View.OnClickListener {

    private TextView tvTitleLeft, tvTitle; //tvTermsDesc
    private ImageView ivBack;
    //LoadingDialog loadingDialog;
    private WebView webView;
    private ProgressBar progressBar;
    private final String URL = "http://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);
        init();
        setData();
        setListeners();
    }

    private void init() {
        //tvTermsDesc = (DLTextview) findViewById(R.id.tv_termsDescription);
        ivBack=findViewById(R.id.iv_back);
        tvTitle=findViewById(R.id.tv_title);
        tvTitleLeft=findViewById(R.id.tv_left_title);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //loadingDialog = new LoadingDialog(this);

    }



    private void setData() {
        //Set Terms data from Here
        tvTitle.setText(getString(R.string.term_and_condition));
        loadUrl();
      /*  String terms = AppPreference.getInstance(getBaseContext()).getTermsConditions().toString();
        if (terms.length() > 0) {
            setTerms(terms);
            callApi(false);
        } else {*/
        //callApi(true);
        /*}*/
    }

    private void loadUrl() {

        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }
    }

//    private void setTerms(String terms) {
//        tvTermsDesc.setText(terms);
//    }

    private void setListeners() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //User can't go back from term and condition screen. User has to agree
        //super.onBackPressed();
    }

   /* private void callApi(boolean showLoader) {
        if (showLoader)
            loadingDialog.show();

        String apiPath = isRegisterdAsProvider ? "12" : "11";

        Call<String> call = RESTClient.getInstance().getTerms(apiPath);
        if (!Functions.isNetworkConnectionAvailable(this)) {
            Functions.showToast(this, getResources().getString(R.string.no_internet));
            loadingDialog.dismiss();
            return;
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //TODO Check for Response parsing
                Functions.prnt("Body : " + response.body());
                Functions.prnt("Message : " + response.message());
                String error = "";
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        JSONObject dataObject = jsonObject.optJSONObject("responseData");
                        String terms;
                        if (dataObject != null) {
                            terms = dataObject.optString("Value", "");
                            if (terms.length() > 0) {
                                AppPreference.getInstance(getBaseContext()).saveTermsConditions(terms);
                                setTerms(terms);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        error = ResponseParser.parseMessage(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (error.length() > 0) Functions.showToast(TermsConditionActivity.this, error);

                if (loadingDialog.isShowing()) loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Functions.showToast(TermsConditionActivity.this, getResources().getString(R.string.connection_error));
                if (loadingDialog.isShowing()) loadingDialog.dismiss();
            }
        });
    }*/

}
