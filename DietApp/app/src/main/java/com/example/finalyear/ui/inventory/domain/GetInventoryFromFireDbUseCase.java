package com.example.finalyear.ui.inventory.domain;

import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.inventory.data.InventoryRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetInventoryFromFireDbUseCase {

    private InventoryRepository inventoryRepository;

    @Inject
    public GetInventoryFromFireDbUseCase(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Observable<List<InventoryItem>> invoke() {
        return inventoryRepository.getInventoryList();
    }
}
