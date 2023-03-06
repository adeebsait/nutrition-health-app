package com.example.finalyear.ui.recipegenerator.presentation.savelists;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyear.common.utils.AdapterListener;
import com.example.finalyear.databinding.ItemRecipeBinding;
import com.example.finalyear.ui.recipegenerator.data.recipepojos.RecipeDetails;

import java.util.ArrayList;
import java.util.List;

public class RecipeSavedListAdapter extends RecyclerView.Adapter<RecipeSavedListAdapter.RecipeViewHolder> {
    private SharedAdapterListener<RecipeDetails> adapterListener;
    private final List<RecipeDetails> list = new ArrayList<>();

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
        RecipeDetails item = list.get(position);
        holder.bind(item);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecipeBinding binding;

        public RecipeViewHolder(ItemRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                adapterListener.onAdapterItemClick(list.get(getAdapterPosition()),binding.itemRecipeImageView);
            });
        }

        public void bind(RecipeDetails item) {
            binding.itemRecipeTitle.setText(item.getRecipeName());
            Glide.with(itemView)
                    .load(item.getImages().getTHUMBNAIL().getUrl())
                    .into(binding.itemRecipeImageView);
        }
    }

    public void onItemClick(SharedAdapterListener<RecipeDetails> adapterListener) {
        this.adapterListener = adapterListener;
    }
    public interface SharedAdapterListener<T>{
        void onAdapterItemClick(T obj, View v);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(@NonNull List<RecipeDetails> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}
