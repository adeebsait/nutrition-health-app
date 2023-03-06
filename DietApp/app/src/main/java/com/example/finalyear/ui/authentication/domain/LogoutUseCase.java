package com.example.finalyear.ui.authentication.domain;

import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class LogoutUseCase {
    private DietFireAuthRepository dietFireAuthRepository;

    @Inject
    public LogoutUseCase(DietFireAuthRepository dietFireAuthRepository) {
        this.dietFireAuthRepository = dietFireAuthRepository;
    }

    public @NonNull Single<Boolean> invoke() {
        return Single.just(dietFireAuthRepository.logOut());
    }
}
