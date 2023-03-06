package com.example.finalyear.ui.home.presentation.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.UserData;
import com.example.finalyear.common.data.firebasepojo.UserActivityData;
import com.example.finalyear.ui.home.domain.GetRemainCaloriesUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class Slider1ViewModel extends ViewModel {
    private final GetRemainCaloriesUseCase getRemainCaloriesUseCase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public Slider1ViewModel(
            GetRemainCaloriesUseCase getRemainCaloriesUseCase) {
        this.getRemainCaloriesUseCase = getRemainCaloriesUseCase;
        getRemainCal();
    }



    private final MutableLiveData<String> remainProto = new MutableLiveData<>();
    public LiveData<String> getRemainProto=  remainProto;

    public void getRemainCal(){
        getRemainCaloriesUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Integer remain) {
                        remainProto.postValue(String.valueOf(remain));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}