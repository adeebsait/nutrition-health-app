package com.example.finalyear.ui.recipegenerator.presentation.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietObserver;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.recipegenerator.data.RecipeRepository;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeDetails;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RecipeDetailViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private final RecipeDetails recipeDetails;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public RecipeDetailViewModel(SavedStateHandle savedStateHandle,
                                 RecipeRepository recipeRepository) {
        this.recipeDetails = savedStateHandle.get("details");
        this.recipeRepository = recipeRepository;
        assert recipeDetails != null;
        checkItemSavedOrNot(recipeDetails.getId()); // if checked then save button active
    }

    public RecipeDetails getRecipeDetails() {
        return recipeDetails;
    }


    // ------------------------------------- Check Item Saved In Room or Not
    public final SingleLiveEvent<Boolean> itemExistEvent = new SingleLiveEvent<>();

    private void checkItemSavedOrNot(String id) {
        recipeRepository.getRecipeExistInRoom(id)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        itemExistEvent.postValue(aBoolean);
                        super.onNext(aBoolean);//call onComplete
                    }
                });
    }


    // ------------------------------------- Save & Remove Recipes To/From Room
    public final SingleLiveEvent<String> savingErrorState = new SingleLiveEvent<String>();
    private final MutableLiveData<Boolean> saveLoadingState = new MutableLiveData<>(false);
    public LiveData<Boolean> getSaveLoadingState = saveLoadingState;

    public SingleLiveEvent<String> saveOrRemoveSuccessEvent = new SingleLiveEvent<>();

    public void saveRecipeToRoom(RecipeDetails recipeDetails) {
        recipeRepository.saveRecipe(recipeDetails)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        saveOrRemoveSuccessEvent.postValue("Save Successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        savingErrorState.postValue(e.getMessage());
                    }
                });
    }

    public void removeFromRoom(RecipeDetails recipeDetails) {
        recipeRepository.removeRecipeFromRoom(recipeDetails)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        saveOrRemoveSuccessEvent.postValue("Removed Successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        savingErrorState.postValue(e.getMessage());
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
