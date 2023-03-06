package com.example.finalyear.ui.recipegenerator.presentation.takerecipe;

import android.annotation.SuppressLint;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyear.common.utils.MyTextWatcher;
import com.example.finalyear.common.utils.Utility;
import com.example.finalyear.databinding.ItemInventorySelectableBinding;
import com.example.finalyear.ui.inventory.data.InventoryItem;

import java.util.ArrayList;
import java.util.List;


public class SelectableInventoryListAdapter extends ListAdapter<InventoryItem, SelectableInventoryListAdapter.MyViewHolder> {

    private final List<InventoryItem> selectedList = new ArrayList<>();

    protected SelectableInventoryListAdapter(@NonNull ItemCallback<InventoryItem> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemInventorySelectableBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemInventorySelectableBinding binding;

        public MyViewHolder(@NonNull ItemInventorySelectableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                InventoryItem item = getItem(getAdapterPosition());
                if (selectedList.contains(item)) {
                    selectedList.remove(item);
                } else {
                    selectedList.add(item);
                }
                notifyItemChanged(getAdapterPosition());
            });
            TextWatcher textWatcher = (MyTextWatcher) (s, start, before, count) -> {
                try {
                    if(s.toString().isEmpty())return;
                    if (Integer.parseInt(s.toString()) < 1) return;
                    getItem(getAdapterPosition()).setQuantity(Integer.parseInt(s.toString()));
                    if (!selectedList.contains(getItem(getAdapterPosition()))) return;
                    selectedList.get(selectedList.indexOf(getItem(getAdapterPosition()))).setQuantity(
                            Integer.parseInt(s.toString())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            binding.itemInventoryQuantity.removeTextChangedListener(textWatcher);
            binding.itemInventoryQuantity.addTextChangedListener(textWatcher);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull InventoryItem item) {
            if (selectedList.contains(item)) {
                binding.getRoot().setStrokeWidth(5);
                binding.checkBox.setChecked(true);
            } else {
                binding.checkBox.setChecked(false);
                binding.getRoot().setStrokeWidth(0);
            }
            binding.itemInventoryText.setText(item.getName());
            if (item.getExpiry() != null) {
                String date = Utility.millisToDate(item.getExpiry());
                binding.itemInventoryExpiry.setText("Exp Date: " + date);
            }
            binding.itemInventoryQuantity.setText(item.getQuantity() == null ? "0"
                    : item.getQuantity().toString());
            binding.itemInventoryQuantityLay.setSuffixText(item.getQuantityUnit());
            binding.itemInventoryQuantity.setFilters(new InputFilter.LengthFilter[]
                    {new InputFilter.LengthFilter(item.getQuantity() == null ? 0
                            : item.getQuantity())});
        }


    }

    public List<InventoryItem> getSelectedList() {
        return selectedList;
    }


    static class InventoryDiff extends ItemCallback<InventoryItem> {

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
