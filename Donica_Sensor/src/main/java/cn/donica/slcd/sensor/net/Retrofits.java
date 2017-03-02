package cn.donica.slcd.sensor.net;

import android.content.Context;

import org.json.JSONObject;

import cn.donica.slcd.sensor.net.utils.UrlUtils;
import cn.donica.slcd.sensor.utils.DLog;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-12-30 10:28
 * Describe:
 */
public class Retrofits {
    public static void getBrand(Context context) {
        /// String host="http://market.kyd2002.cn/API/Market/GetBrand?token=\"000000000000000-2d3acccfc8b5dbf14e9c260485b5f49a\"";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlUtils.HOST).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
        Call<JSONObject> call = service.getBrand(UrlUtils.getAppKey(context));

        DLog.info(UrlUtils.getAppKey(context));
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Response<JSONObject> response, Retrofit retrofit) {
                try {

                    DLog.info("String:" + response.body().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    DLog.info("JSONException:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                DLog.info("onFailure:" + throwable.getMessage());
            }
        });

    }
}
