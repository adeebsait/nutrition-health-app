package com.example.finalyear.ui.home.presentation.settings;

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

import com.example.finalyear.databinding.FragmentSettingBinding;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding = null;

    private SettingsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;


        viewModel.getErrorState.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG).show();
        });

        viewModel.getLogoutLoadingState.observe(getViewLifecycleOwner(), aBoolean -> {
            binding.logOutBtn.isLoading(aBoolean);
        });

        viewModel.getLogoutSuccessState.observe(getViewLifecycleOwner(), aBoolean -> {
            NavDirections action = SettingFragmentDirections.actionSettingFragmentToAuthEntryFragment();
            Navigation.findNavController(view).navigate(action);
        });
        binding.stepGoalTv.setOnClickListener(v -> {
            NavDirections action = SettingFragmentDirections.actionSettingFragmentToStepGoalDialog();
            Navigation.findNavController(view).navigate(action);
        });
        binding.logOutBtn.setOnClickListener(v -> {
            viewModel.submitLogout();
        });

    }
}