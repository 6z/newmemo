package a6z.com.newmemo.model;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    static {

    }

    public static void saveToFile(Context context, boolean toEnCrypt) {
        AccountPersistent persistent = new AccountPersistent();
        //persistent.setSecretKey(CRYPT_KEY);
        try {
            if (toEnCrypt) {
                persistent.setItems(new ArrayList<AccountItem>());
                for (AccountItem originData : ITEMS) {
                    AccountItem newData = (AccountItem) originData.clone();
                    for (AccountDetail detail : newData.getDetails()) {
                        detail.setValue(CryptoUtil.encrypt(detail.getValue(), persistent.getSecretKey()));
                    }
                    persistent.getItems().add(newData);
                }
            } else {
                persistent.setItems(ITEMS);
            }
            SerializationUtil.Serialize(context, persistent, "account.dat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFromFile(Context context, boolean toDecrypt) {

        try {
            AccountPersistent persistent = (AccountPersistent) SerializationUtil.Deserialze(context, "account.dat");
            ITEMS.clear();
            ITEM_MAP.clear();
            if (persistent == null) {
                return;
            }
            for (AccountItem data : persistent.getItems()) {
                for (AccountDetail detail : data.getDetails()) {
                    if (toDecrypt) {
                        detail.setValue(CryptoUtil.decrypt(detail.getValue(), persistent.getSecretKey()));
                    }
                }
                addItem(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addItem(AccountItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    public static void addItem(AccountItem item, int position) {
        ITEMS.add(position, item);
        ITEM_MAP.put(item.getId(), item);
    }

    public static int modifyItem(String id, String title, String comment) {
        AccountItem item = ITEM_MAP.get(id);
        if (item != null) {
            int index = ITEMS.indexOf(item);
            item.setTitle(title);
            item.setComment(comment);
            return index;
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
            int index = ITEMS.indexOf(accountItem);
            return index;
        }
        return -1;
    }

    public static AccountItem createItem(String title, String comments, List<AccountDetail> details) {
        AccountItem item = new AccountItem(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        item.setTitle(title);
        item.setComment(comments);
        item.clearDetails();
        if (details != null) {
            for (AccountDetail detailItem : details) {
                item.addDetail(detailItem);
            }
        }
        item.setUpdateTime(Calendar.getInstance());
        return item;
    }

    /**
     * 帐号项目实体类.
     */
    public static class AccountItem implements Cloneable, Serializable {
        private static final long serialVersionUID = 100;

        private String id;
        private String title;
        private String comment;
        private Calendar updateTime;
        private List<AccountDetail> details;
        private Map<String, AccountDetail> detailMap;

        public AccountItem(String id) {
            this.id = id;
            this.details = new ArrayList<>();
            this.detailMap = new HashMap<>();
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Calendar getUpdateTime() {

            return updateTime;
        }

        public void setUpdateTime(Calendar updateTime) {
            this.updateTime = updateTime;
        }

        public String getFormattedUpdateTime(String pattern) {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
            return format.format(this.updateTime.getTime());
        }

        public List<AccountDetail> getDetails() {
            return details;
        }

        public int getDetailListSize() {
            return details.size();
        }

        public void clearDetails() {
            this.details.clear();
            this.detailMap.clear();
        }

        public void addDetail(AccountDetail item) {
            this.details.add(item);
            this.detailMap.put(item.getId(), item);
        }

        public void addDetail(String key, String value) {
            AccountDetail item = AccountDetail.Create(key, value);
            addDetail(item);
        }

        public int removeDetail(String id) {
            AccountDetail item = this.detailMap.remove(id);
            if (item != null) {
                int index = details.indexOf(item);
                this.details.remove(item);
                return index;
            }
            return -1;
        }

        public int modifyDetail(String id, String name, String value) {
            AccountDetail item = this.detailMap.get(id);
            if (item != null) {
                int index = details.indexOf(item);
                item.setName(name);
                item.setValue(value);
                return index;
            }
            return -1;
        }

        @Override
        public String toString() {
            return title;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            AccountItem newObj = new AccountItem(getId());
            newObj.setTitle(getTitle());
            newObj.setComment(getComment());
            newObj.setUpdateTime(getUpdateTime());
            for (AccountDetail detail : getDetails()) {
                newObj.addDetail((AccountDetail) detail.clone());
            }
            return newObj;
        }
    }

    /**
     * 帐号详情实体类
     */
    public static class AccountDetail implements Cloneable, Serializable {
        private static final long serialVersionUID = 100;

        private String id;
        private String name;
        private String value;

        private AccountDetail(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public static AccountDetail Create(String key, String value) {
            AccountDetail item = new AccountDetail(key, value);
            item.id = java.util.UUID.randomUUID().toString().replaceAll("-", "");
            return item;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

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
