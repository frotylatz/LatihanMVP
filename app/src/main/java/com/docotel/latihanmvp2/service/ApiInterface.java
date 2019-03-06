package com.docotel.latihanmvp2.service;


import com.docotel.latihanmvp2.model.Photo;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface ApiInterface {
    @GET("/photos/")
    Observable<List<Photo>> getPhotoList();
}
