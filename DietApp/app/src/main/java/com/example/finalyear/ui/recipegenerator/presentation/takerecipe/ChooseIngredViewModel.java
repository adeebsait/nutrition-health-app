package com.example.finalyear.ui.recipegenerator.presentation.takerecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.recipegenerator.TakenRecipeEntity;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeDetails;
import com.example.finalyear.ui.recipegenerator.domain.AddFireCaloriesUseCase;
import com.example.finalyear.ui.recipegenerator.domain.GetLowerQuantityInventoryItemsUseCase;
import com.example.finalyear.ui.recipegenerator.domain.ReduceItemsFromInventoryUseCase;
import com.example.finalyear.ui.recipegenerator.domain.SaveTakenRecipeRoomUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ChooseIngredViewModel extends ViewModel {

    private final GetLowerQuantityInventoryItemsUseCase getLowerQuantityInventoryItemsUseCase;

    private final SaveTakenRecipeRoomUseCase saveTakenRecipeRoomUseCase;
    private final AddFireCaloriesUseCase addFireCaloriesUseCase;
    private final ReduceItemsFromInventoryUseCase reduceItemsFromInventoryUseCase;
    private RecipeDetails recipeDetails;

    public RecipeDetails getRecipeDetails() {
        return recipeDetails;
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ChooseIngredViewModel(SavedStateHandle savedStateHandle,
                                 GetLowerQuantityInventoryItemsUseCase getLowerQuantityInventoryItemsUseCase,
                                 SaveTakenRecipeRoomUseCase saveTakenRecipeRoomUseCase,
                                 AddFireCaloriesUseCase addFireCaloriesUseCase,
                                 ReduceItemsFromInventoryUseCase reduceItemsFromInventoryUseCase) {
        this.recipeDetails = savedStateHandle.get("details");
        this.reduceItemsFromInventoryUseCase = reduceItemsFromInventoryUseCase;
        this.getLowerQuantityInventoryItemsUseCase = getLowerQuantityInventoryItemsUseCase;
        this.saveTakenRecipeRoomUseCase = saveTakenRecipeRoomUseCase;
        this.addFireCaloriesUseCase = addFireCaloriesUseCase;
        getInventoryItems();
    }

    public final SingleLiveEvent<String> inventoryError = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> inventoryLoading = new MutableLiveData<>();

    public final LiveData<Boolean> getInventoryLoading = inventoryLoading;
    private final MutableLiveData<List<InventoryItem>> inventoryItems = new MutableLiveData<>();
    public final LiveData<List<InventoryItem>> getInventoryItems = inventoryItems;

    public void getInventoryItems() {
        getLowerQuantityInventoryItemsUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<List<InventoryItem>>() {
                    @Override
                    public void onLoading(boolean aBoolean) {
                        inventoryLoading.postValue(aBoolean);
                    }

                    @Override
                    public void onComplete(List<InventoryItem> inventoryItem) {
                        inventoryItems.postValue(inventoryItem);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        inventoryError.postValue("Item loading failed. Try later");
                    }
                });
    }

    public final SingleLiveEvent<String> saveTakenSuccessState = new SingleLiveEvent<>();
    public final SingleLiveEvent<String> saveTakenErrorState = new SingleLiveEvent<>();
    public final SingleLiveEvent<Boolean> saveTakenLoadingState = new SingleLiveEvent<>();


    public void reduceItemsAndUpdateData(List<InventoryItem> inventoryItemList, int calories) {
        reduceItemsFromInventoryUseCase.invoke(inventoryItemList)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<Boolean>() {
                    @Override
                    public void onLoading(boolean aBoolean) {
                        saveTakenLoadingState.postValue(true);
                    }

                    @Override
                    public void onComplete(Boolean aBoolean) {
                        addCaloriesToFireAndSaveToRoom(calories);
                    }
                });
    }


    private void addCaloriesToFireAndSaveToRoom(int calories) {
        addFireCaloriesUseCase.invoke((long) calories)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Boolean aBoolean) {
                        saveTakenRecipeToRoom();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        saveTakenLoadingState.postValue(false);
                        saveTakenErrorState.postValue(e.getMessage());
                    }
                });
    }

    private void saveTakenRecipeToRoom() {
        saveTakenRecipeRoomUseCase.invoke(
                        new TakenRecipeEntity(getRecipeDetails().getRecipeName(),
                                getRecipeDetails().getId(),
                                (int) getRecipeDetails().getCalories())
                ).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        saveTakenSuccessState.postValue("Successfully Cooked");
                        saveTakenLoadingState.postValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        saveTakenLoadingState.postValue(false);
                        saveTakenErrorState.postValue(e.getMessage());
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
