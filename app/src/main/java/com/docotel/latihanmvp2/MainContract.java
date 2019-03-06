package com.docotel.latihanmvp2;

import android.support.annotation.StringRes;

import com.docotel.latihanmvp2.base.BaseView;
import com.docotel.latihanmvp2.model.Photo;

import java.util.List;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void showSnackbar(@StringRes int Message);

        void dismissProgressBar();

        void showPhotoList(List<Photo> photos);

        void showMorePhotoList(List<Photo> photos);

    }

    interface Presenter{
        void requestPhotoFeed();

    }
}
