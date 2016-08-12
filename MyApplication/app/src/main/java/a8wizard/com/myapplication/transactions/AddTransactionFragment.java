package a8wizard.com.myapplication.transactions;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.databinding.FragmentTransactionAddBinding;

public class AddTransactionFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {

    FragmentTransactionAddBinding binding;

    public static final String TAG = "AddTransactionFragment";
    public static TextView datePickerTextView;
    public static TextView timePickerTextView;

    private Calendar now;
    private SQLHelper helper;
    private TimePickerDialog timePickerDialog;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_add, container, false);

        setupButton();
        defineTextView();
        setupTextView();
        helper = new SQLHelper(getActivity());
        datePickerTextView.setText(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        now = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog();
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
                showDatePicker();
                Toast.makeText(getActivity(), "Man", Toast.LENGTH_SHORT).show();
                break;
            case R.id.transaction_time_picker_text_view:
                showTimePicker();
                Toast.makeText(getActivity(), "Man", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showDatePicker() {
        binding.transactionPriceInputText.setFocusable(false);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show(getActivity().getFragmentManager(), "Datepicker Dialog");

    }

    private void showTimePicker() {
        timePickerDialog = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), false);

        timePickerDialog.setOnCancelListener(this);
        timePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        timePickerDialog.show(getActivity().getFragmentManager(), "Timepicker Dialog");

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        timePickerTextView.setText(hourOfDay + ":" + minute);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"."+(++monthOfYear)+"."+year;
        datePickerTextView.setText(date);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        timePickerDialog.dismiss();
    }
}
