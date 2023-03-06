package com.example.finalyear.ui;

import androidx.annotation.MenuRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyear.common.data.BottomMenuItem;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @Inject
    public MainViewModel() {

    }

    private final MutableLiveData<BottomMenuItem> navMenu = new MutableLiveData<>();

    public LiveData<BottomMenuItem> getNavMenu = navMenu;

    public void setBottomNavMenu(@MenuRes int menu, String id) {
        BottomMenuItem item = new BottomMenuItem(id, menu);
        if (navMenu.getValue() != null && navMenu.getValue().equals(item)) return;
        navMenu.setValue(item);
    }

    public void setBottomNavMenu(@MenuRes int menu, String id, int position) {
        BottomMenuItem item = new BottomMenuItem(id, menu, position);
        if (navMenu.getValue() != null && navMenu.getValue().equals(item)) return;
        navMenu.setValue(item);
    }

}
