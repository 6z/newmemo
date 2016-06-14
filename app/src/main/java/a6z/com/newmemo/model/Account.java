package a6z.com.newmemo.model;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a6z.com.newmemo.Utils.CryptoUtil;
import a6z.com.newmemo.Utils.SerializationUtil;

/**
 * 账号数据模型.
 */
public class Account {
    /**
     * An array of account items.
     */
    public static final List<AccountItem> ITEMS = new ArrayList<>();

    /**
     * A map of account items, by ID.
     */
    public static final Map<String, AccountItem> ITEM_MAP = new HashMap<>();

    private static final String FILE_NAME = "account.dat";

    private static Context context = null;

    private static boolean cachedMode = false;
    private static AccountSavedListener savedListener;
    private final static AccountChangedListener ACCOUNT_CHANGED_LISTENER = new AccountChangedListener() {
        @Override
        public void onAccountChanged() {
            beginSave();
        }
    };
    private static AccountLoadedListener loadedListener;

    public static void setCachedMode(boolean cachedMode) {
        Account.cachedMode = cachedMode;
    }

    public static void beginSave() {
        if (cachedMode) {
            return;
        }
        try {
            cachedMode = true;
            saveToFile(true);
            if (savedListener != null) {
                savedListener.onSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (savedListener != null) {
                savedListener.onFailure(e.getMessage());
            }
        } finally {
            cachedMode = false;
        }
    }

    public static void beginLoad() {
        if (cachedMode) {
            return;
        }
        try {
            cachedMode = true;
            readFromFile(true);
            if (loadedListener != null) {
                loadedListener.onSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (loadedListener != null) {
                loadedListener.onFailure(e.getMessage());
            }
        } finally {
            cachedMode = false;
        }
    }

    public static void reset() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void init(Context context, AccountSavedListener savedListener, AccountLoadedListener loadedListener) {
        Account.context = context;
        Account.savedListener = savedListener;
        Account.loadedListener = loadedListener;
    }

    private static void saveToFile(boolean toEnCrypt) throws Exception {
        if (context == null) {
            throw new IllegalStateException("init must be called");
        }
        AccountPersistent persistent = new AccountPersistent();

        if (toEnCrypt) {
            persistent.setItems(new ArrayList<AccountItem>());
            for (AccountItem originData : ITEMS) {
                AccountItem newData = (AccountItem) originData.clone();
                for (AccountItemDetail detail : newData.getDetails()) {
                    detail.setValue(CryptoUtil.encrypt(detail.getValue(), persistent.getSecretKey()));
                }
                persistent.getItems().add(newData);
            }
        } else {
            persistent.setItems(ITEMS);
        }
        SerializationUtil.Serialize(context, persistent, FILE_NAME);
    }

    private static void readFromFile(boolean toDecrypt) throws Exception {

        if (context == null) {
            throw new IllegalStateException("init must be called");
        }

        reset();

        AccountPersistent persistent = (AccountPersistent) SerializationUtil.Deserialze(context, FILE_NAME);
        if (persistent == null) {
            return;
        }
        for (AccountItem data : persistent.getItems()) {
            for (AccountItemDetail detail : data.getDetails()) {
                if (toDecrypt) {
                    detail.setValue(CryptoUtil.decrypt(detail.getValue(), persistent.getSecretKey()));
                }
            }
            addItem(data);
        }

        sortByTitle();
    }

    private static void sortByTitle() {
        Collections.sort(ITEMS, new AccountSort());
    }

    private static void addItem(AccountItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
        item.attach(ACCOUNT_CHANGED_LISTENER);
        beginSave();
    }

    public static int addItem(AccountItem item, int position) {
        if (position < 0) {
            ITEMS.add(item);
        } else {
            ITEMS.add(position, item);
        }
        if (!cachedMode) {
            sortByTitle();
        }
        ITEM_MAP.put(item.getId(), item);
        item.attach(ACCOUNT_CHANGED_LISTENER);
        beginSave();
        return ITEMS.indexOf(item);
    }

    public static int modifyItem(String id, String title, String comment) {
        AccountItem item = ITEM_MAP.get(id);
        if (item != null) {
            int index = ITEMS.indexOf(item);
            item.setTitle(title);
            item.setComment(comment);
            if (!cachedMode) {
                sortByTitle();
            }
            return ITEMS.indexOf(item);
        }
        return -1;
    }

    public static int removeItem(AccountItem item) {
        return removeItem(item.getId());
    }

    public static int removeItem(String itemId) {
        AccountItem accountItem = ITEM_MAP.remove(itemId);
        if (accountItem != null) {
            int index = ITEMS.indexOf(accountItem);
            ITEMS.remove(accountItem);
            accountItem.detach();
            beginSave();
            return index;
        }
        return -1;
    }

    public static int addItemDetail(String itemId, String detailName, String detailValue) {
        AccountItem accountItem = ITEM_MAP.get(itemId);
        if (accountItem != null) {
            accountItem.addDetail(detailName, detailValue);
            return accountItem.getDetailListSize() - 1;
        }
        return -1;
    }

    public static int removeItemDetail(String itemId, String detailId) {
        AccountItem accountItem = ITEM_MAP.get(itemId);
        if (accountItem != null) {
            return accountItem.removeDetail(detailId);
        }
        return -1;
    }

    public static int modifyItemDetail(String itemId, String detailId, String name, String value) {
        AccountItem accountItem = ITEM_MAP.get(itemId);
        if (accountItem != null) {
            return accountItem.modifyDetail(detailId, name, value);
        }
        return -1;
    }

    public static int indexOf(String itemId) {
        AccountItem accountItem = ITEM_MAP.get(itemId);
        if (accountItem != null) {
            return ITEMS.indexOf(accountItem);
        }
        return -1;
    }

    public static AccountItem createItem(String title, String comments, List<AccountItemDetail> details) {
        AccountItem item = new AccountItem(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        item.setTitle(title);
        item.setComment(comments);
        item.clearDetails();
        if (details != null) {
            for (AccountItemDetail detailItem : details) {
                item.addDetail(detailItem);
            }
        }
        item.setUpdateTime(Calendar.getInstance());
        return item;
    }

    private static class AccountSort implements Comparator<AccountItem> {
        @Override
        public int compare(AccountItem lhs, AccountItem rhs) {
            return lhs.getAlphabetOfTitle().compareToIgnoreCase(rhs.getAlphabetOfTitle());
        }
    }

    /**
     * 用于持久化的帐号实体
     */
    private static class AccountPersistent extends PersistentEntity implements Serializable {

        private static final long serialVersionUID = 100;

        private final static String CRYPT_KEY = "nEwZhI@0510.2O16";

        private List<AccountItem> Items;

        @Override
        public String getSecretKey() {
            return CRYPT_KEY;
        }

        @Override
        public void setSecretKey(String secretKey) {
            super.setSecretKey(CRYPT_KEY);
        }

        public List<AccountItem> getItems() {
            return Items;
        }

        public void setItems(List<AccountItem> items) {
            Items = items;
        }
    }
}
