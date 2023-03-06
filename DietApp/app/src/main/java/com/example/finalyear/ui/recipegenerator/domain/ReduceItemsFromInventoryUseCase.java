package com.example.finalyear.ui.recipegenerator.domain;

import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.inventory.data.InventoryRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class ReduceItemsFromInventoryUseCase {
    private InventoryRepository inventoryRepository;

    @Inject
    public ReduceItemsFromInventoryUseCase(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Single<Boolean> invoke(List<InventoryItem> list){
        return inventoryRepository.reduceInventoryItem(list);
    }
}
