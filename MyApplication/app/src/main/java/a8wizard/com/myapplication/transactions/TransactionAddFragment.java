package a8wizard.com.myapplication.transactions;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.databinding.FragmentTransactionAddBinding;

public class TransactionAddFragment extends Fragment {

    FragmentTransactionAddBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_add, container, false);
        return binding.getRoot();
    }
}
