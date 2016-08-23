package a8wizard.com.myapplication.history;

import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import a8wizard.com.myapplication.R;
import a8wizard.com.myapplication.SQLHelper;
import a8wizard.com.myapplication.databinding.FragmentHistoryBinding;
import a8wizard.com.myapplication.transactions.TransactionAdapter;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    public static HistoryAdapter historyAdapter;

    public static TransactionAdapter transactionAdapter;

    public static ListView listItem;
    public static Button backButton;
    public static Button searchButton;
    public static EditText searchEditText;
    public static TextView dateTitle, transactionTitle, totalTitle;

    private ArrayList<HistoryItem> listHistory;
    public static int adapterStatus;
    private boolean searching = false;

    private SQLHelper helper;

    public static final String TAG = "HistoryFragment";
    FragmentHistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        binding.historyLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));

        setupDB();
        defineView();
        setupButton();

        binding.searchEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                if (HistoryFragment.adapterStatus == 1)
                    HistoryFragment.historyAdapter.getFilter().filter(arg0);
                else
                    HistoryFragment.transactionAdapter.getFilter().filter(arg0);
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {
            }
        });
        fillList();

        return binding.getRoot();
    }

    private void defineView() {
        backButton = binding.historyBackButton;
        searchButton = binding.historyBackButton;
        dateTitle = binding.dateHistoryRow;
        transactionTitle = binding.historyTransactionTitle;
        totalTitle = binding.historyTotalTitle;
        listItem = binding.listHistory;
        searchEditText = binding.searchEditText;
    }


    private void setupButton() {
        binding.historyBackButton.setVisibility(View.GONE);
        binding.historyBackButton.setOnClickListener(this);
        binding.searchBackButton.setOnClickListener(this);
    }

    private void fillList() {
        listHistory = new ArrayList<HistoryItem>();
        listHistory = helper.getAllHistory();

        HistoryFragment.historyAdapter = new HistoryAdapter(getActivity(), listHistory);
        HistoryFragment.adapterStatus = 1;
        setTextTextView();

        binding.listHistory.setAdapter(historyAdapter);

    }

    private void setTextTextView() {

        binding.dateHistoryRow.setText(R.string.date_history_row);
        binding.historyTransactionTitle.setText(R.string.transaction_history_row);
        binding.historyTotalTitle.setText(R.string.total_history_row);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_back_button:
                getAllHistory();
                break;
            case R.id.search_back_button:
                isSearching();
                break;
        }
    }

    private void isSearching() {
        if (searching) {
            binding.searchEditText.setVisibility(View.GONE);
            searching = false;
        } else {
            binding.searchEditText.setVisibility(View.VISIBLE);
            binding.searchEditText.requestFocus();
            searching = true;
        }
    }

    public void getAllHistory() {
        listHistory = new ArrayList<HistoryItem>();

        setupDB();

        listHistory = helper.getAllHistory();
        historyAdapter = new HistoryAdapter(getActivity(), listHistory);

        binding.listHistory.setAdapter(historyAdapter);
        HistoryFragment.adapterStatus = 1;
        setTextTextView();

    }

    private void setupDB() {
        helper = new SQLHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        helper.onCreate(db);
    }
}


