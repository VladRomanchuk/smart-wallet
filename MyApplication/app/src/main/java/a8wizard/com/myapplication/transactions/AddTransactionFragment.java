package a8wizard.com.myapplication.transactions;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.databinding.FragmentTransactionAddBinding;

public class AddTransactionFragment extends Fragment implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    FragmentTransactionAddBinding binding;

    public static final String TAG = "AddTransactionFragment";

    private Calendar now;
    private SQLHelper helper;
    private TimePickerDialog timePickerDialog;

    public static TextView transactionDatePickerTextView;
    public static TextView transactionTimePickerTextView;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_add, container, false);

        setupButton();
        setupLayouts();
        initTextView();
        helper = new SQLHelper(getActivity());
        now = Calendar.getInstance();

        return binding.getRoot();
    }

    private void initTextView() {
        transactionDatePickerTextView = binding.transactionDatePickerTextView;
        transactionTimePickerTextView = binding.transactionTimePickerTextView;
    }

    private void setupButton() {
        binding.addNewTransaction.setOnClickListener(this);
    }

    private void setupLayouts() {
        binding.datePickerLayout.setOnClickListener(this);
        binding.timePickerLayout.setOnClickListener(this);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = hourString + ":" + minuteString;
        binding.transactionTimePickerTextView.setText(time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_transaction:
                if (!isEditTextEmpty(binding.transactionPriceInputText, binding.transactionDescriptionInputText)) {
                    addTransaction();
                    binding.transactionPriceInputText.setText("");
                    binding.transactionDescriptionInputText.setText("");
                    binding.transactionPriceInputText.requestFocus();
                }else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.date_picker_layout:
                showDatePicker();
                break;
            case R.id.time_picker_layout:
                showTimePicker();
                break;
        }
    }

    private void addTransaction() {
        Date date = new Date();
        TransactionItem transactionItem = new TransactionItem(0,
                binding.transactionDescriptionInputText.getText().toString(),
                binding.transactionPriceInputText.getText().toString(),
                binding.transactionTimePickerTextView.getText().toString(),
                binding.transactionDatePickerTextView.getText().toString(),
                date.getTime()
        );
        helper.addTransaction(transactionItem);
        helper.updateBudgetByDate(binding.transactionDatePickerTextView.getText().toString()
                        + binding.transactionTimePickerTextView.getText().toString(),
                Double.parseDouble(binding.transactionPriceInputText.getText().toString())
                        * (-1));

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "." + (++monthOfYear) + "." + year;
        binding.transactionDatePickerTextView.setText(date);
    }

    public boolean isEditTextEmpty(EditText fField, EditText sField) {
        boolean isEditTextEmpty;
        if ((fField.getText().length() == 0)) {
            fField.setError("Must be fill.");
            isEditTextEmpty = true;
        }
        if ((sField.getText().length() == 0)) {
            sField.setError("Must be fill.");
            isEditTextEmpty = true;
        }else{
            isEditTextEmpty = false;
        }
        return isEditTextEmpty;

    }

    public void showDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePicker Dialog");

    }

    public void showTimePicker() {
        timePickerDialog = TimePickerDialog.newInstance(this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        timePickerDialog.show(getActivity().getFragmentManager(), "TimePicker Dialog");

    }
}
