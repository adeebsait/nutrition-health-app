package com.example.finalyear.ui.recipegenerator.presentation.recipedetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.finalyear.databinding.FragmentRecipeDetailBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecipeDetailFragment extends BottomSheetDialogFragment {

    private FragmentRecipeDetailBinding binding = null;
    private RecipeDetailViewModel viewModel;
    private MaterialButtonToggleGroup.OnButtonCheckedListener toggleListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RecipeDetailViewModel.class);
        binding = FragmentRecipeDetailBinding.inflate(inflater);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // --------------------------------------------- Recipe Exist or Not

        viewModel.itemExistEvent.observe(getViewLifecycleOwner(), isExist -> {
            if (isExist)
                binding.recipeSaveToogle.check(binding.saveRecipeBtn.getId());
            else
                binding.recipeSaveToogle.uncheck(binding.saveRecipeBtn.getId());
        });
        // -------------------------------------- Recipes Text Set
        binding.recipeHeadline.setText(viewModel.getRecipeDetails().getRecipeName());

        // --------------------------------------------- Recipe Ingredient List
        RecipeDetailRecAdapter recipeDetailRecAdapter =
                new RecipeDetailRecAdapter(new RecipeDetailRecAdapter.DiffCallBack());
        binding.recipeDetailRecycler.setAdapter(recipeDetailRecAdapter);

        binding.recipeDetailRecycler.setLayoutManager(
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );

        Glide.with(view)
                .load(viewModel.getRecipeDetails().getImages().getrRegular().getUrl())
                .into(binding.sheetImageview);

        String calorie = String.valueOf(Math.floor(viewModel.getRecipeDetails().getCalories()));

        binding.sheetCalorieText.setText(calorie + " Cal");

        recipeDetailRecAdapter.submitList(viewModel.getRecipeDetails().getIngredients());

        //----------------------------------------- Save Room Toggle

        viewModel.getSaveLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.recipeTakenBtn.startLoading();
            } else {
                binding.recipeTakenBtn.stopLoading();
            }
        });

        viewModel.savingErrorState.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
        });

        viewModel.saveOrRemoveSuccessEvent.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
        });

        toggleListener = (group, checkedId, isChecked) -> {
            if (!binding.saveRecipeBtn.isPressed()) return;
            if (isChecked) {
                viewModel.saveRecipeToRoom(viewModel.getRecipeDetails());
                return;
            }
            viewModel.removeFromRoom(viewModel.getRecipeDetails());
        };

        binding.recipeSaveToogle.addOnButtonCheckedListener(toggleListener);


        binding.recipeTakenBtn.setOnClickListener(v -> {
            NavDirections action = RecipeDetailFragmentDirections.actionGlobalChooseIngredientItemsFragment(
                    viewModel.getRecipeDetails()
            );
            Navigation.findNavController(view).navigate(action);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.recipeSaveToogle.removeOnButtonCheckedListener(toggleListener);
    }
}
