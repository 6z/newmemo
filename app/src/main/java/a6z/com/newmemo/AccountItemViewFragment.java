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

import a6z.com.newmemo.control.ExpandableView;
import a6z.com.newmemo.model.Account;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountItemViewFragment extends Fragment {

    public static final String ARG_TAG = "item_id";

    private Account.AccountItem mItem;

    public AccountItemViewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_account_item, container, false);

        //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.id_item_detail_list);
        ExpandableView detailView = (ExpandableView) view.findViewById(R.id.id_item_detail_container);
        if (detailView != null) {
            detailView.fillData(R.drawable.ic_menu_send, "账号明细");
            RecyclerView recyclerView = new RecyclerView(getContext());
            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new AccountItemDetailRecyclerViewAdapter(mItem.getDetails()));

            detailView.addContentView(recyclerView);
            detailView.setContentDefaultVisible(View.VISIBLE);
        }
        ExpandableView commentView = (ExpandableView) view.findViewById(R.id.id_item_comment_container);
        if (commentView != null) {
            commentView.fillData(R.drawable.ic_menu_share, mItem.getComment());
        }

        return view;
    }

}
