package a6z.com.newmemo;

import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import a6z.com.newmemo.model.Account;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountDetailActivityFragment extends Fragment {

    public static final String ARG_TAG = "item_id";

    private Account.AccountItem mItem;

    public AccountDetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().containsKey(ARG_TAG)){
            mItem =(Account.AccountItem) Account.ITEM_MAP.get(getArguments().getString(ARG_TAG));

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_detail, container, false);
    }
}
