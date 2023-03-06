package com.example.finalyear.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.TransitionManager;

import com.example.finalyear.R;
import com.example.finalyear.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.permissionx.guolindev.PermissionX;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding = null;
    private NavController navController;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION)
                .request((allGranted, grantedList, deniedList) -> {

                });


        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfig = new AppBarConfiguration
                .Builder(R.id.homeFragments)
                .build();


        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfig);
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);


        mainViewModel.getNavMenu.observe(this, bottomMenuItem -> {
            if (bottomMenuItem == null) return;
            binding.bottomNavView.getMenu().clear();
            binding.bottomNavView.inflateMenu(bottomMenuItem.getMenu());
            if (bottomMenuItem.getCheckedPosition() != 0)
                binding.bottomNavView.getMenu().getItem(bottomMenuItem.getCheckedPosition()).setChecked(true);
            TransitionManager.endTransitions((NavigationBarMenuView) binding.bottomNavView.getChildAt(0));
        });


        // --------------
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            switch (navDestination.getId()) {
                case R.id.splashFragment:
                case R.id.authEntryFragment:
                case R.id.loginScreenFragment:
                case R.id.signupScreenFragment:
                case R.id.signUpOptionsFragment:
                case R.id.goalsScreenFragment:
                    if (binding.bottomAppBar.getVisibility() != View.GONE)
                        binding.bottomAppBar.setVisibility(View.GONE);
                    if (binding.toolbarAppBar.getVisibility() != View.GONE)
                        binding.toolbarAppBar.setVisibility(View.GONE);
                    break;
                default:
                    if (binding.bottomAppBar.getVisibility() != View.VISIBLE)
                        binding.bottomAppBar.setVisibility(View.VISIBLE);
                    if (binding.toolbarAppBar.getVisibility() != View.VISIBLE)
                        binding.toolbarAppBar.setVisibility(View.VISIBLE);
                    break;
            }
            switch (navDestination.getId()) {
                case R.id.infoFormScreenFragment:
                case R.id.scanResultFragment:
                    if (binding.bottomAppBar.getVisibility() != View.GONE)
                        binding.bottomAppBar.setVisibility(View.GONE);
                    break;
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp()
                || super.onSupportNavigateUp();
    }
}