package com.example.finalyear.ui.inventory.presentation;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyear.common.utils.AdapterListener;
import com.example.finalyear.common.utils.Utility;
import com.example.finalyear.databinding.ItemInventoryBinding;
import com.example.finalyear.ui.inventory.data.InventoryItem;


public class InventoryListAdapter extends ListAdapter<InventoryItem, InventoryListAdapter.MyViewHolder> {

    private AdapterListener<String> adapterListener;

    protected InventoryListAdapter(@NonNull ItemCallback<InventoryItem> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemInventoryBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false));
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
                adapterListener.onAdapterItemClick(getItem(getAdapterPosition()).getId());
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull InventoryItem item) {
            binding.itemInventoryText.setText(item.getName());
            if (item.getExpiry() != null) {
                String date = Utility.millisToDate(item.getExpiry());
                binding.itemInventoryExpiry.setText("Exp Date: "+date);
            }
            binding.itemInventoryQuantity.setText(item.getQuantity() == null ? "0"
                    : item.getQuantity().toString()+" "+item.getQuantityUnit());
        }
    }

    public void onItemClick(AdapterListener<String> adapterListener) {
        this.adapterListener = adapterListener;
    }

    static class InventoryDiff extends DiffUtil.ItemCallback<InventoryItem> {

        @Override
        public boolean areItemsTheSame(@NonNull InventoryItem oldItem, @NonNull InventoryItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull InventoryItem oldItem, @NonNull InventoryItem newItem) {
            return oldItem.equals(newItem);
        }
    }

}
