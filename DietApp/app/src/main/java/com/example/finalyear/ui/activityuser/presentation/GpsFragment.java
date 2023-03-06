package com.example.finalyear.ui.activityuser.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalyear.databinding.FragmentGpsBinding;
import com.mapbox.common.location.AccuracyAuthorization;
import com.mapbox.common.location.LocationServiceObserver;
import com.mapbox.common.location.PermissionStatus;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.locationcomponent.DefaultLocationProvider;
import com.mapbox.maps.plugin.locationcomponent.LocationProvider;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GpsFragment extends Fragment {

    private FragmentGpsBinding binding = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGpsBinding.inflate(inflater);
        return binding.getRoot();
    }


    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);


    }

}