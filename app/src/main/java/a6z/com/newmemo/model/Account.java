package a6z.com.newmemo.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Account {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<AccountItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, AccountItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(AccountItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static AccountItem createDummyItem(int position) {
        AccountItem item = new AccountItem(String.valueOf(position));
        item.setTitle("账号 " + position);
        item.setComment(makeComments(position));
        item.setUpdateTime(Calendar.getInstance());
        for (int i = 1; i <= 5; i++) {
            item.addDetail("key_" + i, "value_" + i);
        }
        return item;
    }

    private static String makeComments(int position) {
        return "银行 " + "网站 " + "生活";
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class AccountItem {
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

        public void addDetail(String key, String value) {
            AccountDetail item = AccountDetail.Create(key, value);
            this.details.add(item);
            this.detailMap.put(item.getId(), item);
        }

        public void removeDetail(String id) {
            AccountDetail item = this.detailMap.remove(id);
            this.details.remove(item);
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public static class AccountDetail {
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

        public String getValue() {
            return value;
        }
    }
}
