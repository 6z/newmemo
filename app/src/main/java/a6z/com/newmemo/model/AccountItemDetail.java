package a6z.com.newmemo.model;

import java.io.Serializable;

/**
 * 帐号项明细实体类
 */
public class AccountItemDetail implements Cloneable, Serializable {
    private static final long serialVersionUID = 100;

    private AccountChangedListener listener;
    private String id;
    private String name;
    private String value;
    private boolean isSecurity;

    private AccountItemDetail(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static AccountItemDetail create(String key, String value) {
        AccountItemDetail item = new AccountItemDetail(key, value);
        item.id = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        return item;
    }

    public void attach(AccountChangedListener listener) {
        this.listener = listener;
    }

    public void detach() {
        this.listener = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
        notifyModified();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        notifyModified();
    }

    public boolean isSecurity() {
        return isSecurity;
    }

    public void setSecurity(boolean security) {
        isSecurity = security;
        notifyModified();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AccountItemDetail newObj = (AccountItemDetail) super.clone();
        //newObj.listener = null;
        return newObj;
    }

    private void notifyModified() {
        if (listener == null) {
            return;
        }
        listener.onAccountChanged();
    }
}

