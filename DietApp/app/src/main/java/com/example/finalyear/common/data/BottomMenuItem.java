package com.example.finalyear.common.data;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;

import java.util.Objects;

public class BottomMenuItem {
    String id;
    @MenuRes int menu;

    int checkedPosition = 0;

    public BottomMenuItem(String id, int menu) {
        this.id = id;
        this.menu = menu;
    }
    public BottomMenuItem(String id, int menu,int checkedPosition) {
        this.id = id;
        this.menu = menu;
        this.checkedPosition = checkedPosition;
    }


    public String getId() {
        return id;
    }

    public int getMenu() {
        return menu;
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BottomMenuItem that = (BottomMenuItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
