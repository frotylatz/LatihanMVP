package com.docotel.latihanmvp2.base;

import android.support.annotation.NonNull;

public interface BaseView<T> {
    void setPresenter(@NonNull T presenter);
}
