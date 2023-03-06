package com.example.finalyear.ui.additionalinfo.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.finalyear.common.utils.MyTextWatcher;
import com.example.finalyear.databinding.FragmentGoalsScreenBinding;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GoalsScreenFragment extends Fragment {

    private GoalsScreenViewModel mViewModel;
    private FragmentGoalsScreenBinding binding = null;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGoalsScreenBinding.inflate(inflater);
        mViewModel = new ViewModelProvider(this).get(GoalsScreenViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        binding.goalInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.setGoalText(s.toString());
        });
        binding.baslineActInputEdt.addTextChangedListener((MyTextWatcher) (s, start, before, count) -> {
            mViewModel.setBaselineText(s.toString());
        });

        mViewModel.getLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.goalsSubmitBtn.startLoading();
            } else {
                binding.goalsSubmitBtn.stopLoading();
            }
        });
        mViewModel.errorState.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
        });
        mViewModel.getSuccessState.observe(getViewLifecycleOwner(), uid -> {
            NavDirections action = GoalsScreenFragmentDirections.actionGoalsScreenFragmentToHomeFragments();
            Navigation.findNavController(view).navigate(action);
        });
        binding.goalsSubmitBtn.setOnClickListener(v -> {
            mViewModel.addGoalData(mViewModel.getGoalText(), mViewModel.getBaselineText());
        });
    }
}