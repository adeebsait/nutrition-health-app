package com.example.finalyear.ui.home.domain;

import com.example.finalyear.common.data.DataStoreRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

public class GetRemainCaloriesUseCase {
    private final DataStoreRepository dataStoreRepository;

    @Inject
    public GetRemainCaloriesUseCase(DataStoreRepository dataStoreRepository) {
        this.dataStoreRepository = dataStoreRepository;
    }
    
    public @NonNull Observable<Integer> invoke(){
        return dataStoreRepository.getRemainCalories()
                .toObservable();
    }
}
