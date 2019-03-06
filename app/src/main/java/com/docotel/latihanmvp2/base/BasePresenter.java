package com.docotel.latihanmvp2.base;

public abstract class BasePresenter {
    private BaseView mBaseView;

    public BasePresenter(BaseView baseView){
        mBaseView = baseView;
    }
}
