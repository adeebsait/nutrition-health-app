package com.example.finalyear.ui.foodscanner.presentation;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.finalyear.R;
import com.example.finalyear.databinding.FragmentScannerBinding;
import com.example.finalyear.ui.MainViewModel;
import com.example.finalyear.ui.foodscanner.data.BarcodeData;
import com.example.finalyear.ui.inventory.data.InventoryItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ScannerFragment extends Fragment {

    private FragmentScannerBinding binding = null;
    private ScannerViewModel scannerViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.setBottomNavMenu(R.menu.scanner_bottom_menu, "scanner_bottom_menu",1);
        scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);

        binding = FragmentScannerBinding.inflate(inflater);
        return binding.getRoot();
    }

    private String lastText;
    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(@NonNull BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            lastText = result.getText();
            scannerViewModel.setTextAfterScan(lastText);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE,
                BarcodeFormat.CODE_39,BarcodeFormat.UPC_A,BarcodeFormat.EAN_13,BarcodeFormat.EAN_8);
        binding.barcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        binding.barcodeScannerView.decodeSingle(callback);


        scannerViewModel.scanLiveData.observe(getViewLifecycleOwner(), barcode -> {
            if (barcode == null) return;
            NavDirections action = ScannerFragmentDirections.actionGlobalScanResultFragment(barcode,null);
            Navigation.findNavController(view).navigate(action);
        });

      //  scannerViewModel.setTextAfterScan("034700000602");

    }
    @Override
    public void onResume() {
        super.onResume();
        binding.barcodeScannerView.resume();


    }

    @Override
    public void onPause() {
        super.onPause();
        binding.barcodeScannerView.pause();
    }


}