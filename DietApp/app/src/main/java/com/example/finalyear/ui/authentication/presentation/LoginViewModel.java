package com.example.finalyear.ui.authentication.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.common.utils.Validator;
import com.example.finalyear.ui.authentication.domain.LoginNavigateTo;
import com.example.finalyear.ui.authentication.domain.LoginWithEmailUseCase;
import com.example.finalyear.ui.authentication.domain.LoginWithGoogleUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class LoginViewModel extends ViewModel {


    private final LoginWithEmailUseCase loginWithEmailUseCase;
    private final LoginWithGoogleUseCase loginWithGoogleUseCase;

    @Inject
    public LoginViewModel(LoginWithEmailUseCase loginWithEmailUseCase,
                          LoginWithGoogleUseCase loginWithGoogleUseCase) {
        this.loginWithEmailUseCase = loginWithEmailUseCase;
        this.loginWithGoogleUseCase = loginWithGoogleUseCase;
    }

    private String email = "";
    private String password = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*-----------------------------------------------------------------*/
    private final MutableLiveData<Boolean> isLoginLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> getLoadingState = isLoginLoading;

    public final SingleLiveEvent<String> isErrorMessage = new SingleLiveEvent<>();


    private final SingleLiveEvent<LoginNavigateTo> isSuccessLogin = new SingleLiveEvent<>();
    public LiveData<LoginNavigateTo> getSuccessLoginState = isSuccessLogin;


    public void submitForLogin(String email, String password) {
        if (Validator.isBlankOrNull(email) || Validator.isBlankOrNull(password)) {
            isErrorMessage.setValue("Empty fields");
            return;
        }
        if (!Validator.isValidEmail(email)) {
            isErrorMessage.setValue("Enter a valid email address");
            return;
        }
        loginWithEmailUseCase.invoke(email, password)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<LoginNavigateTo>() {
                    @Override
                    public void onLoading(boolean isLoading) {
                        isLoginLoading.postValue(isLoading);
                    }

                    @Override
                    public void onComplete(LoginNavigateTo loginNavigateTo) {
                        isSuccessLogin.postValue(loginNavigateTo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isErrorMessage.postValue(e.getMessage());
                    }
                });
    }

    public void submitForLoginWithGoogle() {
        loginWithGoogleUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<LoginNavigateTo>() {
                    @Override
                    public void onLoading(boolean isLoading) {
                        isLoginLoading.postValue(isLoading);
                    }

                    @Override
                    public void onComplete(LoginNavigateTo loginNavigateTo) {
                        isSuccessLogin.postValue(loginNavigateTo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isErrorMessage.postValue(e.getMessage());
                    }
                });
    }


}