package com.example.finalyear.ui.recipegenerator.presentation.takerecipe;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyear.common.utils.AdapterListener;
import com.example.finalyear.common.utils.Utility;
import com.example.finalyear.databinding.ItemInventoryBinding;
import com.example.finalyear.ui.recipegenerator.TakenRecipeEntity;


public class TakenRecipeListAdapter extends ListAdapter<TakenRecipeEntity, TakenRecipeListAdapter.MyViewHolder> {

    private AdapterListener<String> adapterListener;

    protected TakenRecipeListAdapter(@NonNull ItemCallback<TakenRecipeEntity> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemInventoryBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemInventoryBinding binding;

        public MyViewHolder(@NonNull ItemInventoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                // adapterListener.onAdapterItemClick();
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull TakenRecipeEntity item) {
            binding.itemInventoryText.setText(item.getRecipe_name());
            if (item.getTaken_date() != null) {
                String date = Utility.millisToDate(item.getTaken_date());
                binding.itemInventoryExpiry.setText("Date: " + date);
            }
            binding.itemInventoryQuantity.setText(String.valueOf(item.getCalories()) + " Cal");
        }
    }

    public void onItemClick(AdapterListener<String> adapterListener) {
        this.adapterListener = adapterListener;
    }

    static class InventoryDiff extends ItemCallback<TakenRecipeEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull TakenRecipeEntity oldItem, @NonNull TakenRecipeEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TakenRecipeEntity oldItem, @NonNull TakenRecipeEntity newItem) {
            return oldItem.equals(newItem);
        }
    }

}
