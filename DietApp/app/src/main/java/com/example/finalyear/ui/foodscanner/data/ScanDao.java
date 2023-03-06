package com.example.finalyear.ui.foodscanner.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.finalyear.ui.inventory.data.InventoryItem;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ScanDao {
    @Query("SELECT * FROM scanhistory")
    public Flowable<List<ScanHistoryItem>> getScanItems();

    @Insert
    Completable insertScan(ScanHistoryItem scanHistoryItem);

    @Delete
    public void deleteScan(ScanHistoryItem scanHistoryItem);
}
