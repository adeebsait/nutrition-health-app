package com.example.finalyear.ui.home.presentation.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalyear.databinding.FragmentSlider1Binding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Slider1 extends Fragment {

    private Slider1ViewModel viewModel;
    private FragmentSlider1Binding binding = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(Slider1ViewModel.class);
        binding = FragmentSlider1Binding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(binding==null)return;
        SpannableString remainString = new SpannableString("\nRemaining");
        remainString.setSpan(new RelativeSizeSpan(0.6f),0,remainString.length(),0);
        viewModel.getRemainProto.observe(getViewLifecycleOwner(), s -> {
            if(s==null)return;
            binding.dietProgressView.setText(s);
            binding.dietProgressView.append(remainString);
        });
        binding.dietProgressView.setProgress(100,true);
    }
}