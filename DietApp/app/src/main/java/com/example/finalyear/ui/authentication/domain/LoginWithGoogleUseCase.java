package com.example.finalyear.ui.authentication.domain;

import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public class LoginWithGoogleUseCase {

    private LoginHelpers loginHelpers;

    private final DietFireAuthRepository fireAuthRepository;

    @Inject
    public LoginWithGoogleUseCase(LoginHelpers loginHelpers, DietFireAuthRepository fireAuthRepository) {
        this.loginHelpers = loginHelpers;
        this.fireAuthRepository = fireAuthRepository;

    }

    public Single<String> loginWithGoogle(String uid) {
        return Single.just(uid);
    }
    public @NonNull Single<LoginNavigateTo> invoke() {
        return Single.create(emitter -> {
            loginHelpers.loginSteps(loginWithGoogle(fireAuthRepository.getUid()),
                    emitter,fireAuthRepository.getUserEmail(),
                    "");
        });
    }

}
