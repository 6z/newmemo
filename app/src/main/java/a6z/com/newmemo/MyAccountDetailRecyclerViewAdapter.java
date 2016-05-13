package a6z.com.newmemo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import a6z.com.newmemo.model.Account;

/**
 * 自定义账号详细里的列表适配器
 */
public class MyAccountDetailRecyclerViewAdapter extends RecyclerView.Adapter<MyAccountDetailRecyclerViewAdapter.ViewHolder> {

    private final List<Account.AccountDetail> items;

    public MyAccountDetailRecyclerViewAdapter(List<Account.AccountDetail> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frame_account_detail_item_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNameView.setText(items.get(position).getName());
        holder.mValueView.setText(items.get(position).getValue());
        //Drawable bitmap =holder.mRootView.getContext().getResources().getDrawable(R.drawable.ic_menu_camera,null);
        //holder.mLogView.setImageDrawable(bitmap);
        holder.mLogView.setImageBitmap(BitmapDrawer.getFilledRect(40, 40, Color.argb(255, 153, 153, 153)));
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
        public final TextView mNameView;
        public final TextView mValueView;
        public final ImageView mLogView;

        public ViewHolder(View view) {
            super(view);
            mRootView = view;
            mNameView = (TextView) view.findViewById(R.id.id_name);
            mValueView = (TextView) view.findViewById(R.id.id_value);
            mLogView = (ImageView) view.findViewById(R.id.id_logImage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValueView.getText() + "'";
        }
    }
}
