package com.example.finalyear.ui.activityuser.presentation;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalyear.R;
import com.example.finalyear.databinding.FragmentSummaryBinding;
import com.example.finalyear.ui.MainViewModel;
import com.example.finalyear.ui.activityuser.presentation.StepCounterService.LocalBinder;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SummaryFragment extends Fragment {
    private FragmentSummaryBinding binding = null;
    private SummaryViewModel summaryViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.setBottomNavMenu(R.menu.activity_bottom_menu, "activity_bottom_menu");
        summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        binding = FragmentSummaryBinding.inflate(inflater);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        summaryViewModel.getStepGoal.observe(getViewLifecycleOwner(), stepGoal -> {
            binding.prView.setMax(stepGoal);
            binding.prView.setSuffix("Step Goal: " + stepGoal);
        });

        summaryViewModel.getStepsCountDate.observe(getViewLifecycleOwner(), dateString -> {
            // dd MMM,yyyy
            binding.prView.setPrefix(dateString);
        });

        summaryViewModel.getCountOfSteps.observe(getViewLifecycleOwner(), l -> {
            binding.prView.setProgressValue(l);
            binding.prView.setText("" + l);
        });

        summaryViewModel.distanceForTotalStep.observe(getViewLifecycleOwner(), dist -> {
            binding.distTv.setText(dist);
        });

        summaryViewModel.getCalorieLoss.observe(getViewLifecycleOwner(), s -> {
            binding.caloriesLossTv.setText(s);
        });
        summaryViewModel.duration.observe(getViewLifecycleOwner(), s -> {
            binding.durationTv.setText(s);
        });

        binding.counterStartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StepCounterService.class);
            intent.putExtra(StepCounterService.ACTION_KEY,StepCounterService.BTN_START_STOP);
            requireActivity().startService(intent);
        });

        summaryViewModel.getResetLoadingState.observe(getViewLifecycleOwner(), isLoading -> {
            binding.counterReset.isLoading(isLoading);
        });

        summaryViewModel.getResetSuccessState.observe(getViewLifecycleOwner(), msg -> {
            Snackbar.make(binding.getRoot(),msg,Snackbar.LENGTH_LONG).show();
        });

        binding.counterReset.setOnClickListener(v -> {
            summaryViewModel.resetCounter(summaryViewModel.getCalorieLoss.getValue());
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(requireContext(), StepCounterService.class);
        requireActivity().startService(intent);
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().unbindService(connection);
        mBound = false;
        super.onStop();
    }

    private StepCounterService mService = null;
    private boolean mBound = false;
    private final ServiceConnection connection = new ServiceConnection() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.serviceRunning.observe(getViewLifecycleOwner(), running -> {
                if (running) {
                    binding.counterStartBtn.setText("Stop");
                    binding.prView.playPr();
                    binding.counterReset.setEnabled(false);
                } else {
                    binding.counterStartBtn.setText("Start");
                    binding.prView.pausePr();
                    binding.counterReset.setEnabled(true);
                }
            });
            mService.completeEvent.observe(getViewLifecycleOwner(),msg->{
                Snackbar.make(binding.getRoot(),msg,Snackbar.LENGTH_LONG).show();
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}