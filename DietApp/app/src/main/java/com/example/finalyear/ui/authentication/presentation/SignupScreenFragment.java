package com.example.finalyear.ui.authentication.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.finalyear.common.utils.DietUtilily;
import com.example.finalyear.common.utils.MyTextWatcher;
import com.example.finalyear.databinding.FragmentSignupScreenBinding;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignupScreenFragment extends Fragment {
    private SignUpScreenViewModel mViewModel;
    private FragmentSignupScreenBinding binding = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupScreenBinding.inflate(inflater);
        mViewModel = new ViewModelProvider(this).get(SignUpScreenViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;


        binding.signupEmailInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.setEmail(s.toString());
        });
        binding.signupInputPassEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.setPassword(s.toString());
        });
        binding.signupContinueBtn.setOnClickListener(v -> {
            DietUtilily.HideKeyBoard(requireContext(), binding.getRoot());
            mViewModel.submitSignup();
        });

        mViewModel.errorEvent.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(),msg,Snackbar.LENGTH_SHORT).show();
        });
        mViewModel.getLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.signupContinueBtn.startLoading();
            } else {
                binding.signupContinueBtn.stopLoading();
            }
        });
        mViewModel.getSuccessEvent.observe(getViewLifecycleOwner(), aBoolean -> {
            NavDirections action = SignupScreenFragmentDirections
                    .actionSignupScreenFragmentToInfoFormScreenFragment();
            Navigation.findNavController(view).navigate(action);
        });

    }
}