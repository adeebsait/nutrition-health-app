package com.example.finalyear.ui.inventory.domain;

import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.inventory.data.InventoryRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class SaveInventoryToFireDbUseCase {

    private InventoryRepository inventoryRepository;

    @Inject
    public SaveInventoryToFireDbUseCase(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Single<String> invoke(InventoryItem scanItem) {
        return inventoryRepository.saveInventoryItem(scanItem);
    }
}
