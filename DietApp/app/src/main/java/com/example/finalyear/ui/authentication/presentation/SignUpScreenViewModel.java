package com.example.finalyear.ui.authentication.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.common.utils.Validator;
import com.example.finalyear.ui.authentication.domain.SignupUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SignUpScreenViewModel extends ViewModel {


    public String email;
    public String password;

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

    private final SignupUseCase signupUseCase;

    @Inject
    public SignUpScreenViewModel(SignupUseCase signupUseCase) {
        this.signupUseCase = signupUseCase;
    }


    private final MutableLiveData<Boolean> isLoadingState = new MutableLiveData<>();
    public LiveData<Boolean> getLoadingState = isLoadingState;

    public SingleLiveEvent<String> errorEvent = new SingleLiveEvent<>();

    private final MutableLiveData<String> successEvent = new MutableLiveData<>();
    public LiveData<String> getSuccessEvent = successEvent;


    private void submitData(String email, String password) {
        signupUseCase.invoke(email, password)
            .subscribeOn(Schedulers.io())
            .subscribe(new DietSingleObserve<String>() {
                @Override
                public void onLoading(boolean aBollean) {
                    isLoadingState.postValue(aBollean);
                }

                @Override
                public void onComplete(String s) {
                    successEvent.postValue(s);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    errorEvent.postValue(e.getMessage());
                }

            });
    }

    public void submitSignup() {
        if (Validator.isBlankOrNull(email, password)) {
            errorEvent.setValue("Field Can't Be Empty");
            return;
        }
        if (!Validator.isValidEmail(email)) {
            errorEvent.setValue("Email is not valid");
            return;
        }
        submitData(email, password);
    }

}