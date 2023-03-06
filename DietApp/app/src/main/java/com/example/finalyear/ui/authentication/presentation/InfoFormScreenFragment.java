package com.example.finalyear.ui.authentication.presentation;

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

import com.example.finalyear.common.utils.DietUtilily;
import com.example.finalyear.common.utils.MyTextWatcher;
import com.example.finalyear.databinding.FragmentSignUpFormScreenBinding;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InfoFormScreenFragment extends Fragment {

    private InfoFormViewModel mViewModel;
    private FragmentSignUpFormScreenBinding binding = null;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpFormScreenBinding.inflate(inflater);

        mViewModel = new ViewModelProvider(this).get(InfoFormViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        // Text Watcher
        binding.firstNameInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.firstName = s.toString();
        });

        binding.lastNameInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.lastName = s.toString();
        });

        binding.dobInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.dob = s.toString();
        });

        binding.genderInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
           mViewModel.gender = s.toString();
        });
        binding.heightFtInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (s.length() < 1) return;
            mViewModel.heightFt = (Integer.valueOf(s.toString()));
        });
        binding.heightInchInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (s.length() < 1) return;
            mViewModel.heightIn = (Integer.valueOf(s.toString()));
        });

        binding.weightKiloInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (s.length() < 1) return;
            mViewModel.weightKilo = (Integer.valueOf(s.toString()));
        });
        binding.weightGmInpdutEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            if (s.length() < 1) return;
            mViewModel.weightGm = (Integer.valueOf(s.toString()));
        });

        mViewModel.getLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading)
                binding.signupSubmitBtn.startLoading();
            else
                binding.signupSubmitBtn.stopLoading();
        });
        mViewModel.getSignupState.observe(getViewLifecycleOwner(), s -> {
            NavDirections action = InfoFormScreenFragmentDirections
                    .actionSignUpFormScreenFragmentToGoalsScreenFragment();
            Navigation.findNavController(view).navigate(action);
        });
        mViewModel.errorState.observe(getViewLifecycleOwner(),msg->{
            Snackbar.make(binding.getRoot(),msg,Snackbar.LENGTH_SHORT).show();
        });
        binding.signupSubmitBtn.setOnClickListener(v -> {
            DietUtilily.HideKeyBoard(requireContext(),binding.getRoot());
            mViewModel.signUp();
        });
    }

}
