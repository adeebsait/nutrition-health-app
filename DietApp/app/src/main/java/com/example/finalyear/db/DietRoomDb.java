package com.example.finalyear.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.finalyear.ui.foodscanner.data.ScanDao;
import com.example.finalyear.ui.foodscanner.data.ScanHistoryItem;
import com.example.finalyear.ui.recipegenerator.TakenRecipeEntity;
import com.example.finalyear.ui.recipegenerator.data.DataRoomConverter;
import com.example.finalyear.ui.recipegenerator.data.RecipeDao;
import com.example.finalyear.ui.recipegenerator.data.RecipeEntity;
import com.example.finalyear.ui.recipegenerator.data.TakenRecipeDao;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeDetails;

@Database(entities = {ScanHistoryItem.class, RecipeDetails.class, TakenRecipeEntity.class}, version = 1)
@TypeConverters({DataRoomConverter.class})
public abstract class DietRoomDb extends RoomDatabase {
    public abstract ScanDao scanDao();
    public abstract RecipeDao recipeDao();
    public abstract TakenRecipeDao takenRecipeDao();
}
