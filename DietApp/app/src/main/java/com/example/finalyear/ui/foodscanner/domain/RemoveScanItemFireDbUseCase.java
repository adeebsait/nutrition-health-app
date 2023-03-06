package com.example.finalyear.ui.foodscanner.domain;

import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;
import com.example.finalyear.ui.authentication.data.DietFireDbRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public class RemoveScanItemFireDbUseCase {
    private final DietFireDbRepository dietFireDbRepository;
    private final DietFireAuthRepository fireAuthRepository;

    @Inject
    public RemoveScanItemFireDbUseCase(DietFireDbRepository dietFireDbRepository, DietFireAuthRepository fireAuthRepository) {
        this.dietFireDbRepository = dietFireDbRepository;
        this.fireAuthRepository = fireAuthRepository;
    }
    public @NonNull Single<String> invoke(String key) {
        String uid = fireAuthRepository.getUid();
        return Single.create(emitter -> {
            dietFireDbRepository.removeScanFood(uid, key, (error, ref) -> {
                if (error != null) {
                    emitter.onError(error.toException());
                    return;
                }
                emitter.onSuccess(uid);
            });
        });
    }
}
