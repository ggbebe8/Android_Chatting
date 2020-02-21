package com.chops.android_chatting.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chops.android_chatting.R;

public class HomeFragment extends Fragment {

    String mstrEmail = "";

    public HomeFragment(String p_strEmail)
    {
        mstrEmail = p_strEmail;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvEmail = (TextView)v.findViewById(R.id.tvEmail);
        tvEmail.setText(mstrEmail);

        return v;
    }
}