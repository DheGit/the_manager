package com.infinitydheer.themanager.presentation.view.fragment.logger;

import android.os.Bundle;
import com.infinitydheer.themanager.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;

public class LoggerOverviewFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_logger_overview,container,false);
    }
}
