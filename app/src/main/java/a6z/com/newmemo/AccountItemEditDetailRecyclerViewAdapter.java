package a6z.com.newmemo;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import a6z.com.newmemo.model.Account;

/**
 * 自定义账号详细里的列表适配器
 */
public class AccountItemEditDetailRecyclerViewAdapter extends RecyclerView.Adapter<AccountItemEditDetailRecyclerViewAdapter.ViewHolder> {

    private final List<Account.AccountDetail> items;

    public AccountItemEditDetailRecyclerViewAdapter(List<Account.AccountDetail> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_account_item_detail_edit_tpl, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNameView.setText(items.get(position).getName());
        holder.mValueView.setText(items.get(position).getValue());
        holder.mLogView.setImageBitmap(BitmapDrawer.getFilledRect(40, 40,
                ContextCompat.getColor(holder.mRootView.getContext(), R.color.colorItemLittleLogo)));
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mRootView;
        public final EditText mNameView;
        public final EditText mValueView;
        public final ImageView mLogView;

        public ViewHolder(View view) {
            super(view);
            mRootView = view;
            mNameView = (EditText) view.findViewById(R.id.id_name);
            mValueView = (EditText) view.findViewById(R.id.id_value);
            mLogView = (ImageView) view.findViewById(R.id.id_logImage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValueView.getText() + "'";
        }
    }
}
