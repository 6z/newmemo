package a6z.com.newmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountItemInfoEditActivity extends AppCompatActivity {

    public static final String ID_ARG_TAG = "ID";
    public static final String NAME_ARG_TAG = "NAME";
    public static final String COMMENT_ARG_TAG = "COMMENT";

    private String mItemId;

    private EditText mNameInputView;
    private EditText mCommentInputView;
    private Button mOkButton;
    private Button mCancelButton;

    private boolean isNew() {
        return mItemId == null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_item_info_edit);

        //Set activity animations
        getWindow().setEnterTransition(new Slide(Gravity.BOTTOM));
        getWindow().setExitTransition(new Slide(Gravity.TOP));

        mNameInputView = (EditText) findViewById(R.id.id_name);
        mCommentInputView = (EditText) findViewById(R.id.id_comment);
        mOkButton = (Button) findViewById(R.id.ok_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);

        Intent intent = getIntent();
        mItemId = intent.getStringExtra(ID_ARG_TAG);
        if (!isNew()) {
            mNameInputView.setText(intent.getStringExtra(NAME_ARG_TAG));
            mCommentInputView.setText(intent.getStringExtra(COMMENT_ARG_TAG));
            setTitle(getResources().getString(R.string.app_name) + " - 编辑帐号基本信息");
        } else {
            setTitle(getResources().getString(R.string.app_name) + " - 新增帐号");
        }

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (isNew()) {
                    intent.putExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACTION_NEW);
                } else {
                    intent.putExtra(ViewTransaction.ACTION_ARG_TAG, ViewTransaction.ACTION_MODIFY);
                    intent.putExtra(ID_ARG_TAG, mItemId);
                }
                intent.putExtra(NAME_ARG_TAG, mNameInputView.getText().toString());
                intent.putExtra(COMMENT_ARG_TAG, mCommentInputView.getText().toString());
                setResult(RESULT_OK, intent);
                AccountItemInfoEditActivity.this.finishAfterTransition();
            }
        });
    }
}
