package com.example.finalyear.ui.recipegenerator.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes")
    Flowable<List<RecipeDetails>> getRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRecipe(RecipeDetails recipeDetails);

    @Delete
    Completable removeRecipe(RecipeDetails recipeDetails);

    @Query("SELECT EXISTS(SELECT * FROM recipes WHERE id = :id)")
    Flowable<Boolean> isRecipeExist(String id);
}
