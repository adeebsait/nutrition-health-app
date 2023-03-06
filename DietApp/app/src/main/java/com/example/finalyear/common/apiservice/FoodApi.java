package com.example.finalyear.common.apiservice;


import com.example.finalyear.ui.foodscanner.data.foodpojo.FoodResponse;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipePojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {
    @GET("recipes/v2?type=public&app_id=38cc78aa&app_key=e4b85e3cf06e105c22ad9928768196c4&calories=10")
    Call<RecipePojo> getFood(@Query("q") String foodname);

    @GET("food-database/v2/parser?app_id=f6f81db5&app_key=0f0fd8fecc2780b73706d33e1a3a49e7&nutrition-type=cooking")
    Call<FoodResponse> getFoodFromBarcode(@Query("upc") String code);
    @GET("recipes/v2?type=public&app_id=38cc78aa&app_key=e4b85e3cf06e105c22ad9928768196c4")
    Call<RecipePojo> getFoodMatched(@Query("q") String foodname);
}
