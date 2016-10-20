package cn.donica.slcd.utils;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import cn.donica.slcd.polling.MyApplication;


public class VolleyRequestUtil {
    protected static final String TAG = "VolleyRequest";
    private static RequestQueue requestQueue;
    public static JSONArray jsonArrayResponse;
    public static JsonArrayRequest jsonArrayRequest;

    public static JSONArray getJSONResponse(String url) {
        requestQueue = VolleySingleton.getVolleySingleton(MyApplication.getContext()).getRequestQueue();
        jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        jsonArrayResponse = response;
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e(TAG, error.getMessage(), error);
                LogUtil.w(TAG, "请求错误");
            }
        });
        VolleySingleton.getVolleySingleton(MyApplication.getContext()).addToRequestQueue(jsonArrayRequest);
        System.gc();
        return jsonArrayResponse;
    }
}
