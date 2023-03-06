package com.example.finalyear.ui.recipegenerator.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.finalyear.ui.recipegenerator.TakenRecipeEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface TakenRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTakenRecipe(TakenRecipeEntity takenRecipeEntity);

    @Query("SELECT * FROM taken_recipes")
    Flowable<List<TakenRecipeEntity>> getTakenList();
}
