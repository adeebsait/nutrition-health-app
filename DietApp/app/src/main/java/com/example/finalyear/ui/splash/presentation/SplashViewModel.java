package com.example.finalyear.ui.splash.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.authentication.domain.UserIsLoggedInUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SplashViewModel extends ViewModel {
    private final UserIsLoggedInUseCase isLoggedInUseCase;

    @Inject
    public SplashViewModel(UserIsLoggedInUseCase isLoggedInUseCase) {
        this.isLoggedInUseCase = isLoggedInUseCase;
    }

    private final SingleLiveEvent<Boolean> isUserLoggedMutLive = new SingleLiveEvent<>();

    public LiveData<Boolean> getUserIsLoggedIn() {
        isLoggedInUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<Boolean>() {
                    @Override
                    public void onComplete(Boolean aBoolean) {
                        isUserLoggedMutLive.postValue(aBoolean);
                    }
                });
        return isUserLoggedMutLive;
    }

}
