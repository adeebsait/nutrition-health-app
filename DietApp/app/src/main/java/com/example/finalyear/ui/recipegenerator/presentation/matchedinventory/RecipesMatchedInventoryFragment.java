package com.example.finalyear.ui.recipegenerator.presentation.matchedinventory;

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
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.finalyear.databinding.FragmentRecipesMatchedInventoryBinding;
import com.example.finalyear.ui.recipegenerator.presentation.recipegenerator.RecipeListAdapter;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecipesMatchedInventoryFragment extends Fragment {

    private FragmentRecipesMatchedInventoryBinding binding = null;
    private RecipeMatchedInventoryViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipesMatchedInventoryBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(RecipeMatchedInventoryViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (binding == null) return;

        RecipeListAdapter adapter = new RecipeListAdapter();

        GridLayoutManager lm = new GridLayoutManager(requireContext(), 3);
        binding.recipeGenRecycler.setLayoutManager(lm);
        binding.recipeGenRecycler.setAdapter(adapter);

        adapter.onItemClick(recipeDetails -> {
            NavDirections action = RecipesMatchedInventoryFragmentDirections
                    .actionGlobalRecipeDetailFragment(recipeDetails);
            Navigation.findNavController(view).navigate(action);
        });


        viewModel.getErrorState.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
        });

        viewModel.getLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.pIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.pIndicator.setVisibility(View.GONE);
            }
        });

        viewModel.getRecipesState.observe(getViewLifecycleOwner(), recipePojo -> {
            if (recipePojo == null || recipePojo.getRecipeList() == null) return;
            adapter.submitList(recipePojo.getRecipeList());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}