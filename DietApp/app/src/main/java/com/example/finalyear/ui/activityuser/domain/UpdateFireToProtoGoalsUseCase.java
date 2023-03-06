package com.example.finalyear.ui.activityuser.domain;

import com.example.finalyear.UserData;
import com.example.finalyear.common.data.DataStoreRepository;
import com.example.finalyear.common.data.firebasepojo.UserActivityData;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public class UpdateFireToProtoGoalsUseCase {
    private final DataStoreRepository dataStoreRepository;

    @Inject
    public UpdateFireToProtoGoalsUseCase(DataStoreRepository dataStoreRepository) {
        this.dataStoreRepository = dataStoreRepository;
    }


    public Single<UserData> invoke(@NonNull UserActivityData userActivityData){
        return  dataStoreRepository.submitGoalsAndRemain(userActivityData);
    }
}
