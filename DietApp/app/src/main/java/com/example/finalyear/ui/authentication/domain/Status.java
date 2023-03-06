package com.example.finalyear.ui.authentication.domain;


public class Status<T> {
    static class SUCCESS<T> extends Status<T> {
        public T t;

        public SUCCESS(T t) {
            this.t = t;
        }
    }

    static class UNSUCCESS<T> extends Status<T> {
        public T t;

        public UNSUCCESS(T t) {
            this.t = t;
        }
    }
}