package a8wizard.com.myapplication.transactions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.Util;
import a8wizard.com.myapplication.databinding.FragmentTransactionAddBinding;

public class AddTransactionFragment extends Fragment implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener {

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

        setupLayouts();
        initTextView();
        helper = new SQLHelper(getActivity());
        now = Calendar.getInstance();
        editTextFocusListenerSetup();

        return binding.getRoot();
    }

    private void editTextFocusListenerSetup() {
        binding.transactionPriceInputText.setOnFocusChangeListener(this);
        binding.transactionDescriptionInputText.setOnFocusChangeListener(this);

    }

    private void colorizeFocusItem(boolean focus, View view) {

        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isAcceptingText()) {
            focus = true;
        } else {
            focus = false;
        }
        if (focus) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorItems));
        }
    }

    private void initTextView() {
        transactionDatePickerTextView = binding.transactionDatePickerTextView;
        transactionTimePickerTextView = binding.transactionTimePickerTextView;
    }


    private void setupLayouts() {
        binding.addNewTransaction.setOnClickListener(this);
        binding.datePickerLayout.setOnClickListener(this);
        binding.timePickerLayout.setOnClickListener(this);
        binding.transactionPriceInputText.setOnClickListener(this);
        binding.transactionDescriptionInputText.setOnClickListener(this);
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
                }
                break;
            case R.id.date_picker_layout:
                showDatePicker();
                break;
            case R.id.time_picker_layout:
                showTimePicker(now);
                break;
        }
        defocusingEditText();

    }

    private void defocusingEditText() {
        binding.priceLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorItems));
        binding.descriptionLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorItems));
    }

    private void addTransaction() {
        TransactionItem transactionItem = new TransactionItem(0,
                binding.transactionDescriptionInputText.getText().toString(),
                binding.transactionPriceInputText.getText().toString(),
                binding.transactionTimePickerTextView.getText().toString(),
                binding.transactionDatePickerTextView.getText().toString(),
                Util.getTimeStamp(binding.transactionTimePickerTextView.getText().toString(), new SimpleDateFormat("HH:mm")));
        helper.addTransaction(transactionItem);
        helper.updateBudgetByDate(binding.transactionDatePickerTextView.getText().toString() +
                        binding.transactionTimePickerTextView.getText().toString(),
                Double.parseDouble(binding.transactionPriceInputText.getText().toString()) * (-1));

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
        } else {
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

    public void showTimePicker(Calendar calendar) {
        timePickerDialog = TimePickerDialog.newInstance(this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        timePickerDialog.show(getActivity().getFragmentManager(), "TimePicker Dialog");

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.transaction_price_input_text:
                colorizeFocusItem(b, binding.priceLayout);
                break;
            case R.id.transaction_description_input_text:
                colorizeFocusItem(b, binding.descriptionLayout);
                break;
        }
    }
}
