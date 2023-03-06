package com.example.finalyear.ui.inventory.domain;

import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.inventory.data.InventoryRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class SaveScanToFireDbUseCase {

    private InventoryRepository inventoryRepository;

    @Inject
    public SaveScanToFireDbUseCase(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Single<String> invoke(InventoryItem scanItem) {
        return inventoryRepository.saveScanItem(scanItem);
    }
}
