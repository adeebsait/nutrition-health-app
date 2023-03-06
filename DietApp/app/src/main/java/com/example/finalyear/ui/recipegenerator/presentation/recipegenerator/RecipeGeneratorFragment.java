package com.example.finalyear.ui.recipegenerator.presentation.recipegenerator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.finalyear.R;
import com.example.finalyear.common.utils.DietUtilily;
import com.example.finalyear.databinding.FragmentRecipeGeneratorBinding;
import com.example.finalyear.ui.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecipeGeneratorFragment extends Fragment {

    private FragmentRecipeGeneratorBinding binding = null;
    private RecipeGeneratorViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.setBottomNavMenu(R.menu.recipes_bottom_menu, "recipes_bottom_menu");
        viewModel = new ViewModelProvider(this).get(RecipeGeneratorViewModel.class);
        binding = FragmentRecipeGeneratorBinding.inflate(inflater);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        binding.recipeSearchViewEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (v.getText().length() > 1) {
                    viewModel.setQueryString(v.getText().toString().trim());
                    DietUtilily.HideKeyBoard(requireContext(), binding.getRoot());
                }
            }
            return true;
        });


        RecipeListAdapter adapter = new RecipeListAdapter();

        GridLayoutManager lm = new GridLayoutManager(requireContext(), 3);
        binding.recipeGenRecycler.setLayoutManager(lm);
        binding.recipeGenRecycler.setAdapter(adapter);

        adapter.onItemClick(recipeDetails -> {
            NavDirections action = RecipeGeneratorFragmentDirections
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
    public void onDestroy() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onDestroy();
    }
}