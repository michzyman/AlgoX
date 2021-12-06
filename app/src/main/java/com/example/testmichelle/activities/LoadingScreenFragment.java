package com.example.testmichelle.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.testmichelle.R;

public class LoadingScreenFragment extends Fragment {
    ImageView imgBigLogo;

    public LoadingScreenFragment() {
        // Required empty public constructor
    }

    public static LoadingScreenFragment newInstance(String param1, String param2) {
        LoadingScreenFragment fragment = new LoadingScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading_screen, container, false);
        imgBigLogo = view.findViewById(R.id.imgBigLogo);
        imgBigLogo.startAnimation(
                AnimationUtils.loadAnimation(getContext(), R.anim.rotate) );
        return view;
    }
}