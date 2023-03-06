package com.example.finalyear.ui.splash.presentation;

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
import androidx.navigation.ui.NavigationUI;

import com.example.finalyear.databinding.FragmentSplashBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding = null;

    private SplashViewModel splashViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        binding = FragmentSplashBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        splashViewModel.getUserIsLoggedIn()
                .observe(getViewLifecycleOwner(), isLogged -> {
                    NavDirections action = isLogged ?
                            SplashFragmentDirections.actionSplashFragmentToHomeFragments():
                            SplashFragmentDirections.actionSplashFragmentToAuthEntryFragment();
                    Navigation.findNavController(view).navigate(action);
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}