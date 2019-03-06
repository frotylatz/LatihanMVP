package com.docotel.latihanmvp2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.docotel.latihanmvp2.databinding.*;

import com.docotel.latihanmvp2.model.Photo;
import com.docotel.latihanmvp2.presenter.MainPresenter;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    private ActivityMainBinding mBinding;
    private MainContract.Presenter mPresenter;
    private PhotoAdapter photoAdapter;
    private List<Photo> mList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    NotificationBadge mBadge;
    Button btnIncrease, btnMany, btnClear;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        new MainPresenter(this, this);
        mPresenter.requestPhotoFeed();

        mBadge = findViewById(R.id.badge);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBadge.setNumber(++count);
            }
        });

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBadge.setNumber(0);
                count = 0;
            }
        });


    }

    @Override
    public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showSnackbar(@StringRes int message) {
        Snackbar.make(mBinding.cl, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void dismissProgressBar() {
        mBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPhotoList(final List<Photo> photos) {
        photoAdapter = new PhotoAdapter(mBinding.recyclerView,photos,MainActivity.this, linearLayoutManager);
        mBinding.recyclerView.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
        mList = photos;
    }

    @Override
    public void showMorePhotoList(final List<Photo> photos) {
        photoAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mList.size() <= photos.size() - 5) {
                    mList.add(null);
                    photoAdapter.notifyItemInserted(mList.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mList.remove(mList.size() - 1);
                            photoAdapter.notifyItemRemoved(mList.size());

                            //Generating more data
                            int index = mList.size();
                            int end = index + 5;
                            for (int i = index; i < end; i++) {
                                Log.d("Tes ", "Foto muncul");
                                Photo photo = new Photo();
                                photo.setAlbumId(photos.get(i).getAlbumId());
                                photo.setId(photos.get(i).getId());
                                photo.setTitle(photos.get(i).getTitle());
                                photo.setUrl(photos.get(i).getUrl());
                                photo.setThumbnailUrl(photos.get(i).getThumbnailUrl());
                                mList.add(photo);
                            }
                            photoAdapter.notifyDataSetChanged();
                            photoAdapter.setLoaded();
                        }
                    }, 5000);
                } else {
                    showSnackbar(R.string.completed);
                }
            }
        });
        photoAdapter.notifyDataSetChanged();
    }
}
