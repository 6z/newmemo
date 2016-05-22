package a6z.com.newmemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import a6z.com.newmemo.model.Account;

public class AccountItemActivity extends AppCompatActivity implements AccountItemViewFragment.OnFragmentInteractionListener {

    private boolean m_IsModified;

    private AccountItemViewFragment mAccountItemViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle("账号标题");
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(AccountItemViewFragment.ARG_TAG,
                    getIntent().getStringExtra(AccountItemViewFragment.ARG_TAG));
            mAccountItemViewFragment = new AccountItemViewFragment();
            mAccountItemViewFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, mAccountItemViewFragment)
                    .commit();
        }

        //Set activity animations
        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        getWindow().setExitTransition(new Slide(Gravity.LEFT));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        //Toast.makeText(this, String.valueOf(menuId), Toast.LENGTH_SHORT).show();

        /*if (menuId == R.id.action_edit) {
            Intent intent = new Intent(this, AccountItemEditActivity.class);
            intent.putExtra(AccountItemViewFragment.ARG_TAG, getIntent().getStringExtra(AccountItemViewFragment.ARG_TAG));
            startActivityForResult(intent, ViewTransaction.ACCOUNT_EDIT);
        } else*/
        if (menuId == android.R.id.home) {
            if (m_IsModified) {
                Intent intent = getIntent();
                intent.putExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACCOUNT_EDIT);
                setResult(RESULT_OK, intent);
                AccountItemActivity.this.finishAfterTransition();
            } else {
                this.finishAfterTransition();
            }
        } else if (menuId == R.id.action_del) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确认删除吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = getIntent();
                    intent.putExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACTION_DEL);
                    setResult(RESULT_OK, intent);
                    AccountItemActivity.this.finishAfterTransition();
                }

            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
            builder.create().show();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                m_IsModified = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemDetailEditRequested(Account.AccountDetail detail) {
        Intent intent = new Intent(this, AccountItemDetailEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemDetailRemoveRequested(final Account.AccountDetail detail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAccountItemViewFragment.removeItemDetail(detail.getId());
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onItemDetailAddRequested() {

    }

    @Override
    public void onItemCommentEditRequest(String comment) {

    }
}
