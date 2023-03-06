package com.example.finalyear.common.utils;

public interface CommonObserver<T> {
    default void onLoading(boolean aBollean){}
    void onComplete(T t);
}
