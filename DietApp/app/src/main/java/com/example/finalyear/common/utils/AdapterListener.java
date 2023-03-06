package com.example.finalyear.common.utils;

import com.example.finalyear.ui.inventory.data.InventoryItem;

public interface AdapterListener<T>{
    void onAdapterItemClick(T obj);
}