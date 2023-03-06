package com.example.finalyear.ui.authentication.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.data.firebasepojo.Height;
import com.example.finalyear.common.data.firebasepojo.Info;
import com.example.finalyear.common.data.firebasepojo.Weight;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.common.utils.Validator;
import com.example.finalyear.ui.authentication.domain.SignUpInfoSubmitUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class InfoFormViewModel extends ViewModel {
    public String firstName = "";
    public String lastName = "";
    public String dob = "";
    public String gender = "";
    public Integer heightFt = 0;
    public Integer heightIn = 0;
    public Integer weightKilo = 0;
    public Integer weightGm = 0;

    private final SignUpInfoSubmitUseCase signUpInfoSubmitUseCase;

    @Inject
    public InfoFormViewModel(SignUpInfoSubmitUseCase signUpInfoSubmitUseCase) {
        this.signUpInfoSubmitUseCase = signUpInfoSubmitUseCase;
    }
    private final MutableLiveData<Boolean> isLoadingState = new MutableLiveData<>(false);
    public final LiveData<Boolean> getLoadingState = isLoadingState;
    public final SingleLiveEvent<String> errorState = new SingleLiveEvent<>();

    private final MutableLiveData<String> signupState = new MutableLiveData<>();
    public final LiveData<String> getSignupState = signupState;


    private void submitData(Info info) {
        signUpInfoSubmitUseCase.invoke(info)
            .subscribeOn(Schedulers.io())
            .subscribe(new DietSingleObserve<String>() {
                @Override
                public void onLoading(boolean aBollean) {
                    isLoadingState.postValue(aBollean);
                }

                @Override
                public void onComplete(String s) {
                    signupState.postValue(s);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    errorState.postValue(e.getMessage());
                }
            });
    }




    public void signUp() {
        if (Validator.isBlankOrNull(firstName, lastName, gender, dob, heightFt, heightIn, weightKilo, weightGm)) {
            errorState.setValue("Field Cannot Be Empty");
            return;
        };
        Height height = new Height(heightFt, heightIn);
        Weight weight = new Weight(weightKilo, weightGm);
        Info info = new Info(firstName, lastName, gender, dob, weight, height);
        submitData(info);
    }


}