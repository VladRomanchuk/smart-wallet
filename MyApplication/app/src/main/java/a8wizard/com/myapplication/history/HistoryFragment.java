package a8wizard.com.myapplication.history;

import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public static TextView timeTitle, descriptionTitle, billTitle;

    public static int adapterStatus;
    private boolean searching = false;

    private SQLHelper helper;

    public static final String TAG = "HistoryFragment";
    FragmentHistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);

        helper = new SQLHelper(getActivity());
        setupButton();
        defineView();

        binding.searchEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (HistoryFragment.adapterStatus == 1)
                    HistoryFragment.historyAdapter.getFilter().filter(arg0);
                else
                    HistoryFragment.transactionAdapter.getFilter().filter(arg0);
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        fill_list();

        return binding.getRoot();
    }

    private void defineView() {
        backButton = binding.historyBackButton;
        searchButton = binding.historyBackButton;
        timeTitle = binding.historyTimeTitle;
        descriptionTitle = binding.historyDescriptionTitle;
        billTitle = binding.historyBillTitle;
        listItem = binding.listHistory;
    }


    private void setupButton() {
        binding.historyBackButton.setVisibility(View.GONE);
        binding.historyBackButton.setOnClickListener(this);
        binding.searchBackButton.setOnClickListener(this);
    }

    private void fill_list() {
        ArrayList<HistoryItem> listHistory = new ArrayList<HistoryItem>();

        listHistory = helper.getAllHistory();
        HistoryFragment.historyAdapter = new HistoryAdapter(getActivity(),
                listHistory);
        HistoryFragment.adapterStatus = 1;

        binding.historyTimeTitle.setText(R.string.time_history_row);
        binding.historyDescriptionTitle.setText(R.string.description_history_row);
        binding.historyBillTitle.setText(R.string.bill_history_row);

        binding.listHistory.setAdapter(historyAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_back_button:
                ArrayList<HistoryItem> listHistory = new ArrayList<HistoryItem>();
                HistoryAdapter adapterHistory;

                SQLHelper helper = new SQLHelper(getActivity());
                SQLiteDatabase db = helper.getReadableDatabase();
                helper.onCreate(db);

                listHistory = helper.getAllHistory();
                adapterHistory = new HistoryAdapter(getActivity(), listHistory);

                binding.listHistory.setAdapter(adapterHistory);
                HistoryFragment.adapterStatus = 1;

                binding.historyTimeTitle.setText(R.string.time_history_row);
                binding.historyDescriptionTitle.setText(R.string.description_history_row);
                binding.historyBillTitle.setText(R.string.bill_history_row);

                binding.listHistory.setAdapter(historyAdapter);

                binding.historyBackButton.setVisibility(View.GONE);
                break;
            case R.id.search_back_button:

                if (searching) {
                    binding.searchEditText.setVisibility(View.GONE);
                    searching = false;
                } else {
                    binding.searchEditText.setVisibility(View.VISIBLE);
                    binding.searchEditText.requestFocus();
                    searching = true;
                }
                break;
        }
    }
}


