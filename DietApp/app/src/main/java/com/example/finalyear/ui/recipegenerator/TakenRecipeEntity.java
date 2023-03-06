package com.example.finalyear.ui.recipegenerator;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "taken_recipes")
public class TakenRecipeEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String recipe_name;

    public String recipe_code;

    public int calories;

    public Long taken_date = System.currentTimeMillis();

    public TakenRecipeEntity() {
    }

    public long getId() {
        return id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public String getRecipe_code() {
        return recipe_code;
    }

    public int getCalories() {
        return calories;
    }

    public Long getTaken_date() {
        return taken_date;
    }

    @Ignore
    public TakenRecipeEntity(String recipe_name, String recipe_code, int calories) {
        this.recipe_name = recipe_name;
        this.recipe_code = recipe_code;
        this.calories = calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TakenRecipeEntity that = (TakenRecipeEntity) o;
        return id == that.id && calories == that.calories && Objects.equals(recipe_name, that.recipe_name) && Objects.equals(recipe_code, that.recipe_code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipe_name, recipe_code, calories);
    }
}
