package a8wizard.com.myapplication.transactions;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.databinding.FragmentTransactionAddBinding;

public class AddTransactionFragment extends Fragment implements View.OnClickListener {

    FragmentTransactionAddBinding binding;
    public static final String TAG = "AddTransactionFragment";
    public static TextView datePickerTextView;
    public static TextView timePickerTextView;

    private SQLHelper helper;
    private boolean value_text;
    private boolean value_number;

    private boolean null_ePrice;
    private boolean null_eDesc;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_add, container, false);

        setupButton();
        defineTextView();
        setupTextView();
        helper = new SQLHelper(getActivity());
        datePickerTextView.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        return binding.getRoot();
    }

    private void defineTextView() {
        datePickerTextView = binding.transactionDatePickerTextView;
        timePickerTextView = binding.transactionTimePickerTextView;
    }


    private void setupButton() {
        binding.addNewTransaction.setOnClickListener(this);
        binding.addNewTransaction.setMinHeight(70);
    }

    private void setupTextView() {
        datePickerTextView.setOnClickListener(this);
        timePickerTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_transaction:
                Toast.makeText(getActivity(), "Man", Toast.LENGTH_SHORT).show();
                break;
            case R.id.transaction_date_picker_text_view:
                Toast.makeText(getActivity(), "Man", Toast.LENGTH_SHORT).show();
                break;
            case R.id.transaction_time_picker_text_view:
                Toast.makeText(getActivity(), "Man", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
