package com.example.finalyear.ui.recipegenerator.presentation.savelists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietObserver;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.recipegenerator.data.RecipeRepository;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeDetails;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RecipeSaveListViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;

    @Inject
    public RecipeSaveListViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // ------------------------------------- Retrieve Recipes From Room
    private final MutableLiveData<Boolean> retrieveLoadingState = new MutableLiveData<>();
    public final LiveData<Boolean> getRetrieveLoadingState = retrieveLoadingState;
    private final MutableLiveData<List<RecipeDetails>> savedListFromDb = new MutableLiveData<>();

    public LiveData<List<RecipeDetails>> getSavedRecipes() {
        recipeRepository.getRecipesRoomDb()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietObserver<List<RecipeDetails>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        retrieveLoadingState.postValue(true);
                    }

                    @Override
                    public void onNext(List<RecipeDetails> recipeDetails) {
                        savedListFromDb.postValue(recipeDetails);
                        retrieveLoadingState.postValue(false);
                    }
                });
        return savedListFromDb;
    }

}
