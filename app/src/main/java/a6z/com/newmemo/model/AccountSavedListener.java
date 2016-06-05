package a6z.com.newmemo.model;

/**
 * 监听帐号保存后的接口
 */
public interface AccountSavedListener {
    void onSuccessful();

    void onFailure(String msg);
}
