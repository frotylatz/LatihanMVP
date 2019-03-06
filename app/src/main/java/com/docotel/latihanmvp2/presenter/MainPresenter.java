package com.docotel.latihanmvp2.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.docotel.latihanmvp2.MainContract;
import com.docotel.latihanmvp2.base.BasePresenter;
import com.docotel.latihanmvp2.model.Photo;
import com.docotel.latihanmvp2.service.ApiClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter extends BasePresenter implements MainContract.Presenter {
    private final MainContract.View mView;

    private List<Photo> mList;
    private Context mContext;

    public MainPresenter(@NonNull MainContract.View view, Context context) {
        super(view);
        mView = view;
        mContext = context;
        mView.setPresenter(this);
    }

    public void requestPhotoFeed() {

        ApiClient apiClient = new ApiClient();

        Observable<List<Photo>> observable = apiClient.getAPI().getPhotoList().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<List<Photo>>() {
            @Override
            public void onCompleted() {
                mView.dismissProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                //mView.dismissProgressBar();
            }

            @Override
            public void onNext(final List<Photo> photos) {
                mList = new ArrayList<>();
                for (int i=0;i<5;i++)
                {
                    Photo photo = new Photo();
                    photo.setAlbumId(photos.get(i).getAlbumId());
                    photo.setId(photos.get(i).getId());
                    photo.setTitle(photos.get(i).getTitle());
                    photo.setUrl(photos.get(i).getUrl());
                    photo.setThumbnailUrl(photos.get(i).getThumbnailUrl());
                    mList.add(photo);
                }
                mView.showPhotoList(mList);
                mView.showMorePhotoList(photos);
            }
        });

    }
}
