package com.example.finalyear.ui.recipegenerator.presentation.matchedinventory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipePojo;
import com.example.finalyear.ui.recipegenerator.domain.GetLowerQuantityInventoryUseCase;
import com.example.finalyear.ui.recipegenerator.domain.GetMatchRecipeUseCase;
import com.example.finalyear.ui.recipegenerator.domain.GetRecipeUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RecipeMatchedInventoryViewModel extends ViewModel {

    private final GetMatchRecipeUseCase getRecipeUseCase;
    private final GetLowerQuantityInventoryUseCase getLowerQuantityInventoryUseCase;

    @Inject
    public RecipeMatchedInventoryViewModel(GetMatchRecipeUseCase getRecipeUseCase,
                                           GetLowerQuantityInventoryUseCase getLowerQuantityInventoryUseCase) {
        this.getRecipeUseCase = getRecipeUseCase;
        this.getLowerQuantityInventoryUseCase = getLowerQuantityInventoryUseCase;
        init();
    }


    public final SingleLiveEvent<String> getErrorState = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public LiveData<Boolean> getLoadingState = loadingState;

    private final MutableLiveData<RecipePojo> recipesState = new MutableLiveData<>();

    private final MutableLiveData<String> queryStringState = new MutableLiveData<>("");
    public LiveData<RecipePojo> getRecipesState = Transformations.switchMap(queryStringState, this::getRecipes);

    private void init() {
        getLowerQuantityInventoryUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<InventoryItem>() {
                    @Override
                    public void onLoading(boolean aBoolean) {
                        loadingState.postValue(true);
                    }

                    @Override
                    public void onComplete(InventoryItem inventoryItem) {
                        queryStringState.postValue(inventoryItem.getName());
                    }
                });
    }

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
}
