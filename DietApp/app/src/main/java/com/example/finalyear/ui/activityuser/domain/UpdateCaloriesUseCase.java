package com.example.finalyear.ui.activityuser.domain;

import androidx.annotation.Nullable;

import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;
import com.example.finalyear.ui.authentication.data.DietFireDbRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class UpdateCaloriesUseCase {
    private final DietFireDbRepository dietFireDbRepository;
    private final DietFireAuthRepository dietFireAuthRepository;

    @Inject
    public UpdateCaloriesUseCase(DietFireDbRepository dietFireDbRepository, DietFireAuthRepository dietFireAuthRepository) {
        this.dietFireDbRepository = dietFireDbRepository;
        this.dietFireAuthRepository = dietFireAuthRepository;
    }


    public @NonNull Single<Long> invoke(Long burnAmount) {
        return Single.create(emitter -> {
            dietFireDbRepository.updateCalories(dietFireAuthRepository.getUid())
                    .runTransaction(new Transaction.Handler() {
                        @androidx.annotation.NonNull
                        @Override
                        public Transaction.Result doTransaction(@androidx.annotation.NonNull MutableData currentData) {
                            try {
                                long prevCalories = 0;
                                if (currentData.getValue() != null) {
                                    prevCalories = (long) currentData.getValue();
                                }
                                // ---------------- subtract burn calories
                                long calcAmount = prevCalories - burnAmount;

                                currentData.setValue(calcAmount);
                                return Transaction.success(currentData);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return Transaction.abort();
                            }

                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                            if(error!=null || !committed){
                                emitter.onError(new Throwable("Burn Calculation Failed"));
                                return;
                            }
                            emitter.onSuccess((Long) currentData.getValue());
                        }
                    });
        });

    }
}
