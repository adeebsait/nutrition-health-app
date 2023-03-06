package com.example.finalyear.ui.foodscanner.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.utils.DietObserver;
import com.example.finalyear.common.utils.DietSingleObserve;
import com.example.finalyear.common.utils.SingleLiveEvent;
import com.example.finalyear.ui.foodscanner.data.ScanHistoryItem;
import com.example.finalyear.ui.foodscanner.data.ScannerRepository;
import com.example.finalyear.ui.inventory.data.InventoryItem;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ScannerViewModel extends ViewModel {
    private final ScannerRepository scannerRepository;
    private final InventoryItem scanItem;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ScannerViewModel(SavedStateHandle savedStateHandle, ScannerRepository repository) {
        scanItem = savedStateHandle.get("scandata");
        this.scannerRepository = repository;
    }

    public InventoryItem getInventoryItem() {
        return scanItem;
    }
    public SingleLiveEvent<Boolean> getSavedScanLoadingState = new SingleLiveEvent<>();

    private final SingleLiveEvent<String> scanSubmit = new SingleLiveEvent<String>();

    private final MutableLiveData<List<InventoryItem>> listScanSavedFire = new MutableLiveData<>();

    public LiveData<List<InventoryItem>> getSavedItem() {
        getScannerList();
        return listScanSavedFire;
    }

    private void getScannerList() {
        scannerRepository.getScanItem()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietSingleObserve<List<InventoryItem>>() {
                    @Override
                    public void onLoading(boolean aBoolean) {
                        getSavedScanLoadingState.postValue(aBoolean);
                    }

                    @Override
                    public void onComplete(List<InventoryItem> list) {
                        listScanSavedFire.postValue(list);
                    }
                });
    }

    private final MutableLiveData<List<ScanHistoryItem>> history = new MutableLiveData<>();


    public LiveData<List<ScanHistoryItem>> getHistory() {
        scannerRepository.getHistory()
                .subscribeOn(Schedulers.io())
                .subscribe(new DietObserver<List<ScanHistoryItem>>() {
                    @Override
                    public void onNext(List<ScanHistoryItem> scanHistoryItems) {
                        history.postValue(scanHistoryItems);
                        super.onNext(scanHistoryItems);
                    }
                });
        return history;
    }



    public SingleLiveEvent<String> scanLiveData = new SingleLiveEvent<>();

    public void setTextAfterScan(String barcode) {
        scanLiveData.setValue(barcode);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
