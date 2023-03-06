package com.example.finalyear.ui.recipegenerator.presentation.recipegenerator;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyear.common.utils.AdapterListener;
import com.example.finalyear.databinding.ItemRecipeBinding;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.Recipe;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeDetails;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private AdapterListener<RecipeDetails> adapterListener;
    private final List<Recipe> list = new ArrayList<>();

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe item = list.get(position);
        holder.bind(item);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecipeBinding binding;

        public RecipeViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                adapterListener.onAdapterItemClick(list.get(getAdapterPosition()).getRecipe());
            });
        }

        public void bind(Recipe item) {
            binding.itemRecipeTitle.setText(item.getRecipe().getRecipeName());
            Glide.with(itemView).load(item.getRecipe().getImages().getTHUMBNAIL().getUrl())
                    .into(binding.itemRecipeImageView);
        }
    }

    public void onItemClick(AdapterListener<RecipeDetails> adapterListener) {
        this.adapterListener = adapterListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(@NonNull List<Recipe> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}
