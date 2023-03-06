package com.example.finalyear.ui.authentication.domain;

import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;
import com.example.finalyear.ui.authentication.data.DietFireDbRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class SignupUseCase {
    private final DietFireAuthRepository dietFireAuthRepository;
    private final LoginHelpers loginHelpers;

    @Inject
    public SignupUseCase(DietFireAuthRepository dietFireAuthRepository,LoginHelpers loginHelpers) {
        this.dietFireAuthRepository = dietFireAuthRepository;
        this.loginHelpers = loginHelpers;
    }


    public Single<String> invoke(String email, String password) {
        return Single.create(emitter -> {
            dietFireAuthRepository.signUpWithEmail(email, password, task -> {
                if (!task.isSuccessful()) {
                    emitter.onError(task.getException());
                    return;
                }
                emitter.onSuccess(task.getResult().getUser().getUid());
            });
        })
        .flatMap(uid -> loginHelpers.createIfNotEmail((String) uid, email, password));
    }

}
