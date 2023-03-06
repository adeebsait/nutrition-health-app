package com.example.finalyear.common.utils;

import androidx.lifecycle.DefaultLifecycleObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;


public class TimerTicker {

    private Disposable disposable;
    private final TickListener tickListener;

    public TimerTicker(TickListener tickListener) {
        Timber.d("created");
        this.tickListener = tickListener;
    }

    public void start() {
        Timber.d("Started");
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(tick -> {
                    if (tickListener != null) {
                        tickListener.onTick(tick);
                    }
                });
    }

    public void stop() {
        Timber.d("Stopped");
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public interface TickListener {
        void onTick(long tick);
    }
}
