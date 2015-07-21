package com.androidapp.osijeknightlife.app;

import retrofit.ResponseCallback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by Toni P on 4/26/2015.
 */
public interface DropBox {
    public static final String BASE_URL = "https://www.dropbox.com";
    public static final String Token = "TORk4MtPVCMAAAAAAAANCF3qHuGZjLsED0bouYaoAE1xnJn7hckFadcJLVwT-WRI";

    @Headers({
            "Authorization: Bearer "+Token
    })
    @GET("/1/files/auto/{path}")
    public void Download(@Path("path") String path, ResponseCallback response);
}
