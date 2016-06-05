package a6z.com.newmemo;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import a6z.com.newmemo.model.Account;
import a6z.com.newmemo.model.AccountItem;
import a6z.com.newmemo.model.AccountItemDetail;

/**
 * 自定义账号详细里的列表适配器
 */
public class AccountItemDetailRecyclerViewAdapter extends RecyclerView.Adapter<AccountItemDetailRecyclerViewAdapter.ViewHolder> {

    private final AccountItem mAccountItem;
    private OnInteractionListener mListener;
    private boolean mEditable;

    private List<ViewHolder> mViewHolderList = new ArrayList<>();

    public AccountItemDetailRecyclerViewAdapter(AccountItem accountItem, boolean editable, OnInteractionListener listener) {
        this.mAccountItem = accountItem;
        this.mListener = listener;
        this.mEditable = editable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item_detail_tpl, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        mViewHolderList.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AccountItemDetail item = mAccountItem.getDetails().get(position);
        holder.mNameView.setText(item.getName());
        //绘制带下划线的文本
        //holder.mValueView.setText(StringUtil.getUnderlineString(item.getValue()));

        holder.mValueView.setText(item.getValue());
        //Drawable bitmap =holder.mRootView.getContext().getResources().getDrawable(R.drawable.ic_menu_camera,null);
        //holder.mLogView.setImageDrawable(bitmap);
        holder.mLogView.setImageBitmap(BitmapDrawer.getFilledRect(40, 40,
                ContextCompat.getColor(holder.itemView.getContext(), R.color.colorItemLittleLogo)));
        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemDetailRemoveRequested(item);
                }
            }
        });
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemDetailEditRequested(item);
                }
            }
        });
        holder.setEditable(mEditable);
    }

    @Override
    public int getItemCount() {
        if (mAccountItem.getDetails() == null) {
            return 0;
        }
        return mAccountItem.getDetails().size();
    }

    public void setEditable(boolean editable) {
        mEditable = editable;
        for (ViewHolder viewHolder : mViewHolderList) {
            viewHolder.setEditable(editable);
        }
    }

    public void addItem(String name, String value) {
        int index = Account.addItemDetail(mAccountItem.getId(), name, value);
        if (index >= 0) {
            notifyItemInserted(index);
        }
    }

    public void removeItem(String itemId) {
        int index = Account.removeItemDetail(mAccountItem.getId(), itemId);
        if (index >= 0) {
            notifyItemRemoved(index);
            mViewHolderList.remove(index);
        }
    }

    public void modifyItem(String itemId, String name, String value) {
        int index = Account.modifyItemDetail(mAccountItem.getId(), itemId, name, value);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    public interface OnInteractionListener {

        void onItemDetailEditRequested(AccountItemDetail detail);

        void onItemDetailRemoveRequested(AccountItemDetail detail);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mValueView;
        public final ImageView mLogView;
        public final ImageButton mRemoveButton;
        public final ImageButton mEditButton;

        public ViewHolder(View view) {
            super(view);
            mNameView = (TextView) view.findViewById(R.id.id_name);
            mValueView = (TextView) view.findViewById(R.id.id_value);
            mLogView = (ImageView) view.findViewById(R.id.id_logImage);
            mRemoveButton = (ImageButton) view.findViewById(R.id.remove_button);
            mEditButton = (ImageButton) view.findViewById(R.id.edit_button);
        }

        public void setEditable(boolean editable) {
            if (editable) {
                mRemoveButton.setVisibility(View.VISIBLE);
                mEditButton.setVisibility(View.VISIBLE);
            } else {
                mRemoveButton.setVisibility(View.GONE);
                mEditButton.setVisibility(View.GONE);
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValueView.getText() + "'";
        }
    }
}
