package com.example.finalyear.ui.inventory.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalyear.R;
import com.example.finalyear.common.utils.Utility;
import com.example.finalyear.databinding.DialogInventoryBinding;
import com.example.finalyear.ui.inventory.data.InventoryItem;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InventoryDialogFragment extends DialogFragment {

    private DialogInventoryBinding binding = null;

    private InventoryViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        binding = DialogInventoryBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        if (getArguments() != null) {
            viewModel.newItem = getArguments().getParcelable("itemdata");
        }

        if (viewModel.newItem != null && viewModel.newItem.getExpiry() != null) {
            try {
                binding.dialogName.setText(viewModel.newItem.getName());
                binding.dialogExpiresOn.setText(String.valueOf(viewModel.newItem.getExpiry()));
                binding.quantText.setText(String.valueOf(viewModel.newItem.getQuantity()));
                binding.quantityUnitAutoText.setText(viewModel.newItem.getQuantityUnit());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

        binding.exitDialog.setOnClickListener(v -> {
            dismiss();
        });

        viewModel.getInventorySubmit.observe(getViewLifecycleOwner(), inventoryId -> {
            if (inventoryId == null) return;
            binding.progressIndicator.setVisibility(View.INVISIBLE);
            dismiss();
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
        });

        binding.addToInventoryBtn.setOnClickListener(v -> {
            binding.progressIndicator.setVisibility(View.VISIBLE);
            try {
                InventoryItem item = new InventoryItem("", Objects.requireNonNull(binding.dialogName.getText()).toString().trim(),
                        Integer.parseInt(binding.quantText.getText().toString()), binding.quantityUnitAutoText.getText().toString(),
                        binding.dialogSugarAmount.getText().toString(),
                        Utility.dateToMillis(binding.dialogExpiresOn.getText().toString().trim()));
                viewModel.saveInventory(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
