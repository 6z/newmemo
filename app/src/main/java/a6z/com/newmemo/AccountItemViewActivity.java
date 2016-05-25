package a6z.com.newmemo;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import a6z.com.newmemo.control.ExpandableView;
import a6z.com.newmemo.model.Account;

public class AccountItemViewActivity extends AppCompatActivity implements AccountItemDetailRecyclerViewAdapter.OnInteractionListener {

    public static final String ARG_TAG = "item_id";

    private Account.AccountItem mItem;

    private boolean m_IsModified;

    private CollapsingToolbarLayout mAppBarLayout;
    private RecyclerView mDetailView;
    private TextView mCommentView;

    private ExpandableView mDetailExpandableView;
    private ExpandableView mCommentExpandableView;

    //private AccountItemViewFragment mAccountItemViewFragment;

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

        Intent intent = getIntent();

        mItem = Account.ITEM_MAP.get(intent.getStringExtra(ARG_TAG));

        mAppBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (mAppBarLayout != null) {
            mAppBarLayout.setTitle(mItem.getTitle());
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
            initView();
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            /*Bundle arguments = new Bundle();
            arguments.putString(AccountItemViewFragment.ARG_TAG,
                    getIntent().getStringExtra(AccountItemViewFragment.ARG_TAG));
            mAccountItemViewFragment = new AccountItemViewFragment();
            mAccountItemViewFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, mAccountItemViewFragment)
                    .commit();*/
        }

        //Set activity animations
        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        getWindow().setExitTransition(new Slide(Gravity.LEFT));

    }

    private void initView() {
        mDetailExpandableView = (ExpandableView) findViewById(R.id.id_item_detail_container);
        if (mDetailExpandableView != null) {
            mDetailExpandableView.fillData(android.R.drawable.ic_menu_view, "账号明细", true);
            mDetailExpandableView.setActionButton(R.drawable.ic_add_black, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemDetailAddRequested();
                }
            });
            mDetailView = new RecyclerView(this);
            mDetailView.setLayoutManager(new LinearLayoutManager(this));
            mDetailView.setAdapter(new AccountItemDetailRecyclerViewAdapter(mItem, this));

            mDetailExpandableView.addContentView(mDetailView);
            mDetailExpandableView.setContentDefaultVisible(View.VISIBLE);
        }
        mCommentExpandableView = (ExpandableView) findViewById(R.id.id_item_comment_container);
        if (mCommentExpandableView != null) {
            mCommentExpandableView.fillData(android.R.drawable.ic_menu_info_details, "帐号说明", true);
            View contentView = getLayoutInflater().inflate(R.layout.account_comment_view, mCommentExpandableView, false);
            mCommentView = (TextView) contentView.findViewById(R.id.id_comment);
            mCommentView.setText(mItem.getComment());
            mCommentExpandableView.addContentView(contentView);
            mCommentExpandableView.setContentDefaultVisible(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_detail, menu);

        //MenuItem delMenu = menu.findItem(R.id.action_del);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (m_IsModified) {
            Intent intent = getIntent();
            intent.putExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACTION_MODIFY);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == android.R.id.home) {
            if (m_IsModified) {
                Intent intent = getIntent();
                intent.putExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACTION_MODIFY);
                setResult(RESULT_OK, intent);
                this.finishAfterTransition();
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
                    AccountItemViewActivity.this.finishAfterTransition();
                }

            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
            builder.create().show();
        } else if (menuId == R.id.action_modify) {
            onItemBaseInfoEditRequest();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //处理 帐号详情 编辑返回逻辑
        if (requestCode == ViewTransaction.PAGE_ACCOUNT_ITEM_DETAIL_EDIT) {
            if (RESULT_OK != resultCode) {
                return;
            }
            int action = data.getIntExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACTION_NONE);
            if (action == ViewTransaction.ACTION_NONE) {
                return;
            }
            String name = data.getStringExtra(AccountItemDetailEditActivity.NAME_ARG_TAG);
            String value = data.getStringExtra(AccountItemDetailEditActivity.VALUE_ARG_TAG);
            if (action == ViewTransaction.ACTION_MODIFY) {
                String id = data.getStringExtra(AccountItemDetailEditActivity.ID_ARG_TAG);
                modifyItemDetail(id, name, value);
            } else if (action == ViewTransaction.ACTION_NEW) {
                addItemDetail(name, value);
            }
        } else if (requestCode == ViewTransaction.PAGE_ACCOUNT_ITEM_INFO_EDIT) {

            if (RESULT_OK != resultCode) {
                return;
            }
            int action = data.getIntExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACTION_NONE);
            //基本信息编辑返回到这的状态,只有修改
            if (action != ViewTransaction.ACTION_MODIFY) {
                return;
            }
            String title = data.getStringExtra(AccountItemInfoEditActivity.NAME_ARG_TAG);
            String comment = data.getStringExtra(AccountItemInfoEditActivity.COMMENT_ARG_TAG);
            if (mAppBarLayout != null) {
                mAppBarLayout.setTitle(title);
            }
            modifyItem(title, comment);
            m_IsModified = true;
        }

    }

    @Override
    public void onItemDetailEditRequested(Account.AccountDetail detail) {
        Intent intent = new Intent(this, AccountItemDetailEditActivity.class);
        intent.putExtra(AccountItemDetailEditActivity.ID_ARG_TAG, detail.getId());
        intent.putExtra(AccountItemDetailEditActivity.NAME_ARG_TAG, detail.getName());
        intent.putExtra(AccountItemDetailEditActivity.VALUE_ARG_TAG, detail.getValue());

        startActivityForResult(intent, ViewTransaction.PAGE_ACCOUNT_ITEM_DETAIL_EDIT, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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
                removeItemDetail(detail.getId());
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

    public void onItemDetailAddRequested() {
        Intent intent = new Intent(this, AccountItemDetailEditActivity.class);
        startActivityForResult(intent, ViewTransaction.PAGE_ACCOUNT_ITEM_DETAIL_EDIT, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void onItemBaseInfoEditRequest() {
        Intent intent = new Intent(this, AccountItemInfoEditActivity.class);
        intent.putExtra(AccountItemInfoEditActivity.ID_ARG_TAG, mItem.getId());
        intent.putExtra(AccountItemInfoEditActivity.NAME_ARG_TAG, mItem.getTitle());
        intent.putExtra(AccountItemInfoEditActivity.COMMENT_ARG_TAG, mItem.getComment());

        startActivityForResult(intent, ViewTransaction.PAGE_ACCOUNT_ITEM_INFO_EDIT, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void addItemDetail(String name, String value) {
        AccountItemDetailRecyclerViewAdapter adapter = (AccountItemDetailRecyclerViewAdapter) mDetailView.getAdapter();
        adapter.addItem(name, value);
        mDetailExpandableView.notifyContentViewChanged();
    }

    private void removeItemDetail(String detailId) {
        AccountItemDetailRecyclerViewAdapter adapter = (AccountItemDetailRecyclerViewAdapter) mDetailView.getAdapter();
        adapter.removeItem(detailId);
        mDetailExpandableView.notifyContentViewChanged();
    }

    private void modifyItemDetail(String detailId, String name, String value) {
        AccountItemDetailRecyclerViewAdapter adapter = (AccountItemDetailRecyclerViewAdapter) mDetailView.getAdapter();
        adapter.modifyItem(detailId, name, value);
    }

    private void modifyItem(String title, String comment) {
        Account.modifyItem(mItem.getId(), title, comment);
        //mItem.setTitle(title);
        //mItem.setComment(comment);
        mAppBarLayout.setTitle(title);
        mCommentView.setText(comment);
        mCommentExpandableView.notifyContentViewChanged();
    }
}
