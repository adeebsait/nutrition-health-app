package com.example.finalyear.ui.inventory.presentation;

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

import com.example.finalyear.R;
import com.example.finalyear.databinding.FragmentInventoryListBinding;
import com.example.finalyear.ui.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class InventoryListFragment extends Fragment {

    private InventoryViewModel viewModel;
    private FragmentInventoryListBinding binding = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInventoryListBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.setBottomNavMenu(R.menu.inventory_bottom_menu, "inventory_bottom_menu");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        InventoryListAdapter adapter = new InventoryListAdapter(new InventoryListAdapter.InventoryDiff());

        LinearLayoutManager lm = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.inventoryRecyclerView.setLayoutManager(lm);
        binding.inventoryRecyclerView.setAdapter(adapter);
        binding.inventoryRecyclerView.setHasFixedSize(true);
        binding.inventoryRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext()
                , LinearLayoutManager.VERTICAL));

        adapter.onItemClick(str -> {

        });

        binding.addInventoryFab.setOnClickListener(v -> {
            NavDirections action = InventoryListFragmentDirections
                    .actionInventoryListFragmentToInventoryDialogFragment(null);
            Navigation.findNavController(view).navigate(action);
        });

        viewModel.getLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.pIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.pIndicator.setVisibility(View.GONE);
            }
        });

        viewModel.getInventory().observe(getViewLifecycleOwner(), list -> {
            if (list == null) return;
            adapter.submitList(list);
        });
    }

}