package com.example.finalyear.ui.foodscanner.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.finalyear.R;
import com.example.finalyear.common.utils.MyTextWatcher;
import com.example.finalyear.common.utils.Utility;
import com.example.finalyear.databinding.FragmentScanResultBinding;
import com.example.finalyear.ui.inventory.presentation.InventoryViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ScanResultFragment extends Fragment {

    private FragmentScanResultBinding binding = null;
    private ScanResultViewModel mViewModel;
    private InventoryViewModel inventoryViewModel;
    private MaterialButtonToggleGroup.OnButtonCheckedListener checkedListener;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        mViewModel = new ViewModelProvider(this).get(ScanResultViewModel.class);
        binding = FragmentScanResultBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.scanResultName.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (mViewModel.retrievedItem == null) return;
            mViewModel.retrievedItem.setName(s.toString());
        });

        binding.scanResultExpires.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (mViewModel.retrievedItem == null) return;
            mViewModel.retrievedItem.setExpiry(Utility.dateToMillis(s.toString()));
        });
        binding.quantText.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (mViewModel.retrievedItem == null && s.length() < 1) return;
            mViewModel.retrievedItem.setQuantity(Integer.valueOf(s.toString()));
        });
        binding.quantityUnitAutoText.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (mViewModel.retrievedItem == null) return;
            mViewModel.retrievedItem.setQuantityUnit(s.toString());
        });

        binding.scanResultSugar.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (mViewModel.retrievedItem == null) return;
            mViewModel.retrievedItem.setSugar(s.toString());
        });


        binding.minusQuantityBtn.setOnClickListener(v -> {
            try {
                Integer i = Integer.parseInt(binding.quantText.getText().toString().trim());
                if (i > 0) {
                    i--;
                    binding.quantText.setText(String.valueOf(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.plusQuantityBtn.setOnClickListener(v -> {
            try {
                Integer i = Integer.parseInt(binding.quantText.getText().toString().trim());
                i++;
                binding.quantText.setText(String.valueOf(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // ----------------------------- Any Success Event Trigger for Snack bar
        mViewModel.universalSuccessEvent.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
        });
        // ----------------------------- Any Error Event Trigger for Snack bar
        mViewModel.scanErrorState.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Exit", v -> {
                        back(view);
                    }).show();
        });

        // views
        binding.quantityUnitAutoText.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                requireContext().getResources().getStringArray(R.array._units)));

        // ----------------------------- Add To Save List (Firebase)

        checkedListener = (group, checkedId, isChecked) -> {
            if (isChecked) {
                mViewModel.saveScanItem(mViewModel.retrievedItem);
                return;
            }
            if (mViewModel.getScanSaveState.getValue() == null) return;
            mViewModel.removeScanSavedItem(mViewModel.getScanSaveState.getValue());
        };

        binding.scanResultFavToggle.addOnButtonCheckedListener(checkedListener);


        // ----------------------------- Add To Inventory (Firebase)
        inventoryViewModel.getSaveLoadingSate.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.scanResultAddtoInventory.startLoading();
            } else {
                binding.scanResultAddtoInventory.stopLoading();
            }
        });
        inventoryViewModel.getInventorySubmit.observe(getViewLifecycleOwner(), isLoading -> {
            Toast.makeText(requireContext(), getString(R.string.inventory_added_success), Toast.LENGTH_SHORT).show();
            back(view);
        });

        binding.scanResultAddtoInventory.setOnClickListener(v -> {
            if (mViewModel.retrievedItem == null) return;
            inventoryViewModel.saveInventory(mViewModel.retrievedItem);
        });
        inventoryViewModel.saveErrorState.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Exit", v -> {
                        back(view);
                    }).show();
        });

        // ----------------------------- Retrieving Food From Barcode
        // ----------------------------- Save to History (RoomDb) Behind

        mViewModel.getLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            int visibility = isLoading ? View.VISIBLE : View.GONE;
            binding.pIndicator.setVisibility(visibility);
        });
        mViewModel.getSuccessState.observe(getViewLifecycleOwner(), inventoryItem -> {
            mViewModel.retrievedItem = inventoryItem;
            binding.scanResultName.setText(inventoryItem.getName());
            binding.quantText.setText(String.valueOf(inventoryItem.getQuantity()));
            binding.quantityUnitAutoText.setText(inventoryItem.getQuantityUnit());
            binding.scanResultSugar.setText(inventoryItem.getSugar());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.scanResultFavToggle.removeOnButtonCheckedListener(checkedListener);
    }

    private void back(View view) {
        Navigation.findNavController(view).popBackStack();
    }
}