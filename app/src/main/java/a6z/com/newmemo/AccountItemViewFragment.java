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
import android.widget.TextView;

import a6z.com.newmemo.control.ExpandableView;
import a6z.com.newmemo.model.Account;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountItemViewFragment extends Fragment {

    public static final String ARG_TAG = "item_id";

    private OnFragmentInteractionListener mListener;

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
            detailView.fillData(android.R.drawable.ic_menu_view, "账号明细", true);
            detailView.setActionButton(R.drawable.ic_add_black, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemDetailAddRequested();
                    }
                }
            });
            RecyclerView recyclerView = new RecyclerView(getContext());
            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new AccountItemDetailRecyclerViewAdapter(mItem, mListener));

            detailView.addContentView(recyclerView);
            detailView.setContentDefaultVisible(View.VISIBLE);
        }
        ExpandableView commentView = (ExpandableView) view.findViewById(R.id.id_item_comment_container);
        if (commentView != null) {
            commentView.fillData(android.R.drawable.ic_menu_info_details, "帐号说明", true);
            commentView.setActionButton(R.drawable.ic_mode_edit_black, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemCommentEditRequest(mItem.getComment());
                    }
                }
            });
            View contentView = inflater.inflate(R.layout.account_comment_view, commentView, false);
            //TextView contentView = new TextView(getContext());
            TextView contentTextView = (TextView) contentView.findViewById(R.id.id_comment);
            contentTextView.setText(mItem.getComment());
            commentView.addContentView(contentView);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    public void removeItemDetail(String detailId) {
        ExpandableView detailView = (ExpandableView) getView().findViewById(R.id.id_item_detail_container);
        RecyclerView recyclerView = (RecyclerView) detailView.getContentLayout().getChildAt(0);
        AccountItemDetailRecyclerViewAdapter adapter = (AccountItemDetailRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.removeItem(detailId);
    }

    public interface OnFragmentInteractionListener {

        public void onItemDetailEditRequested(Account.AccountDetail detail);

        public void onItemDetailRemoveRequested(Account.AccountDetail detail);

        public void onItemDetailAddRequested();

        public void onItemCommentEditRequest(String comment);
    }
}
