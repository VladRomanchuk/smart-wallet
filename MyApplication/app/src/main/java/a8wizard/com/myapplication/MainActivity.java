package a8wizard.com.myapplication;

import android.app.AlertDialog;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import a8wizard.com.myapplication.history.HistoryFragment;
import a8wizard.com.myapplication.statistic.StatisticFragment;
import a8wizard.com.myapplication.transactions.AddTransactionFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView toolbarTextView;

    private LinearLayout tabAddNewTransactionLayout;
    private LinearLayout tabHistoryLayout;
    private LinearLayout tabStatisticLayout;

    private SQLHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineToolbar();
        setupToolbar();

        defineTabs();
        setupTabs();

        helper = new SQLHelper(MainActivity.this);

        showTransactionScreen();

    }

    private void setupTabs() {
        tabAddNewTransactionLayout.setOnClickListener(this);
        tabHistoryLayout.setOnClickListener(this);
        tabStatisticLayout.setOnClickListener(this);
    }

    private void defineTabs() {
        tabAddNewTransactionLayout = (LinearLayout) findViewById(R.id.transaction_tab_layout);
        tabHistoryLayout = (LinearLayout) findViewById(R.id.history_tab_layout);
        tabStatisticLayout = (LinearLayout) findViewById(R.id.statistic_tab_layout);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void defineToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTextView = (TextView) findViewById(R.id.toolbar_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.budget:
                if (!Util.checkBudget(MainActivity.this)) {
                    showAlertInsertBudget();
                } else {
                    showAlertInfoBudget();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Fragment f;

        switch (view.getId()) {
            case R.id.transaction_tab_layout:
                f = getSupportFragmentManager().findFragmentByTag("AddTransactionFragment");
                if (f != null && f instanceof AddTransactionFragment) {
                } else {
                    showTransactionScreen();
                }
                break;
            case R.id.history_tab_layout:
                f = getSupportFragmentManager().findFragmentByTag("HistoryFragment");
                if (f != null && f instanceof HistoryFragment) {
                } else {
                    showHistoryScreen();
                }
                break;
            case R.id.statistic_tab_layout:
                f = getSupportFragmentManager().findFragmentByTag("StatisticFragment");
                if (f != null && f instanceof StatisticFragment) {
                } else {
                    showStatisticScreen();
                }
                break;
        }
    }

    public void showScreen(Fragment fragment, String contentTag, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(String.valueOf(System.identityHashCode(fragment)));
        }
        transaction.replace(R.id.main_frame_layout, fragment, contentTag);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

    public void showTransactionScreen() {
        showScreen(new AddTransactionFragment(), AddTransactionFragment.TAG, false);
        findViewById(R.id.main_layout).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorItems));
        tabAddNewTransactionLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        tabHistoryLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        tabStatisticLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        toolbarTextView.setText("Add Transaction");
    }

    private void showHistoryScreen() {
        showScreen(new HistoryFragment(), HistoryFragment.TAG, false);
        findViewById(R.id.main_layout).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        tabHistoryLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        tabAddNewTransactionLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        tabStatisticLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        toolbarTextView.setText("History");
    }

    private void showStatisticScreen() {
        showScreen(new StatisticFragment(), StatisticFragment.TAG, false);
        findViewById(R.id.main_layout).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        tabStatisticLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        tabHistoryLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        tabAddNewTransactionLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        toolbarTextView.setText("Statistic");
    }

    private void setTextToolbar(String toolbarText) {
        toolbarTextView.setText(toolbarText);
    }


    public void showAlertInsertBudget() {

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogview = inflater.inflate(R.layout.alertdialog_insertbudget, null);
        final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setView(dialogview);

        final EditText eSetBudget = (EditText) dialogview.findViewById(R.id.editText1);
        Button bSet = (Button) dialogview.findViewById(R.id.button1);
        Button bCancel = (Button) dialogview.findViewById(R.id.button2);

        final Spinner spinner = (Spinner) dialogview.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Spinner spinner2 = (Spinner) dialogview.findViewById(R.id.spinner2);
        ArrayList<String> list = new ArrayList<String>();

        list.add("Time start:");
        list.add((new SimpleDateFormat("dd/MM/yyyy kk:mm:ss")).format(new Date()));
        list.add((new SimpleDateFormat("dd/MM/yyyy")).format(new Date()) + " 24:00:00");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

        bSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
                    Date startDate = sdf.parse(spinner2.getSelectedItem().toString());
                    Date endDate = sdf.parse(spinner2.getSelectedItem().toString());

                    if (spinner.getSelectedItem().toString().equals("Choose your category:") ||
                            spinner2.getSelectedItem().toString().equals("Time start:") || eSetBudget.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this,
                                "Please fill all forms", Toast.LENGTH_SHORT).show();
                    } else {

                        if (spinner.getSelectedItem().toString().equals("Weekly")) {
                            endDate = Util.addDays(startDate, 7);
                        } else if (spinner.getSelectedItem().toString().equals("Monthly")) {
                            endDate = Util.addMonths(startDate, 1);
                        }

                        BudgetItem budget = new BudgetItem(0, (
                                new SimpleDateFormat("dd/MM/yyyy")).format(startDate),
                                (new SimpleDateFormat("dd/MM/yyyy")).format(endDate),
                                spinner.getSelectedItem().toString(),
                                eSetBudget.getText().toString(),
                                eSetBudget.getText().toString(),
                                startDate.getTime(), endDate.getTime());


                        helper.addBudget(budget);

                        alert.dismiss();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (java.text.ParseException e) {

                }

            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                alert.dismiss();
            }
        });

        alert.show();
    }

    public void showAlertInfoBudget() {

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View dialogview = inflater.inflate(R.layout.alertdialog_infobudget, null);
        final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setCancelable(false);

        final BudgetItem budget = helper.getDetailLastBudget();

        TextView tStartDate = (TextView) dialogview.findViewById(R.id.textView2);
        TextView tEndDate = (TextView) dialogview.findViewById(R.id.textView3);
        TextView tCategory = (TextView) dialogview.findViewById(R.id.textView4);
        TextView tAmount = (TextView) dialogview.findViewById(R.id.textView5);
        TextView tLeft = (TextView) dialogview.findViewById(R.id.textView6);
        Button bCancel = (Button) dialogview.findViewById(R.id.button1);
        Button bReset = (Button) dialogview.findViewById(R.id.button2);

        tStartDate.setText(Util.getDateString(budget.getTimeStartDate(), new SimpleDateFormat("dd/MM/yy kk:mm:ss")));
        tEndDate.setText(Util.getDateString(budget.getTimeEndDate(), new SimpleDateFormat("dd/MM/yy kk:mm:ss")));
        tCategory.setText(budget.getCategory());
        tAmount.setText(Util.formatUSD(budget.getAmount()));
        tLeft.setText(Util.formatUSD(budget.getLeft()));

        bReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alert.dismiss();
                helper.deleteBudget(budget.getIdBudget());
                showAlertInsertBudget();

            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        alert.setView(dialogview);
        alert.show();
    }

}
