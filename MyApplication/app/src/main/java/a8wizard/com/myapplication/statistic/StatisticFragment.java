package a8wizard.com.myapplication.statistic;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.databinding.FragmentStatisticBinding;

public class StatisticFragment extends Fragment {

    public static final String TAG = "StatisticFragment";
    FragmentStatisticBinding binding;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistic, container, false);
            return binding.getRoot();
        }
    }
