package com.example.finalyear.ui.recipegenerator.presentation.takerecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietObserver;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.recipegenerator.TakenRecipeEntity;
import com.example.finalyear.ui.recipegenerator.domain.RetrieveTakenListUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RecipeTakeViewModel extends ViewModel {

    private RetrieveTakenListUseCase retrieveTakenListUseCase;

    @Inject
    public RecipeTakeViewModel(RetrieveTakenListUseCase retrieveTakenListUseCase) {
        this.retrieveTakenListUseCase = retrieveTakenListUseCase;
        getTakenRecipeList();
    }


    public SingleLiveEvent<String> takenListError = new SingleLiveEvent<>();

    private MutableLiveData<List<TakenRecipeEntity>> takenListRetrieveSuccess = new MutableLiveData<>();
    public LiveData<List<TakenRecipeEntity>> getTakenListRetrieveSuccess = takenListRetrieveSuccess;

    private void getTakenRecipeList() {
        retrieveTakenListUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietObserver<List<TakenRecipeEntity>>() {
                    @Override
                    public void onNext(List<TakenRecipeEntity> takenRecipeEntities) {
                        takenListRetrieveSuccess.postValue(takenRecipeEntities);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        takenListError.postValue(e.getMessage());
                    }
                });
    }

}
