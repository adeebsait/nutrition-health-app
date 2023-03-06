package com.example.finalyear.ui.recipegenerator.domain;

import com.example.finalyear.ui.recipegenerator.TakenRecipeEntity;
import com.example.finalyear.ui.recipegenerator.data.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class RetrieveTakenListUseCase {
    private RecipeRepository recipeRepository;

    @Inject
    public RetrieveTakenListUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Observable<List<TakenRecipeEntity>> invoke(){
        return recipeRepository.getTakenList();
    }
}
