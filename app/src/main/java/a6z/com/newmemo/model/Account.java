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

    private static final int COUNT = 25;

    static {
        /*
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i), i - 1);
        }*/
    }

    public static void saveToFile(Context context) {

        try {
            SerializationUtil.Serialize(context, ITEMS, "account.dat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFromFile(Context context) {

        List<AccountItem> dataList = null;

        try {
            dataList = (List<AccountItem>) SerializationUtil.Deserialze(context, "account.dat");
            ITEMS.clear();
            ITEM_MAP.clear();
            for (AccountItem data : dataList) {
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

    private static AccountItem createDummyItem(int position) {
        AccountItem item = new AccountItem(String.valueOf(position));
        item.setTitle("账号 " + position);
        item.setComment(makeComments(position));
        item.setUpdateTime(Calendar.getInstance());
        for (int i = 1; i <= 5; i++) {
            item.addDetail("我没意见" + i, "你呢?" + i);
        }
        return item;
    }

    private static String makeComments(int position) {
        return "银行 \r\n" + "网站 " + "生活";
    }

    /**
     * 帐号项目实体类.
     */
    public static class AccountItem implements Cloneable, Serializable {
        private static final long serialVersionUID = 2936649311758499194L;

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
        private static final long serialVersionUID = -2586682212525578973L;

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
}
