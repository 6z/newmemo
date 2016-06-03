package a6z.com.newmemo.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 帐号项目实体类.
 */
public class AccountItem implements Cloneable, Serializable, AccountChangedListener {
    private static final long serialVersionUID = 100;

    private AccountChangedListener listener;

    private String id;
    private String title;
    private String comment;
    private Calendar updateTime;
    private List<AccountItemDetail> details;
    private Map<String, AccountItemDetail> detailMap;
    private List<String> tags;

    public AccountItem(String id) {
        this.id = id;
        this.details = new ArrayList<>();
        this.detailMap = new HashMap<>();
        this.tags = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyModified();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        notifyModified();
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

    public List<AccountItemDetail> getDetails() {
        return details;
    }

    public int getDetailListSize() {
        return details.size();
    }

    public String[] getTags() {
        return tags.toArray(null);
    }

    public void attach(AccountChangedListener listener) {
        this.listener = listener;
    }

    public void detach() {
        this.listener = null;
    }

    public void clearDetails() {
        for (AccountItemDetail detail : details) {
            detail.detach();
        }
        this.details.clear();
        this.detailMap.clear();
        notifyModified();
    }

    public void addDetail(AccountItemDetail item) {
        this.details.add(item);
        this.detailMap.put(item.getId(), item);
        item.attach(this);
        notifyModified();
    }

    public void addDetail(String key, String value) {
        AccountItemDetail item = AccountItemDetail.create(key, value);
        addDetail(item);
        item.attach(this);
        notifyModified();
    }

    public int removeDetail(String id) {
        AccountItemDetail item = this.detailMap.remove(id);
        if (item != null) {
            item.detach();
            int index = details.indexOf(item);
            this.details.remove(item);
            notifyModified();
            return index;
        }
        return -1;
    }

    public int modifyDetail(String id, String name, String value) {
        AccountItemDetail item = this.detailMap.get(id);
        if (item != null) {
            int index = details.indexOf(item);
            item.setName(name);
            item.setValue(value);
            return index;
        }
        return -1;
    }

    public void addTag(String tag) {
        tags.add(tag);
        notifyModified();
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
        if (details != null) {
            for (AccountItemDetail detail : details) {
                newObj.addDetail((AccountItemDetail) detail.clone());
            }
        }
        if (tags != null) {
            for (String tag : tags) {
                newObj.addTag(tag);
            }
        }
        //最好设置更新时间
        newObj.setUpdateTime(getUpdateTime());
        return newObj;
    }

    private void notifyModified() {
        if (listener != null) {
            listener.onAccountChanged();
        }
    }

    @Override
    public void onAccountChanged() {
        notifyModified();
    }
}

