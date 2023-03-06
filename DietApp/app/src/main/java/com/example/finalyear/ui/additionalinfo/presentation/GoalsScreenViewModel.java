package com.example.finalyear.ui.additionalinfo.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.additionalinfo.domain.GoalDataSubmitUserCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class GoalsScreenViewModel extends ViewModel {
    private final GoalDataSubmitUserCase goalDataSubmitUserCase;
    @Inject
    public GoalsScreenViewModel(GoalDataSubmitUserCase goalDataSubmitUserCase) {
        this.goalDataSubmitUserCase = goalDataSubmitUserCase;
    }

    private String goalText = "";
    private String baselineText = "";

    public String getGoalText() {
        return goalText;
    }

    public void setGoalText(String goalText) {
        this.goalText = goalText;
    }

    public String getBaselineText() {
        return baselineText;
    }

    public void setBaselineText(String baselineText) {
        this.baselineText = baselineText;
    }

    private final MutableLiveData<Boolean> isLoadingState = new MutableLiveData<>();
    public LiveData<Boolean> getLoadingState = isLoadingState;


    private final MutableLiveData<String> iSuccessState = new MutableLiveData<>();
    public LiveData<String> getSuccessState = iSuccessState;


    public final SingleLiveEvent<String> errorState = new SingleLiveEvent<>();



    public void addGoalData(String goal,String baseline){
        goalDataSubmitUserCase.invoke(goal,baseline)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<String>() {
                    @Override
                    public void onLoading(boolean aBollean) {
                        isLoadingState.postValue(aBollean);
                    }

                    @Override
                    public void onComplete(String s) {
                        iSuccessState.postValue(s);
                    }
                });
    }
}