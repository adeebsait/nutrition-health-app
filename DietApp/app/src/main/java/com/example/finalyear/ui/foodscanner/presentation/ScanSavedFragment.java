package com.example.finalyear.ui.foodscanner.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalyear.databinding.FragmentScanSavedBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanSavedFragment extends Fragment {
    private FragmentScanSavedBinding binding = null;
    private ScannerViewModel mViewModel = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        binding = FragmentScanSavedBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        ScanListAdapter adapter = new ScanListAdapter(new ScanListAdapter.ScanDiff());

        LinearLayoutManager lm = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.scanSaveRecyclerView.setLayoutManager(lm);
        binding.scanSaveRecyclerView.setAdapter(adapter);
        binding.scanSaveRecyclerView.setHasFixedSize(true);
        binding.scanSaveRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext()
                , LinearLayoutManager.VERTICAL));

        adapter.onItemClick(inventoryItem -> {
            if (inventoryItem == null) return;
            NavDirections action = ScanSavedFragmentDirections
                    .actionGlobalScanResultFragment(null,inventoryItem);
            Navigation.findNavController(view).navigate(action);
        });

        mViewModel.getSavedScanLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) binding.pIndicator.setVisibility(View.VISIBLE);
            else binding.pIndicator.setVisibility(View.GONE);
        });

        mViewModel.getSavedItem().observe(getViewLifecycleOwner(), list -> {
            if (list == null) return;
            adapter.submitList(list);
        });
    }
}