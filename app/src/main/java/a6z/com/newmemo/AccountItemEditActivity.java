package a6z.com.newmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import a6z.com.newmemo.model.Account;

public class AccountItemEditActivity extends AppCompatActivity {

    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_item_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    }

}
