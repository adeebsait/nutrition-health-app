package com.example.finalyear.ui.authentication.domain;

import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public class UserIsLoggedInUseCase {

    private final DietFireAuthRepository repository;

    @Inject
    public UserIsLoggedInUseCase(DietFireAuthRepository repository) {
        this.repository = repository;
    }


    public @NonNull Single<Boolean> invoke(){
        return Single.just(repository.getUserIsLoggedIn());
    }
}
