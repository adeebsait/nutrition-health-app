package com.example.finalyear.ui.recipegenerator.presentation.recipegenerator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipePojo;
import com.example.finalyear.ui.recipegenerator.domain.GetRecipeUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RecipeGeneratorViewModel extends ViewModel {
    private final GetRecipeUseCase getRecipeUseCase;

    @Inject
    public RecipeGeneratorViewModel(GetRecipeUseCase getRecipeUseCase) {
        this.getRecipeUseCase = getRecipeUseCase;
    }

    public final SingleLiveEvent<String> getErrorState = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public LiveData<Boolean> getLoadingState = loadingState;

    private final MutableLiveData<RecipePojo> recipesState = new MutableLiveData<>();

    private final MutableLiveData<String> queryStringState = new MutableLiveData<>("");
    public LiveData<RecipePojo> getRecipesState = Transformations.switchMap(queryStringState, this::getRecipes);

    private LiveData<RecipePojo> getRecipes(String qString) {
        getRecipeUseCase.invoke(qString)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<RecipePojo>() {
                    @Override
                    public void onLoading(boolean aBoolean) {
                        loadingState.postValue(aBoolean);
                    }

                    @Override
                    public void onComplete(RecipePojo recipePojo) {
                        recipesState.postValue(recipePojo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getErrorState.postValue("Try again later");
                    }
                });
        return recipesState;
    }

    public void setQueryString(String query) {
        queryStringState.postValue(query);
    }
}
