package com.example.finalyear.ui.authentication.domain;

import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public class LoginWithEmailUseCase {

    private final LoginHelpers loginHelpers;
    private final DietFireAuthRepository fireAuthRepository;

    @Inject
    public LoginWithEmailUseCase(LoginHelpers loginHelpers, DietFireAuthRepository fireAuthRepository) {
        this.loginHelpers = loginHelpers;
        this.fireAuthRepository = fireAuthRepository;

    }


    public Single<String> loginWithEmail(String email, String password) {
        return Single.create(emitter ->
                fireAuthRepository.loginWithEmail(email, password, task -> {
                    if (!task.isSuccessful()) {
                        emitter.onError(task.getException());
                        return;
                    }
                    emitter.onSuccess(task.getResult().getUser().getUid());
                }));
    }


    public @NonNull Single<LoginNavigateTo> invoke(String email, String password) {
        return Single.create(emitter -> {
            loginHelpers.loginSteps(loginWithEmail(email, password), emitter, email, password);
        });
    }


}
