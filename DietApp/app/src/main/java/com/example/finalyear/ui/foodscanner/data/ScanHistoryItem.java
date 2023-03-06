package com.example.finalyear.ui.foodscanner.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.finalyear.ui.inventory.data.InventoryItem;

import java.util.Objects;
@Entity(tableName = "scanhistory")
public class ScanHistoryItem {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String code;
    private Long scanDate = System.currentTimeMillis();

    public ScanHistoryItem() {
    }

    @Ignore
    public ScanHistoryItem(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Long getScanDate() {
        return scanDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setScanDate(Long scanDate) {
        this.scanDate = scanDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScanHistoryItem that = (ScanHistoryItem) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(scanDate, that.scanDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, scanDate);
    }
}
