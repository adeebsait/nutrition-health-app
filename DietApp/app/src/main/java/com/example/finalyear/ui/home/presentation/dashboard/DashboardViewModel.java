package com.example.finalyear.ui.home.presentation.dashboard;

import androidx.lifecycle.ViewModel;

import com.example.finalyear.UserData;
import com.example.finalyear.common.data.firebasepojo.UserActivityData;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.ui.activityuser.domain.RetrieveUserActivitiesFireUseCase;
import com.example.finalyear.ui.activityuser.domain.UpdateFireToProtoGoalsUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class DashboardViewModel extends ViewModel {


    private final UpdateFireToProtoGoalsUseCase updateFireToProtoGoalsUseCase;

    private final RetrieveUserActivitiesFireUseCase retrieveUserActivitiesFireUseCase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public DashboardViewModel(
            RetrieveUserActivitiesFireUseCase retrieveUserActivitiesFireUseCase
            , UpdateFireToProtoGoalsUseCase updateFireToProtoGoalsUseCase) {
        this.retrieveUserActivitiesFireUseCase = retrieveUserActivitiesFireUseCase;
        this.updateFireToProtoGoalsUseCase = updateFireToProtoGoalsUseCase;
        getUserActivities();

    }

    public void getUserActivities() {
        retrieveUserActivitiesFireUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserActivityData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull UserActivityData userActivityData) {
                        updateGoalAndRemain(userActivityData);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateGoalAndRemain(@NonNull UserActivityData userActivityData) {
        updateFireToProtoGoalsUseCase
                .invoke(userActivityData)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<UserData>() {
                    @Override
                    public void onComplete(UserData userData) {
                        Timber.d("Activities FireDb To Proto Success");
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}