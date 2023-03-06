package com.example.finalyear.db.proto;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.core.CorruptionException;
import androidx.datastore.core.Serializer;

import com.example.finalyear.UserData;
import com.firebase.ui.auth.data.model.User;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import kotlin.Unit;
import kotlin.coroutines.Continuation;


public class  UserDataSerializer implements Serializer<UserData> {

    @Nullable
    @Override
    public Object readFrom(@NonNull InputStream inputStream, @NonNull Continuation<? super UserData> continuation) {
        try {
            return UserData.parseFrom(inputStream);
        } catch (InvalidProtocolBufferException exception) {
            try {
                throw new CorruptionException("Cannot read proto.", exception);
            } catch (CorruptionException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @Override
    public Object writeTo(UserData userData, @NonNull OutputStream outputStream, @NonNull Continuation<? super Unit> continuation) {
        try {
            userData.writeTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public UserData getDefaultValue() {
        return UserData.getDefaultInstance();
    }
}
