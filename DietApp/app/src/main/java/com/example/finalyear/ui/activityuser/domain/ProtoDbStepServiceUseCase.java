package com.example.finalyear.ui.activityuser.domain;

import com.example.finalyear.UserData;
import com.example.finalyear.common.data.DataStoreRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class ProtoDbStepServiceUseCase {
    private final DataStoreRepository dataStoreRepository;

    @Inject
    public ProtoDbStepServiceUseCase(DataStoreRepository dataStoreRepository) {
        this.dataStoreRepository = dataStoreRepository;
    }

    public Single<UserData> incrementSteps(){
        return dataStoreRepository.incrementStep();
    }


    public Single<UserData> insertStepDate(long currentTimeMillis) {
        return dataStoreRepository.insertStepDate(currentTimeMillis);
    }

    public Single<UserData> addStepDuration() {
        return dataStoreRepository.addDuration();
    }

    public Observable<Integer> getIncrementSteps() {
        return dataStoreRepository.getCountOfSteps().toObservable();
    }
}
