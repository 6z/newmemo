package a6z.com.newmemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.newzhi.customview.IndexView;

import java.util.HashMap;
import java.util.Map;

import a6z.com.newmemo.control.DividerItemDecoration;
import a6z.com.newmemo.model.Account;
import a6z.com.newmemo.model.AccountImport;
import a6z.com.newmemo.model.AccountItem;
import a6z.com.newmemo.model.AccountLoadedListener;
import a6z.com.newmemo.model.AccountSavedListener;

/**
 * 帐号列表 Fragment
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class AccountListViewFragment extends Fragment {

    private final AccountLoadedListener accountLoadedListener = new AccountLoadedListener() {
        @Override
        public void onSuccessful() {
            Toast.makeText(getContext(), "帐号加载成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(String msg) {
            Toast.makeText(getContext(), "帐号加载失败:" + msg, Toast.LENGTH_LONG).show();
        }
    };
    private final AccountSavedListener accountSavedListener = new AccountSavedListener() {
        @Override
        public void onSuccessful() {
            Toast.makeText(getContext(), "帐号保存成功", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailure(String msg) {
            Toast.makeText(getContext(), "帐号保存失败:" + msg, Toast.LENGTH_LONG).show();
        }
    };
    private final Map<String, Integer> letterIndexes = new HashMap<>();
    private int mColumnCount = 1;
    private OnFragmentInteractionListener mListener;
    private TextView mEmptyTipsView;
    private RecyclerView mRecyclerView;
    private IndexView mIndexView;
    private boolean mContinousMoving;
    private int mToTopIndex;

    public AccountListViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Account.init(getContext(), accountSavedListener, accountLoadedListener);

    }

    private void importOld() {
        //String dataDirectory = Environment.getExternalStorageDirectory().getPath() + "/mynotes";
        AccountImport _import = new AccountImport(getContext().getFilesDir(), "myNotes.xml");
        try {
            Account.setCachedMode(true);
            if (_import.load()) {
                Account.setCachedMode(false);
                Account.beginSave();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "导入数据失败", Toast.LENGTH_SHORT).show();
        } finally {
            Account.setCachedMode(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_account_list, container, false);

        mIndexView = (IndexView) view.findViewById(R.id.index_view);
        TextView selectIndexView = (TextView) view.findViewById(R.id.select_index_view);
        mIndexView.setTextViewDialog(selectIndexView);

        mIndexView.setOnIndexChangeListener(new IndexView.OnIndexChangeListener() {
            @Override
            public void onChange(String indexString) {
                Integer index = letterIndexes.get(indexString);
                if (index != null) {
                    scrollToPosition(index);
                }
            }
        });

        mEmptyTipsView = (TextView) view.findViewById(R.id.emptyTips);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_account_list);

        Account.beginLoad();

        //importOld();

        checkViewMode();

        // Set the adapter
        if (mRecyclerView != null) {
            fullIndex();
            Context context = view.getContext();
            final RecyclerView.LayoutManager layoutManager;
            if (mColumnCount <= 1) {
                layoutManager = new LinearLayoutManager(context);
            } else {
                layoutManager = new GridLayoutManager(context, mColumnCount);
            }
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (mContinousMoving && mColumnCount <= 1) {
                        mContinousMoving = false;
                        //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                        int n = mToTopIndex - ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        if (0 <= n && n < mRecyclerView.getChildCount()) {
                            //获取要置顶的项顶部离RecyclerView顶部的距离
                            int top = mRecyclerView.getChildAt(n).getTop();
                            //最后的移动
                            mRecyclerView.smoothScrollBy(0, top);
                        }
                    }
                }
            });
            mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(new AccountListViewItemRecyclerViewAdapter(Account.ITEMS, mListener));
        }

        /*FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onAddItemRequest();
                    }
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }*/

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
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        //Account.saveToFile(getContext(), true);
        super.onDestroy();
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

    private void fullIndex() {
        letterIndexes.clear();
        letterIndexes.put("#", 0);
        int startIndex = 0;
        for (String indexStr : mIndexView.getIndexStringArray()) {
            for (int index = startIndex; index < Account.ITEMS.size(); index++) {
                char itemChar = Account.ITEMS.get(index).getAlphabetOfTitle().toUpperCase().charAt(0);
                char indexChar = indexStr.toUpperCase().charAt(0);
                if (itemChar == indexChar) {
                    if (!letterIndexes.containsKey(indexStr)) {
                        letterIndexes.put(indexStr, index);
                        startIndex = index;
                        break;
                    }
                } else if (itemChar > indexChar) {
                    startIndex = index;
                    break;
                }
            }
        }
    }

    private void scrollToPosition(int position) {
        if (mColumnCount <= 1) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
            int firstItem = layoutManager.findFirstVisibleItemPosition();
            int lastItem = layoutManager.findLastVisibleItemPosition();
            //然后区分情况
            if (position <= firstItem) {
                //当要置顶的项在当前显示的第一个项的前面时
                mRecyclerView.smoothScrollToPosition(position);
            } else if (position <= lastItem) {
                //当要置顶的项已经在屏幕上显示时
                int top = mRecyclerView.getChildAt(position - firstItem).getTop();
                mRecyclerView.scrollBy(0, top);
            } else {
                //当要置顶的项在当前显示的最后一项的后面时
                mRecyclerView.smoothScrollToPosition(position);
                //这里这个变量是用在RecyclerView滚动监听里面的
                mToTopIndex = position;
                mContinousMoving = true;
            }
        } else {
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    public void addItem(AccountItem item) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.id_account_list);
        if (recyclerView != null) {
            int insertPosition = 0;
            insertPosition = ((AccountListViewItemRecyclerViewAdapter) recyclerView.getAdapter()).add(item, insertPosition);
            recyclerView.scrollToPosition(insertPosition);
            fullIndex();
        }
        checkViewMode();
    }

    public void removeItem(AccountItem item) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.id_account_list);
        if (recyclerView != null) {
            ((AccountListViewItemRecyclerViewAdapter) recyclerView.getAdapter()).remove(item);
            fullIndex();
        }
        checkViewMode();
    }

    public void removeItem(String itemId) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.id_account_list);
        if (recyclerView != null) {
            ((AccountListViewItemRecyclerViewAdapter) recyclerView.getAdapter()).remove(itemId);
            fullIndex();
        }
        checkViewMode();
    }

    public void notifyItemChanged(String itemId) {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.id_account_list);
        if (recyclerView != null) {
            int pos = ((AccountListViewItemRecyclerViewAdapter) recyclerView.getAdapter()).notifyItemChanged(itemId);
            recyclerView.scrollToPosition(pos);
            fullIndex();
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
