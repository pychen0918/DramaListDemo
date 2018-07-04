package com.pychen0918.dramalistdemo.model.api;

import com.pychen0918.dramalistdemo.model.gson.DramaDataContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DramaWebApi {
    @GET("{path_id}")
    Call<DramaDataContainer> getDramaList(@Path("path_id") String pathId);
}
