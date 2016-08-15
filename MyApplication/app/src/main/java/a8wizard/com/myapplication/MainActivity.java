package a8wizard.com.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import a8wizard.com.myapplication.history.HistoryFragment;
import a8wizard.com.myapplication.statistic.StatisticFragment;
import a8wizard.com.myapplication.transactions.AddTransactionFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private LinearLayout tabAddNewTransactionLayout;
    private LinearLayout tabHistoryLayout;
    private LinearLayout tabStatisticLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineToolbar();
        setupToolbar();

        defineTabs();
        setupTabs();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.transaction_tab_layout:
                    showTransactionScreen();
                break;
            case R.id.history_tab_layout:
                showHistoryScreen();
                break;
            case R.id.statistic_tab_layout:
                showStatisticScreen();
                break;
        }

    }

    public void showScreen(Fragment fragment, String contentTag, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        if (addToBackStack) {
            transaction.addToBackStack(String.valueOf(System.identityHashCode(fragment)));
        }
        transaction.replace(R.id.main_frame_layout, fragment, contentTag);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

    private void showTransactionScreen() {
        showScreen(new AddTransactionFragment(), AddTransactionFragment.TAG, false);
    }

    private void showHistoryScreen() {
        showScreen(new HistoryFragment(), HistoryFragment.TAG, false);
    }

    private void showStatisticScreen() {
        showScreen(new StatisticFragment(), StatisticFragment.TAG, false);
    }
}
