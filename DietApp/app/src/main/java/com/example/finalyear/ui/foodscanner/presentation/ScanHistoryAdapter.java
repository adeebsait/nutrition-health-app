package com.example.finalyear.ui.foodscanner.presentation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyear.common.utils.AdapterListener;
import com.example.finalyear.common.utils.Utility;
import com.example.finalyear.databinding.ItemInventoryBinding;
import com.example.finalyear.ui.foodscanner.data.ScanHistoryItem;
import com.example.finalyear.ui.inventory.data.InventoryItem;


public class ScanHistoryAdapter extends ListAdapter<ScanHistoryItem, ScanHistoryAdapter.MyViewHolder> {

    private AdapterListener<String> adapterListener;

    protected ScanHistoryAdapter(@NonNull ItemCallback<ScanHistoryItem> diffCallback) {
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

        public MyViewHolder(ItemInventoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                adapterListener.onAdapterItemClick(getItem(getAdapterPosition()).getId().toString());
            });
        }

        public void bind(ScanHistoryItem item) {
            binding.itemInventoryText.setText(item.getCode());
            if (item.getScanDate() != null) {
                String date = Utility.millisToDate(item.getScanDate());
                binding.itemInventoryExpiry.setText(date);
            }
            binding.itemInventoryQuantity.setText("");
        }
    }

    public void onItemClick(AdapterListener<String> adapterListener) {
        this.adapterListener = adapterListener;
    }

    static class ScanDiff extends ItemCallback<ScanHistoryItem> {

        @Override
        public boolean areItemsTheSame(@NonNull ScanHistoryItem oldItem, @NonNull ScanHistoryItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ScanHistoryItem oldItem, @NonNull ScanHistoryItem newItem) {
            return oldItem.equals(newItem);
        }
    }

}
