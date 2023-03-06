package com.example.finalyear.ui.authentication.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalyear.databinding.FragmentAuthEntryBinding;
import com.example.finalyear.ui.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthEntryFragment extends Fragment {


    private FragmentAuthEntryBinding binding = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthEntryBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding != null) {
            binding.entryLoginBtn.setOnClickListener(v -> {
                NavDirections action = AuthEntryFragmentDirections.actionAuthEntryFragmentToLoginScreenFragment();
                Navigation.findNavController(v).navigate(action);
            });
            binding.entrySignUpBtn.setOnClickListener(v -> {
                NavDirections action = AuthEntryFragmentDirections.actionAuthEntryFragmentToSignUpOptionsFragment();
                Navigation.findNavController(v).navigate(action);
            });
        }
    }
}