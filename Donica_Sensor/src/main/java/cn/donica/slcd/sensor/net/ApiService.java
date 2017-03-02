package cn.donica.slcd.sensor.net;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2016-12-30 10:02
 * Describe:
 */
public interface ApiService {
    /*   @GET("API/Market/GetBrand")
      Observable<String> getBrand(@Query("token") String token);*/
    @GET("API/Market/GetBrand")
    Call<JSONObject> getBrand(@Query("token") String token);
}
