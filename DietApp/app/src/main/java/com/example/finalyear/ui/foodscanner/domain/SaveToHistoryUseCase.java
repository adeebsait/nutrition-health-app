package com.example.finalyear.ui.foodscanner.domain;

import com.example.finalyear.ui.foodscanner.data.ScannerRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class SaveToHistoryUseCase {
    private ScannerRepository scannerRepository;

    @Inject
    public SaveToHistoryUseCase(ScannerRepository scannerRepository) {
        this.scannerRepository = scannerRepository;
    }
    public Observable<Object> invoke(String code){
        return scannerRepository.saveHistory(code);
    }
}
