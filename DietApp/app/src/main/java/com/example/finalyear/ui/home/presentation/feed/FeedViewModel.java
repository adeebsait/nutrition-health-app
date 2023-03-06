package com.example.finalyear.ui.home.presentation.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietObserver;
import com.example.finalyear.ui.home.data.FeedItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class FeedViewModel extends ViewModel {
    @Inject
    public FeedViewModel() {
    }

    private final MutableLiveData<List<FeedItem>> feedItemState = new MutableLiveData<>();
    public LiveData<List<FeedItem>> feedItems = feedItemState;

    public void getFeedItems(String jsonString) {
        Observable.fromAction(() -> {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<FeedItem>>() {
                    }.getType();
                    List<FeedItem> list = gson.fromJson(jsonString, listType);
                    feedItemState.postValue(list);
                }).subscribeOn(Schedulers.io())
                .subscribe(new DietObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                    }
                });

    }
}
