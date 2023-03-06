package com.example.finalyear.ui.recipegenerator.data;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalyear.common.apiservice.FoodApi;
import com.example.finalyear.common.utils.DietConstants;
import com.example.finalyear.db.DietRoomDb;
import com.example.finalyear.ui.recipegenerator.TakenRecipeEntity;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeDetails;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipePojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private final FoodApi foodApi;
    private final DietRoomDb dietRoomDb;

    private final DatabaseReference firebaseDatabase;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public RecipeRepository(FoodApi foodApi, DietRoomDb dietRoomDb) {
        this.foodApi = foodApi;
        this.dietRoomDb = dietRoomDb;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = FirebaseDatabase.getInstance().getReference(DietConstants.USER_DATA_NODE);
    }


    public Single<RecipePojo> getRecipes(String ingredient) {
        return Single.create(emitter -> {
            Call<RecipePojo> cb = foodApi.getFood(ingredient);
            cb.enqueue(new Callback<RecipePojo>() {
                @Override
                public void onResponse(@NonNull Call<RecipePojo> call, @NonNull Response<RecipePojo> response) {
                    if (response.errorBody() != null) {
                        emitter.onError(new Throwable(response.errorBody().toString()));
                        return;
                    }
                    emitter.onSuccess(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<RecipePojo> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }

    public Single<RecipePojo> getMatchRecipe(String ingredient) {
        return Single.create(emitter -> {
            Call<RecipePojo> cb = foodApi.getFoodMatched(ingredient);
            cb.enqueue(new Callback<RecipePojo>() {
                @Override
                public void onResponse(@NonNull Call<RecipePojo> call, @NonNull Response<RecipePojo> response) {
                    if (response.errorBody() != null) {
                        emitter.onError(new Throwable(response.errorBody().toString()));
                        return;
                    }
                    emitter.onSuccess(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<RecipePojo> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }

    public Completable saveRecipe(RecipeDetails recipeDetails) {
        return dietRoomDb.recipeDao().insertRecipe(recipeDetails);
    }

    public Completable removeRecipeFromRoom(RecipeDetails recipeDetails) {
        return dietRoomDb.recipeDao().removeRecipe(recipeDetails);
    }

    public Observable<List<RecipeDetails>> getRecipesRoomDb() {
        return dietRoomDb.recipeDao().getRecipes().toObservable();
    }

    public Observable<Boolean> getRecipeExistInRoom(String id) {
        return dietRoomDb.recipeDao().isRecipeExist(id).toObservable();
    }

    public Single<Boolean> addCalories(Long mCalorie) {
        return Single.create(emitter -> {
            if (firebaseAuth.getCurrentUser() == null) {
                emitter.onError(new Throwable("User need to logged in"));
                return;
            }
            try {
                String uuid = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference dbs = firebaseDatabase.child(uuid).child(DietConstants.USER_ACTIVITY_NODE)
                        .child("calories");

                dbs.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        Long currentCalories = (Long) currentData.getValue();
                        if (currentCalories == null) {
                            currentData.setValue(mCalorie);
                            return Transaction.success(currentData);
                        }
                        currentData.setValue(currentCalories + mCalorie);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        if (error != null) {
                            emitter.onError(new Throwable(error.getMessage()));
                            return;
                        }
                        emitter.onSuccess(true);
                    }
                });
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Completable saveTakenRecipeRoom(TakenRecipeEntity takenRecipeEntity) {
        return dietRoomDb.takenRecipeDao().insertTakenRecipe(takenRecipeEntity);
    }

    public Observable<List<TakenRecipeEntity>> getTakenList() {
        return dietRoomDb.takenRecipeDao().getTakenList().toObservable();
    }
}
