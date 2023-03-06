package com.example.finalyear.ui.recipegenerator.data;

import androidx.room.TypeConverter;

import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeIngredients;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataRoomConverter {

    @TypeConverter
    public String fromCountryLangList(List<RecipeIngredients> countryLang) {
        if (countryLang == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<RecipeIngredients>>() {
        }.getType();
        return gson.toJson(countryLang, type);
    }

    @TypeConverter
    public List<RecipeIngredients> toCountryLangList(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<RecipeIngredients>>() {
        }.getType();
        return gson.fromJson(countryLangString, type);
    }
}