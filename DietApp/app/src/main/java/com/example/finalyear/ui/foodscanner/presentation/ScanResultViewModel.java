package com.example.finalyear.ui.foodscanner.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietObserver;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.foodscanner.domain.GetFoodByScanUseCase;
import com.example.finalyear.ui.foodscanner.domain.RemoveScanItemFireDbUseCase;
import com.example.finalyear.ui.foodscanner.domain.SaveToHistoryUseCase;
import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.inventory.domain.SaveInventoryToFireDbUseCase;
import com.example.finalyear.ui.inventory.domain.SaveScanToFireDbUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ScanResultViewModel extends ViewModel {
    private final GetFoodByScanUseCase getFoodByScanUseCase;
    private final SaveScanToFireDbUseCase saveScanToFireDbUseCase;
    private final RemoveScanItemFireDbUseCase removeScanItemFireDbUseCase;
    private final SaveToHistoryUseCase saveToHistoryUseCase;

    private String foodCode = "";

    public String getFoodCode() {
        return foodCode;
    }

    public InventoryItem retrievedItem = null;

    @Inject
    public ScanResultViewModel(SavedStateHandle savedStateHandle,
                               GetFoodByScanUseCase getFoodByScanUseCase,
                               SaveToHistoryUseCase saveToHistoryUseCase,
                               SaveScanToFireDbUseCase saveScanToFireDbUseCase,
                               RemoveScanItemFireDbUseCase removeScanItemFireDbUseCase) {
        this.foodCode = savedStateHandle.get("foodcode");
        this.retrievedItem = savedStateHandle.get("item");
        this.getFoodByScanUseCase = getFoodByScanUseCase;
        this.saveScanToFireDbUseCase = saveScanToFireDbUseCase;
        this.saveToHistoryUseCase = saveToHistoryUseCase;
        this.removeScanItemFireDbUseCase = removeScanItemFireDbUseCase;
        if (foodCode != null) {
            getScanFoodByUpcCode(foodCode);
            saveScannItemToHistory(foodCode);
        }
        if (retrievedItem != null){
            populateInventoryItem(retrievedItem);
        }

    }


    private final MutableLiveData<Boolean> isLoadingState = new MutableLiveData<>();
    public LiveData<Boolean> getLoadingState = isLoadingState;


    public SingleLiveEvent<String> scanErrorState = new SingleLiveEvent<>();
    public SingleLiveEvent<String> universalSuccessEvent = new SingleLiveEvent<>();

    private final MutableLiveData<InventoryItem> isSuccessState = new MutableLiveData<>();
    public LiveData<InventoryItem> getSuccessState = isSuccessState;

    public void getScanFoodByUpcCode(String foodCode) {
        getFoodByScanUseCase.invoke(foodCode)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<InventoryItem>() {
                    @Override
                    public void onLoading(boolean aBoolean) {
                        isLoadingState.postValue(aBoolean);
                    }

                    @Override
                    public void onComplete(InventoryItem scanHistoryItem) {
                        isSuccessState.postValue(scanHistoryItem);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        scanErrorState.postValue(e.getMessage());
                    }
                });
    }

    private void populateInventoryItem(InventoryItem inventoryItem) {
        isSuccessState.postValue(inventoryItem);
        isLoadingState.postValue(false);
    }
    public void saveScannItemToHistory(String foodCode) {
        saveToHistoryUseCase.invoke(foodCode)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                    }
                });
    }

    private final MutableLiveData<String> scanSaveSuccessState = new MutableLiveData<>();
    public LiveData<String> getScanSaveState = scanSaveSuccessState;


    public void saveScanItem(InventoryItem scanItem) {
        saveScanToFireDbUseCase.invoke(scanItem)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<String>() {
                    @Override
                    public void onComplete(String savedItemKey) {
                        scanSaveSuccessState.postValue(savedItemKey);
                        universalSuccessEvent.postValue("Added to save list");
                    }
                });

    }

    public void removeScanSavedItem(String key) {
        removeScanItemFireDbUseCase.invoke(key)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<String>() {
                    @Override
                    public void onComplete(String s) {
                        universalSuccessEvent.postValue("Removed from save list");
                    }
                });
    }
}

