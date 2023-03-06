package com.example.finalyear.ui.authentication.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.finalyear.common.utils.DietUtilily;
import com.example.finalyear.common.utils.MyTextWatcher;
import com.example.finalyear.databinding.FragmentLoginScreenBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginScreenFragment extends Fragment {
    private LoginViewModel mViewModel;
    private FragmentLoginScreenBinding binding = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginScreenBinding.inflate(inflater);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;


        binding.loginEmailInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.setEmail(s.toString());
        });
        binding.loginInputPassEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.setPassword(s.toString());
        });
        binding.loginSubmitBtn.setOnClickListener(v -> {
            DietUtilily.HideKeyBoard(requireContext(), binding.getRoot());
            mViewModel.submitForLogin(mViewModel.getEmail(), mViewModel.getPassword());
        });
        binding.loginOptGlBtn.setOnClickListener(v -> {
            AuthUI authUi = AuthUI.getInstance();
            Intent intent = authUi.createSignInIntentBuilder()
                    .setAvailableProviders(Collections.singletonList(
                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                    .setDefaultProvider(new AuthUI.IdpConfig.GoogleBuilder().build())
                    .build();
            loginLauncher.launch(intent);
        });
        binding.loginOptFbBtn.setOnClickListener(v -> {
            /*AuthUI authUi = AuthUI.getInstance();
            Intent intent = authUi.createSignInIntentBuilder()
                    .setAvailableProviders(Collections.singletonList(
                            new AuthUI.IdpConfig.FacebookBuilder().build()))
                    .setDefaultProvider(new AuthUI.IdpConfig.FacebookBuilder().build())
                    .build();
            loginLauncher.launch(intent);*/
        });


        mViewModel.getLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading && !binding.loginSubmitBtn.isLoading())
                binding.loginSubmitBtn.startLoading();
            else
                binding.loginSubmitBtn.stopLoading();
        });
        mViewModel.isErrorMessage.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
        });

        mViewModel.getSuccessLoginState
                .observe(getViewLifecycleOwner(), loginNavigate -> {
                    switch (loginNavigate) {
                        case HOME:
                            NavDirections home = LoginScreenFragmentDirections
                                    .actionLoginScreenFragmentToHomeFragments();
                            Navigation.findNavController(view).navigate(home);
                            break;
                        case SIGNUP_INFORMATION:
                            NavDirections signupInfo = LoginScreenFragmentDirections
                                    .actionLoginScreenFragmentToSignUpFormScreenFragment();
                            Navigation.findNavController(view).navigate(signupInfo);
                            break;
                        case GOALS:
                            NavDirections goals = LoginScreenFragmentDirections
                                    .actionLoginScreenFragmentToGoalsScreenFragment();
                            Navigation.findNavController(view).navigate(goals);
                            break;
                    }
                });
    }

    ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    mViewModel.submitForLoginWithGoogle();
                    return;
                }
                mViewModel.isErrorMessage.setValue("Failed Try Later");
            });
}