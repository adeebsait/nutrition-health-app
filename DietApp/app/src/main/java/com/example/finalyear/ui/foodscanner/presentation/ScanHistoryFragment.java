package com.example.finalyear.ui.foodscanner.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalyear.R;
import com.example.finalyear.databinding.FragmentScanHistoryBinding;
import com.example.finalyear.ui.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanHistoryFragment extends Fragment {
    private FragmentScanHistoryBinding binding = null;
    private ScannerViewModel scannerViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.setBottomNavMenu(R.menu.scanner_bottom_menu, "scanner_bottom_menu");

        binding = FragmentScanHistoryBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        ScanHistoryAdapter adapter = new ScanHistoryAdapter(new ScanHistoryAdapter.ScanDiff());

        LinearLayoutManager lm = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.scanHistoryRecyclerView.setLayoutManager(lm);
        binding.scanHistoryRecyclerView.setAdapter(adapter);
        binding.scanHistoryRecyclerView.setHasFixedSize(true);
        binding.scanHistoryRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        adapter.onItemClick(str -> {

        });

        scannerViewModel.getHistory().observe(getViewLifecycleOwner(), list -> {
            if (list == null) return;
            adapter.submitList(list);
        });

    }
}