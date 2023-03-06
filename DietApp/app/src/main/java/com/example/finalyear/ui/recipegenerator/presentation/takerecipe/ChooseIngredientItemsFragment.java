package com.example.finalyear.ui.recipegenerator.presentation.takerecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalyear.databinding.FragmentChooseIngredientItemsBinding;
import com.example.finalyear.ui.inventory.presentation.InventoryListAdapter;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ChooseIngredientItemsFragment extends Fragment {

    private FragmentChooseIngredientItemsBinding binding = null;
    private ChooseIngredViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChooseIngredientItemsBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(ChooseIngredViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;
        SelectableInventoryListAdapter adapter = new SelectableInventoryListAdapter(
                new SelectableInventoryListAdapter.InventoryDiff());

        LinearLayoutManager lm = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.chooseIngredRecycler.setLayoutManager(lm);
        binding.chooseIngredRecycler.setAdapter(adapter);
        binding.chooseIngredRecycler.setHasFixedSize(true);
        binding.chooseIngredRecycler.addItemDecoration(new DividerItemDecoration(requireContext()
                , LinearLayoutManager.VERTICAL));

        viewModel.inventoryError.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(),msg,Snackbar.LENGTH_SHORT).show();
        });
        viewModel.getInventoryLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.pIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.pIndicator.setVisibility(View.GONE);
            }
        });
        viewModel.getInventoryItems.observe(getViewLifecycleOwner(), list -> {
            if (list == null) return;
            adapter.submitList(list);
        });

        //--------------------------- Saving To Room Taken Recipes

        viewModel.saveTakenLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
                binding.cookRecipeButton.isLoading(isLoading);
        });

        viewModel.saveTakenErrorState.observe(getViewLifecycleOwner(), msg -> {
                Snackbar.make(binding.getRoot(),msg,Snackbar.LENGTH_SHORT).show();
        });

        viewModel.saveTakenSuccessState.observe(getViewLifecycleOwner(), msg -> {
                NavDirections action = ChooseIngredientItemsFragmentDirections
                    .actionGlobalRecipeTakeFragment();
            Navigation.findNavController(view).navigate(action);
        });

        binding.cookRecipeButton.setOnClickListener(v->{
            if(adapter.getSelectedList().size()<1){
                viewModel.inventoryError.setValue("No Item Selected");
                return;
            }
            viewModel.reduceItemsAndUpdateData(
                    adapter.getSelectedList(),
                    (int) viewModel.getRecipeDetails().getCalories());
        });
    }
}