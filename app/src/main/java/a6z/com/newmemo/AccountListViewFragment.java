package a6z.com.newmemo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import a6z.com.newmemo.control.DividerItemDecoration;
import a6z.com.newmemo.model.Account;
import a6z.com.newmemo.model.Account.AccountItem;

/**
 * 帐号列表 Fragment
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class AccountListViewFragment extends Fragment {

    private int mColumnCount = 1;

    private OnFragmentInteractionListener mListener;

    private TextView mEmptyTipsView;
    private RecyclerView mRecyclerView;

    public AccountListViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);

        mEmptyTipsView = (TextView) view.findViewById(R.id.emptyTips);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_account_list);

        checkViewMode();

        // Set the adapter
        if (mRecyclerView != null) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(new AccountListViewItemRecyclerViewAdapter(Account.ITEMS, mListener));
        }

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onAddItemRequest();
                    }
                    /*addItem(Account.createItem("中国银行", "房贷还款", null));
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                }
            });
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void checkViewMode() {
        if (Account.ITEMS.size() > 0) {
            if (mRecyclerView != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            if (mEmptyTipsView != null) {
                mEmptyTipsView.setVisibility(View.GONE);
            }
        } else {
            if (mRecyclerView != null) {
                mRecyclerView.setVisibility(View.GONE);
            }
            if (mEmptyTipsView != null) {
                mEmptyTipsView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void addItem(AccountItem item) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.id_account_list);
        if (recyclerView != null) {
            int insertPosition = 0;
            ((AccountListViewItemRecyclerViewAdapter) recyclerView.getAdapter()).add(item, insertPosition);
            recyclerView.scrollToPosition(insertPosition);
        }
        checkViewMode();
    }

    public void removeItem(AccountItem item) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.id_account_list);
        if (recyclerView != null) {
            ((AccountListViewItemRecyclerViewAdapter) recyclerView.getAdapter()).remove(item);
        }
        checkViewMode();
    }

    public void removeItem(String itemId) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.id_account_list);
        if (recyclerView != null) {
            ((AccountListViewItemRecyclerViewAdapter) recyclerView.getAdapter()).remove(itemId);
        }
        checkViewMode();
    }

    public void notifyItemChanged(String itemId) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.id_account_list);
        if (recyclerView != null) {
            ((AccountListViewItemRecyclerViewAdapter) recyclerView.getAdapter()).notifyItemChanged(itemId);
        }
    }

    /**
     * 列表项被点击后的交互事件触发
     */
    public interface OnFragmentInteractionListener {
        void onItemClicked(AccountItem item);

        void onAddItemRequest();
    }
}
