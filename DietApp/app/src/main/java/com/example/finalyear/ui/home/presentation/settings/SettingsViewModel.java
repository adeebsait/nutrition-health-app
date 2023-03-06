package com.example.finalyear.ui.home.presentation.settings;

import androidx.lifecycle.ViewModel;

import com.example.finalyear.UserData;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.authentication.domain.LogoutUseCase;
import com.example.finalyear.ui.home.domain.ResetDataStoreUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final LogoutUseCase logoutUseCase;

    private final ResetDataStoreUseCase resetDataStoreUseCase;

    @Inject
    public SettingsViewModel(LogoutUseCase logoutUseCase,ResetDataStoreUseCase
                             resetDataStoreUseCase) {
        this.logoutUseCase = logoutUseCase;
        this.resetDataStoreUseCase = resetDataStoreUseCase;
    }

    public final SingleLiveEvent<Boolean> getLogoutSuccessState = new SingleLiveEvent<>();
    public final SingleLiveEvent<String> getErrorState = new SingleLiveEvent<>();
    public final SingleLiveEvent<Boolean> getLogoutLoadingState = new SingleLiveEvent<>();



    public void submitLogout() {
        logoutUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .flatMap(aBoolean -> resetDataStoreUseCase.invoke())
                .subscribe(new DietSingleObserve<UserData>() {
                    @Override
                    public void onLoading(boolean aBoolean) {
                        getLogoutLoadingState.postValue(aBoolean);
                    }

                    @Override
                    public void onComplete(UserData userData) {
                        getLogoutSuccessState.postValue(true);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
