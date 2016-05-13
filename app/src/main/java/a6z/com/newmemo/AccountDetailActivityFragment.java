package a6z.com.newmemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        if (getArguments().containsKey(ARG_TAG)) {
            mItem = Account.ITEM_MAP.get(getArguments().getString(ARG_TAG));

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.id_item_detail_list);
        if (recyclerView != null) {
            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(new MyAccountDetailRecyclerViewAdapter(mItem.getDetails()));
        }
        return view;
    }
}
