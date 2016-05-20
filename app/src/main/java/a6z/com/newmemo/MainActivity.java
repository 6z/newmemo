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
        , AccountListViewFragment.OnListFragmentInteractionListener {

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
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        showFragment(id);

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
                switch (requestCode) {
                    case ViewTransaction.ACCOUNT_VIEW:
                        int action = data.getIntExtra(ViewTransaction.ACTION_ARG_TAG, -1);
                        if (action == ViewTransaction.ACTION_DEL) {
                            accountFragment.removeItem(data.getStringExtra(AccountItemViewFragment.ARG_TAG));
                        } else if (action == ViewTransaction.ACTION_MODIFY) {
                            accountFragment.notifyItemChanged(data.getStringExtra(AccountItemViewFragment.ARG_TAG));
                        }
                        break;
                    case ViewTransaction.ACCOUNT_EDIT:

                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void showFragment(int key) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (key == R.id.nav_account) {
            if (accountFragment == null) {
                accountFragment = new AccountListViewFragment();
            }
            transaction.replace(R.id.id_content, accountFragment);
        }/* else if (key == R.id.nav_calendar) {

        } else if (key == R.id.nav_smarthome) {

        } else if (key == R.id.nav_settings) {

        } else if (key == R.id.nav_share) {

        } else if (key == R.id.nav_send) {

        }*/

        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Account.AccountItem item) {
        Intent intent = new Intent(this, AccountItemActivity.class);
        intent.putExtra(AccountItemViewFragment.ARG_TAG, item.getId());
        //startActivity(intent);
        //noinspection unchecked
        startActivityForResult(intent, ViewTransaction.ACCOUNT_VIEW, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
