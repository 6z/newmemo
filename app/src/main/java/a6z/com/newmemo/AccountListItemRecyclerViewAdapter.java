package a6z.com.newmemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import a6z.com.newmemo.AccountListViewFragment.OnListFragmentInteractionListener;
import a6z.com.newmemo.model.Account.AccountItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AccountItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AccountListItemRecyclerViewAdapter extends RecyclerView.Adapter<AccountListItemRecyclerViewAdapter.ViewHolder> {

    private final List<AccountItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public AccountListItemRecyclerViewAdapter(List<AccountItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_account_list_item_tpl, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mCommentView.setText(mValues.get(position).getComment());
        holder.mUpdateTimeView.setText(mValues.get(position).getFormattedUpdateTime(holder.mView.getContext().getString(R.string.time_format_pattern)));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mCommentView;
        public final TextView mUpdateTimeView;
        public AccountItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.id_title);
            mCommentView = (TextView) view.findViewById(R.id.id_comment);
            mUpdateTimeView = (TextView) view.findViewById(R.id.id_update_timestamp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCommentView.getText() + "'";
        }
    }
}
