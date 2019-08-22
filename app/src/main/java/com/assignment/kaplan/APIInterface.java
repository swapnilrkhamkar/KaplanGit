package com.assignment.kaplan;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("weather")
    Single<ForecastModel> getResponse(@Query("lat") double lat, @Query("lon") double lon, @Query("APPID") String appid);

}



