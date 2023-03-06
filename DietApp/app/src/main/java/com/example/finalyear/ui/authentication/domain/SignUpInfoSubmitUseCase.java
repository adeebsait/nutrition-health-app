package com.example.finalyear.ui.authentication.domain;

import com.example.finalyear.common.data.firebasepojo.Info;
import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;
import com.example.finalyear.ui.authentication.data.DietFireDbRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class SignUpInfoSubmitUseCase {
    private final DietFireAuthRepository dietFireAuthRepository;
    private final DietFireDbRepository dietFireDbRepository;

    @Inject
    public SignUpInfoSubmitUseCase(DietFireAuthRepository dietFireAuthRepository,
                                   DietFireDbRepository dietFireDbRepository) {
        this.dietFireAuthRepository = dietFireAuthRepository;
        this.dietFireDbRepository = dietFireDbRepository;
    }

    public Single<String> invoke(Info info) {
        return Single.create(emitter -> {
            String uid = dietFireAuthRepository.getUid();
            dietFireDbRepository.addInfo(uid, info, (error, ref) -> {
                if (error != null) {
                    emitter.onError(error.toException());
                    return;
                }
                emitter.onSuccess(uid);
            });
        });
    }
}
