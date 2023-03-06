package com.example.finalyear.ui.home.presentation.dashboard;

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
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.finalyear.R;
import com.example.finalyear.databinding.FragmentDashboardScreenBinding;
import com.example.finalyear.ui.MainViewModel;

import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;
    private FragmentDashboardScreenBinding binding = null;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.setBottomNavMenu(R.menu.home_bottom_menu, "home_bottom_menu");
        binding = FragmentDashboardScreenBinding.inflate(inflater);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        List<Fragment> list = Collections.singletonList(new Slider1());
        SliderAdapter sliderAdapter = new SliderAdapter(this, list);

        binding.dashboardSlider.setAdapter(sliderAdapter);
        binding.sliderIndicator.attachTo(binding.dashboardSlider);
        binding.dashboardSlider.setOffscreenPageLimit(3);
        binding.dashboardSlider.setClipToPadding(false);
        binding.dashboardSlider.setClipChildren(false);
        binding.dashboardSlider.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(20));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        binding.dashboardSlider.setPageTransformer(transformer);


        binding.dashboardScanFoodTv.setOnClickListener(v -> {
            NavDirections action = DashboardFragmentDirections.actionGlobalToScanfragments();
            Navigation.findNavController(v).navigate(action);
        });

        binding.dashboardInventoryTv.setOnClickListener(v -> {
            NavDirections action = DashboardFragmentDirections.actionGlobalToInventory();
            Navigation.findNavController(v).navigate(action);
        });

        binding.dashboardRecipeGenTv.setOnClickListener(v -> {
            NavDirections action = DashboardFragmentDirections.actionGlobalToRecipe();
            Navigation.findNavController(v).navigate(action);

        });
        binding.dashboardActivityTv.setOnClickListener(v -> {
            NavDirections action = DashboardFragmentDirections.actionGlobalToActivityFragments();
            Navigation.findNavController(v).navigate(action);

        });
    }
}