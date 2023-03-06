package com.example.finalyear.ui.recipegenerator.presentation.takerecipe;

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

import com.example.finalyear.databinding.FragmentRecipeTakeBinding;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecipeTakeFragment extends Fragment {

    private FragmentRecipeTakeBinding binding = null;
    private RecipeTakeViewModel recipeTakeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeTakeBinding.inflate(inflater);
        recipeTakeViewModel = new ViewModelProvider(this).get(RecipeTakeViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(binding==null)return;
        TakenRecipeListAdapter takenRecipeListAdapter = new TakenRecipeListAdapter(new TakenRecipeListAdapter.InventoryDiff());

        LinearLayoutManager lm = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.takenRecipeRecycler.setLayoutManager(lm);
        binding.takenRecipeRecycler.setAdapter(takenRecipeListAdapter);
        binding.takenRecipeRecycler.setHasFixedSize(true);
        binding.takenRecipeRecycler.addItemDecoration(new DividerItemDecoration(requireContext()
                , LinearLayoutManager.VERTICAL));

        recipeTakeViewModel.takenListError.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(),msg,Snackbar.LENGTH_SHORT).show();
        });
        recipeTakeViewModel.getTakenListRetrieveSuccess.observe(getViewLifecycleOwner(), list -> {
            if (list == null) return;
            takenRecipeListAdapter.submitList(list);
        });
    }
}