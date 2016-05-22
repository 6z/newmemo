package a6z.com.newmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import a6z.com.newmemo.model.Account;

public class AccountItemEditActivity extends AppCompatActivity {

    Button okButton;
    Button cancelButton;
    private Account.AccountItem m_AccountItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_item_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set activity animations
        getWindow().setEnterTransition(new Slide(Gravity.TOP));
        getWindow().setExitTransition(new Slide(Gravity.BOTTOM));

        Intent intent = getIntent();
        try {
            m_AccountItem = (Account.AccountItem) Account.ITEM_MAP.get(intent.getStringExtra(AccountItemViewFragment.ARG_TAG)).clone();
            m_AccountItem.addDetail("", "");
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_item_detail_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AccountItemEditDetailRecyclerViewAdapter(m_AccountItem.getDetails()));

        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Account.AccountItem accountItem = Account.ITEM_MAP.get(intent.getStringExtra(AccountItemViewFragment.ARG_TAG));
                accountItem.setTitle("我已被修改");
                setResult(RESULT_OK, intent);
                AccountItemEditActivity.this.finishAfterTransition();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountItemEditActivity.this.finishAfterTransition();
            }
        });
    }

}
