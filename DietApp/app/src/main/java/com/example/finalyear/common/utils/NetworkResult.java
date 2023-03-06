package com.example.finalyear.common.utils;


public abstract class NetworkResult {
    public static class OnError extends NetworkResult {
        String Error;

        public OnError(String error) {
            Error = error;
        }
    };
    public static class OnLoading extends NetworkResult {};
    public static class OnSuccess<T> extends NetworkResult {

        T data;
        public OnSuccess(T data) {
            this.data = data;
        }
        public T getData(){
            return data;
        }
    };
}