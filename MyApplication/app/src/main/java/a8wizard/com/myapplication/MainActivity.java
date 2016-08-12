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

import a8wizard.com.myapplication.transactions.AddTransactionFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineToolbar();
        setupToolbar();
        showIcon();
    }

    private void showIcon() {
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu));
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
        switch (item.getItemId()){
            case R.id.search:
                showScreen(new AddTransactionFragment(), AddTransactionFragment.TAG, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showScreen(Fragment fragment, String contentTag, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        // TODO: 7/28/16 make change animation

        if (addToBackStack) {
            transaction.addToBackStack(String.valueOf(System.identityHashCode(fragment)));
        }
        transaction.replace(R.id.main_frame_layout, fragment, contentTag);
        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }
}
