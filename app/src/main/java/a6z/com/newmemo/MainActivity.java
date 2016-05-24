package a6z.com.newmemo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import a6z.com.newmemo.model.Account;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , AccountListViewFragment.OnFragmentInteractionListener {

    private AccountListViewFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_account);
        }
        Account.readFromFile(this);
        showFragment(R.id.nav_account, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                finish();
                System.exit(0);
            }
        } else {
            super.onBackPressed();
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Account.saveToFile(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        showFragment(id, true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                int action = data.getIntExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACTION_NONE);
                switch (requestCode) {
                    case ViewTransaction.PAGE_ACCOUNT_ITEM_VIEW:
                        if (action == ViewTransaction.ACTION_DEL) {
                            accountFragment.removeItem(data.getStringExtra(AccountItemViewActivity.ARG_TAG));
                        } else if (action == ViewTransaction.ACTION_MODIFY) {
                            accountFragment.notifyItemChanged(data.getStringExtra(AccountItemViewActivity.ARG_TAG));
                        }
                        break;
                    case ViewTransaction.PAGE_ACCOUNT_ITEM_INFO_EDIT:
                        if (action == ViewTransaction.ACTION_NEW) {
                            String title = data.getStringExtra(AccountItemInfoEditActivity.NAME_ARG_TAG);
                            String comment = data.getStringExtra(AccountItemInfoEditActivity.COMMENT_ARG_TAG);
                            Account.AccountItem item = Account.createItem(title, comment, null);
                            accountFragment.addItem(item);
                            onItemClicked(item);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void showFragment(int key, boolean canRollback) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (key == R.id.nav_account) {
            if (accountFragment == null) {
                accountFragment = new AccountListViewFragment();
            }
            setTitle(getResources().getString(R.string.account_module_name));
            transaction.replace(R.id.id_content, accountFragment);
        }/* else if (key == R.id.nav_calendar) {

        } else if (key == R.id.nav_smarthome) {

        } else if (key == R.id.nav_settings) {

        } else if (key == R.id.nav_share) {

        } else if (key == R.id.nav_send) {

        }*/
        if (canRollback) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onItemClicked(Account.AccountItem item) {
        Intent intent = new Intent(this, AccountItemViewActivity.class);
        intent.putExtra(AccountItemViewActivity.ARG_TAG, item.getId());
        //noinspection unchecked
        startActivityForResult(intent, ViewTransaction.PAGE_ACCOUNT_ITEM_VIEW, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void onAddItemRequest() {
        Intent intent = new Intent(this, AccountItemInfoEditActivity.class);
        //noinspection unchecked
        startActivityForResult(intent, ViewTransaction.PAGE_ACCOUNT_ITEM_INFO_EDIT, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
