package com.example.finalyear.ui.recipegenerator.presentation.recipedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyear.databinding.ItemRecipeIngredientBinding;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeIngredients;

public class RecipeDetailRecAdapter extends ListAdapter<RecipeIngredients, RecipeDetailRecAdapter.MyHolder> {

    protected RecipeDetailRecAdapter(@NonNull DiffUtil.ItemCallback<RecipeIngredients> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(
                ItemRecipeIngredientBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final ItemRecipeIngredientBinding binding;
        public MyHolder(@NonNull ItemRecipeIngredientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
        public void bind(RecipeIngredients item){
            binding.recipeIngrNumbering.setText(String.valueOf(getAdapterPosition()+1));
            binding.recipeIngrText.setText(item.getText());
        }
    }

    static class DiffCallBack extends DiffUtil.ItemCallback<RecipeIngredients> {

        @Override
        public boolean areItemsTheSame(@NonNull RecipeIngredients oldItem, @NonNull RecipeIngredients newItem) {
            return oldItem.getFoodId().equals(newItem.getFoodId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RecipeIngredients oldItem, @NonNull RecipeIngredients newItem) {
            return oldItem.equals(newItem);

        }
    }
}
