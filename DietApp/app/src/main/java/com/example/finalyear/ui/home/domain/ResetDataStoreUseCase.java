package com.example.finalyear.ui.home.domain;

import com.example.finalyear.UserData;
import com.example.finalyear.common.data.DataStoreRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ResetDataStoreUseCase {
    private final DataStoreRepository dataStoreRepository;

    @Inject
    public ResetDataStoreUseCase(DataStoreRepository dataStoreRepository) {
        this.dataStoreRepository = dataStoreRepository;
    }

    public Single<UserData> invoke(){
        return dataStoreRepository.resetAll();
    }

}
