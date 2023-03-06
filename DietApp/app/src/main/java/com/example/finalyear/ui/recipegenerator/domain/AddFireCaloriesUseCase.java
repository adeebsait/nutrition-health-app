package com.example.finalyear.ui.recipegenerator.domain;

import com.example.finalyear.ui.recipegenerator.data.RecipeRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class AddFireCaloriesUseCase {
    private RecipeRepository recipeRepository;

    @Inject
    public AddFireCaloriesUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Single<Boolean> invoke(long calories){
        return recipeRepository.addCalories(calories);
    }
}
