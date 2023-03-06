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

import com.example.finalyear.common.utils.MyTextWatcher;
import com.example.finalyear.databinding.FragmentSignUpOptionsBinding;
import com.example.finalyear.ui.MainViewModel;
import com.example.finalyear.ui.authentication.domain.LoginNavigateTo;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpOptionsFragment extends Fragment {

    private FragmentSignUpOptionsBinding binding = null;
    private LoginViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding = FragmentSignUpOptionsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;
        binding.signupOptContinueBtn.setOnClickListener(v -> {
            NavDirections action = SignUpOptionsFragmentDirections
                    .actionSignUpOptionsFragmentToSignupScreenFragment();
            Navigation.findNavController(view).navigate(action);
        });

        mViewModel.isErrorMessage.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
        });

        binding.signupOptGlBtn.setOnClickListener(v -> {
            AuthUI authUi = AuthUI.getInstance();
            Intent intent = authUi.createSignInIntentBuilder()
                    .setAvailableProviders(Collections.singletonList(
                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                    .setDefaultProvider(new AuthUI.IdpConfig.GoogleBuilder().build())
                    .build();
            googleSignupLauncher.launch(intent);
        });
        mViewModel.getSuccessLoginState
                .observe(getViewLifecycleOwner(), loginNavigate -> {
                    switch (loginNavigate) {
                        case HOME:
                            Snackbar.make(binding.getRoot(),"Id Exist, Try With Login",Snackbar.LENGTH_SHORT).show();
                            break;
                        case SIGNUP_INFORMATION:
                            NavDirections signupInfo = SignUpOptionsFragmentDirections
                                    .actionSignUpOptionsFragmentToInfoFormScreenFragment();
                            Navigation.findNavController(view).navigate(signupInfo);
                            break;
                    }
                });
    }

    ActivityResultLauncher<Intent> googleSignupLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    mViewModel.submitForLoginWithGoogle();
                    return;
                }
                mViewModel.isErrorMessage.setValue("Failed Try Later");
            });
}