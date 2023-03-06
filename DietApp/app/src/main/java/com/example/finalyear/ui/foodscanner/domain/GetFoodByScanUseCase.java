package com.example.finalyear.ui.foodscanner.domain;

import com.example.finalyear.ui.foodscanner.data.ScannerRepository;
import com.example.finalyear.ui.foodscanner.data.foodpojo.Food;
import com.example.finalyear.ui.foodscanner.data.foodpojo.FoodResponse;
import com.example.finalyear.ui.inventory.data.InventoryItem;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetFoodByScanUseCase {
    private final ScannerRepository scannerRepository;

    @Inject
    public GetFoodByScanUseCase(ScannerRepository scannerRepository) {
        this.scannerRepository = scannerRepository;
    }

    public @NonNull Single<InventoryItem> invoke(String code) {
        return Single.create(emitter -> {
                    scannerRepository.getFoodByBarCode(code, new Callback<FoodResponse>() {
                        @Override
                        public void onResponse(@androidx.annotation.NonNull Call<FoodResponse> call,
                                               @androidx.annotation.NonNull Response<FoodResponse> response) {
                            if (response.errorBody() != null || response.code() != 200 || response.body() == null) {
                                emitter.onError(new Throwable("No data available"));
                                return;
                            }
                            emitter.onSuccess(response.body());
                        }

                        @Override
                        public void onFailure(@androidx.annotation.NonNull Call<FoodResponse> call,
                                              @androidx.annotation.NonNull Throwable t) {
                            emitter.onError(new Throwable("No data available"));
                        }
                    });
                })
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    try {
                        FoodResponse foodResponse = ((FoodResponse) response);
                        Food food = ((FoodResponse) response).getItem().getFood();
                        return new InventoryItem(foodResponse.getText(), food.getLabel(),
                                food.getFirstSize().getQuantity().intValue(),
                                food.getFirstSize().getLabel(), food.getNutrients().getSugarRounded(),
                                System.currentTimeMillis());
                    } catch (Exception e) {
                        return null;
                    }
                });
    }
}
