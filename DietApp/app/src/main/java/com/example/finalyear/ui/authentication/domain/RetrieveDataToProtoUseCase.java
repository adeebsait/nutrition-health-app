package com.example.finalyear.ui.authentication.domain;

import com.example.finalyear.UserData;
import com.example.finalyear.common.data.DataStoreRepository;
import com.example.finalyear.common.data.firebasepojo.UserInfo;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

/**
 * Never Call From DashBoard Screen
 * Only call when sign up or login succeed*/
public class RetrieveDataToProtoUseCase {
    private final DataStoreRepository dataStoreRepository;

    @Inject
    public RetrieveDataToProtoUseCase(DataStoreRepository dataStoreRepository) {
        this.dataStoreRepository = dataStoreRepository;
    }

    public Single<UserData> invoke(UserInfo userInfo) {
        return dataStoreRepository.insertData(userInfo);
    }
}
