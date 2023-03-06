package com.example.finalyear.common.utils;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public abstract class DietSingleObserve<T> implements SingleObserver<T>, CommonObserver<T> {

    private Disposable ds;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        ds = d;
        onLoading(true);
    }

    @Override
    public void onSuccess(@NonNull T t) {
        onComplete(t);
        onLoading(false);
        if (!ds.isDisposed())
            ds.dispose();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onLoading(false);
        Timber.e(e);
    }


    @Override
    public void onLoading(boolean aBoolean){};

    @Override
    public abstract void onComplete(T t);
}
