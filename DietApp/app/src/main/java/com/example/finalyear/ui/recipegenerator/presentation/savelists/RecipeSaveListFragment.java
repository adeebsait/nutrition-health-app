package com.example.finalyear.ui.recipegenerator.presentation.savelists;

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
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.finalyear.R;
import com.example.finalyear.databinding.FragmentRecipeSavedBinding;
import com.example.finalyear.ui.MainViewModel;
import com.example.finalyear.ui.recipegenerator.presentation.recipedetails.RecipeDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecipeSaveListFragment extends Fragment {

    private FragmentRecipeSavedBinding binding = null;

    private RecipeSaveListViewModel recipeDetailViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recipeDetailViewModel = new ViewModelProvider(this).get(RecipeSaveListViewModel.class);
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.setBottomNavMenu(R.menu.recipes_bottom_menu, "recipes_bottom_menu");
        binding = FragmentRecipeSavedBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        RecipeSavedListAdapter adapter = new RecipeSavedListAdapter();

        GridLayoutManager lm = new GridLayoutManager(requireContext(), 3);
        binding.recipeSavedRecycler.setLayoutManager(lm);
        binding.recipeSavedRecycler.setAdapter(adapter);

        adapter.onItemClick((recipeDetails, v) -> {
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(v,"img")
                    .build();
            NavDirections action = RecipeSaveListFragmentDirections
                    .actionGlobalRecipeDetailFragment(recipeDetails);
            Navigation.findNavController(view).navigate(action,extras);
        });

        recipeDetailViewModel.getRetrieveLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.pIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.pIndicator.setVisibility(View.GONE);
            }
        });
        recipeDetailViewModel.getSavedRecipes().observe(getViewLifecycleOwner(), recipeEntityList -> {
            if (recipeEntityList == null) return;
            adapter.submitList(recipeEntityList);
        });
    }
}