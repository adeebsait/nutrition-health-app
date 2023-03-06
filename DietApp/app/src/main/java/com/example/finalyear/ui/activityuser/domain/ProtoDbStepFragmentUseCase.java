package com.example.finalyear.ui.activityuser.domain;

import com.example.finalyear.Counter;
import com.example.finalyear.UserData;
import com.example.finalyear.common.data.DataStoreRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class ProtoDbStepFragmentUseCase {
    private final DataStoreRepository dataStoreRepository;

    @Inject
    public ProtoDbStepFragmentUseCase(DataStoreRepository dataStoreRepository) {
        this.dataStoreRepository = dataStoreRepository;
    }

    public @NonNull Observable<Integer> getCountOfSteps() {
        return dataStoreRepository.getCountOfSteps().toObservable();
    }


    public @NonNull Observable<Long> getStepCountDate() {
        return dataStoreRepository.getStepDate().toObservable();
    }

    public @NonNull Observable<Integer> getStepGoal() {
        return dataStoreRepository.getStepGoal().toObservable();
    }

    public @NonNull Observable<Long> getDuration() {
        return dataStoreRepository.getStepDuration().toObservable();
    }

    public @NonNull Observable<Integer> getCaloriesBurn() {
        return dataStoreRepository.getCaloriesBurn().toObservable();
    }

    public @NonNull Observable<Boolean> getIsStepAndGoalsEqual() {
        return dataStoreRepository.getStepAndGoalsEqual().toObservable();
    }

    public Single<UserData> resetCounter() {
        return dataStoreRepository.resetCounter();
    }

    public @NonNull Observable<Counter> getCounterDatas() {
        return dataStoreRepository.getCounterDatas().toObservable();
    }
}
