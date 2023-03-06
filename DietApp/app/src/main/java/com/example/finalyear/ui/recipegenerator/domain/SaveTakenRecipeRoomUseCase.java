package com.example.finalyear.ui.recipegenerator.domain;

import com.example.finalyear.ui.recipegenerator.TakenRecipeEntity;
import com.example.finalyear.ui.recipegenerator.data.RecipeRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class SaveTakenRecipeRoomUseCase {
    private RecipeRepository recipeRepository;

    @Inject
    public SaveTakenRecipeRoomUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    public Completable invoke(TakenRecipeEntity takenRecipeEntity){
        return recipeRepository.saveTakenRecipeRoom(takenRecipeEntity);
    }
}
