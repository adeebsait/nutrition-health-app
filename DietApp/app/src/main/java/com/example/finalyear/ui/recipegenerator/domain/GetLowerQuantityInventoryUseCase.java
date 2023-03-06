package com.example.finalyear.ui.recipegenerator.domain;

import androidx.annotation.NonNull;

import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;
import com.example.finalyear.ui.authentication.data.DietFireDbRepository;
import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.example.finalyear.ui.inventory.data.InventoryRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetLowerQuantityInventoryUseCase {
    private InventoryRepository inventoryRepository;

    private DietFireDbRepository dietFireDbRepository;

    private DietFireAuthRepository dietFireAuthRepository;

    @Inject
    public GetLowerQuantityInventoryUseCase(InventoryRepository inventoryRepository,
                                            DietFireDbRepository dietFireDbRepository, DietFireAuthRepository dietFireAuthRepository) {
        this.inventoryRepository = inventoryRepository;
        this.dietFireDbRepository = dietFireDbRepository;
        this.dietFireAuthRepository = dietFireAuthRepository;
    }



    public @io.reactivex.rxjava3.annotations.NonNull Single<InventoryItem> invoke() {
        return Single.create(emitter -> {
            if (!dietFireAuthRepository.getUserIsLoggedIn()) return;
            String uid = dietFireAuthRepository.getUid();
            try {
                dietFireDbRepository.getQueryForInventory(uid, new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            emitter.onSuccess(child.getValue(InventoryItem.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        emitter.onError(error.toException());
                    }
                });
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
