package com.example.finalyear.ui.home.presentation.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.finalyear.databinding.FragmentFeedBinding;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FeedFragment extends Fragment {

    private FragmentFeedBinding binding = null;

        private FeedViewModel feedViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        binding = FragmentFeedBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding == null) return;

        FeedAdapter feedAdapter = new FeedAdapter();

        binding.recyclerView.setAdapter(feedAdapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false
                )
        );
        feedViewModel.getFeedItems(loadJSONFromAsset());
        feedViewModel.feedItems.observe(getViewLifecycleOwner(), feedAdapter::submitList);
    }
    public String loadJSONFromAsset() {
        String json = "";
        try {
            InputStream is = requireActivity().getAssets().open("feeddata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}