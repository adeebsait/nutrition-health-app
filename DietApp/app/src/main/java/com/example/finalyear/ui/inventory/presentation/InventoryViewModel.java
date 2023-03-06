package com.example.finalyear.ui.inventory.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.common.utils.Utility;
import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.inventory.domain.GetInventoryFromFireDbUseCase;
import com.example.finalyear.ui.inventory.domain.SaveInventoryToFireDbUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class InventoryViewModel extends ViewModel {
    private final SaveInventoryToFireDbUseCase saveInventoryToFireDbUseCase;
    private final GetInventoryFromFireDbUseCase getInventoryFromFireDbUseCase;

    public InventoryItem newItem = new InventoryItem();


    @Inject
    public InventoryViewModel(SaveInventoryToFireDbUseCase saveInventoryToFireDbUseCase,
                              GetInventoryFromFireDbUseCase getInventoryFromFireDbUseCase) {
        this.saveInventoryToFireDbUseCase = saveInventoryToFireDbUseCase;
        this.getInventoryFromFireDbUseCase = getInventoryFromFireDbUseCase;
    }


    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>(false);
    public LiveData<Boolean> getLoadingState = loadingState;
    private final SingleLiveEvent<String> errorState = new SingleLiveEvent<String>();

    private final MutableLiveData<List<InventoryItem>> list = new MutableLiveData<>();

    public LiveData<List<InventoryItem>> getInventory() {
        getInventoryList();
        return list;
    }

    private void getInventoryList() {
        getInventoryFromFireDbUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<InventoryItem>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        loadingState.setValue(true);
                    }

                    @Override
                    public void onNext(@NonNull List<InventoryItem> inventoryItems) {
                        list.setValue(inventoryItems);
                        loadingState.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loadingState.postValue(false);
                        errorState.postValue("Get inventory error");
                    }

                    @Override
                    public void onComplete() {
                        loadingState.postValue(false);
                    }
                });
    }

    public final SingleLiveEvent<String> saveErrorState = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> saveLoadingSate = new MutableLiveData<>();
    public final LiveData<Boolean> getSaveLoadingSate = saveLoadingSate;
    private final MutableLiveData<String> inventorySubmit = new MutableLiveData<>();
    public LiveData<String> getInventorySubmit = inventorySubmit;

    public void saveInventory(InventoryItem scanItem) {
        saveInventoryToFireDbUseCase.invoke(scanItem)
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<String>() {
                    @Override
                    public void onLoading(boolean aBollean) {
                        saveLoadingSate.postValue(aBollean);
                    }

                    @Override
                    public void onComplete(@NonNull String savedItemKey) {
                        inventorySubmit.postValue(savedItemKey);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        errorState.postValue("Could not added inventory");
                    }
                });
    }

    private final MutableLiveData<List<InventoryItem>> toBuyListLiveData = new MutableLiveData<>();
    private final LiveData<List<InventoryItem>> toBuyListSort = Transformations.switchMap(list, newList -> {
        toBuyListLiveData.setValue(Utility.sortWithQuantity(newList));
        return toBuyListLiveData;
    });

    public LiveData<List<InventoryItem>> toBuyList() {
        getInventory();
        return toBuyListSort;
    }


}
