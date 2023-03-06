package com.example.finalyear.ui.activityuser.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.Counter;
import com.example.finalyear.UserData;
import com.example.finalyear.common.utils.DietObserver;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.common.utils.Utility;
import com.example.finalyear.ui.activityuser.domain.ProtoDbStepFragmentUseCase;
import com.example.finalyear.ui.activityuser.domain.UpdateCaloriesUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SummaryViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final ProtoDbStepFragmentUseCase protoDbStepFragmentUseCase;

    private UpdateCaloriesUseCase updateCaloriesUseCase;

    @Inject
    public SummaryViewModel(ProtoDbStepFragmentUseCase protoDbStepFragmentUseCase,
                            UpdateCaloriesUseCase updateCaloriesUseCase) {
        this.updateCaloriesUseCase = updateCaloriesUseCase;
        this.protoDbStepFragmentUseCase = protoDbStepFragmentUseCase;
        getStepGoal();
        getCounterDatas();
    }

    private final MutableLiveData<Integer> countOfStepsState = new MutableLiveData<>(0);
    public LiveData<Integer> getCountOfSteps = countOfStepsState;

    private final MutableLiveData<String> distanceForTotalStepState = new MutableLiveData<>();
    public LiveData<String> distanceForTotalStep = distanceForTotalStepState;

    private void getCounterDatas() {
        protoDbStepFragmentUseCase.getCounterDatas()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietObserver<Counter>() {
                    @Override
                    public void onNext(Counter counter) {
                        // ----------- Count Of Step and Miles Calc
                        countOfStepsState.postValue(counter.getSteps());
                        float AVERAGE_DISTANCE_EACH_STEP = 0.7f;// 1 step = 0.7m
                        double _1METER_MILE = 0.00062;
                        distanceForTotalStepState.postValue(Utility.getMilesString((float) (_1METER_MILE *
                                AVERAGE_DISTANCE_EACH_STEP * counter.getSteps())));
                        // ----------- Counter Start Date
                        long date = counter.getDate();
                        if (counter.getDate() < 1672509600000L) {
                            date = System.currentTimeMillis();
                        }
                        stepDateState.postValue(date);
                        // ----------- Duration Calc
                        durationLiveData.postValue(counter.getDuration());
                        // ----------- Calories Burn
                        caloriesLossLiveData.postValue(counter.getCaloriesburn());

                    }
                });
    }

    private final MutableLiveData<Long> stepDateState =
            new MutableLiveData<>(System.currentTimeMillis());
    public LiveData<String> getStepsCountDate =
            Transformations.map(stepDateState, data -> Utility.millisToDate(data, "dd MMM,yyyy"));

    private final MutableLiveData<Integer> stepGoalState = new MutableLiveData<>(0);
    public LiveData<Integer> getStepGoal = stepGoalState;


    private void getStepGoal() {
        protoDbStepFragmentUseCase
                .getStepGoal()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        stepGoalState.postValue(integer);
                    }
                });
    }

    private final MutableLiveData<Integer> caloriesLossLiveData = new MutableLiveData<>();

    public LiveData<String> getCalorieLoss = Transformations.map(caloriesLossLiveData, String::valueOf);


    private final MutableLiveData<Long> durationLiveData = new MutableLiveData<>();
    public LiveData<String> duration = Transformations.map(durationLiveData, Utility::secondToHr);

    private final MutableLiveData<Boolean> resetLoadingState = new MutableLiveData<>();
    public LiveData<Boolean> getResetLoadingState = resetLoadingState;
    public SingleLiveEvent<String> getResetSuccessState = new SingleLiveEvent<>();

    public void resetCounter(String caloriesBurn) {
        updateCaloriesUseCase.invoke(Long.valueOf(caloriesBurn))
                .flatMap(caloriesResult -> protoDbStepFragmentUseCase
                        .resetCounter())
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<UserData>() {
                    @Override
                    public void onLoading(boolean aBoolean) {
                        resetLoadingState.postValue(aBoolean);
                    }
                    @Override
                    public void onComplete(UserData userData) {
                        getResetSuccessState.postValue("Reset Successfully");
                    }
                });

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
