package com.example.finalyear.ui.recipegenerator.domain;

import com.example.finalyear.ui.recipegenerator.data.RecipeRepository;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipePojo;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetRecipeUseCase {
    private RecipeRepository recipeRepository;

    @Inject
    public GetRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    public Single<RecipePojo> invoke(String ingredient){
       return recipeRepository.getRecipes(ingredient);
    }
}
