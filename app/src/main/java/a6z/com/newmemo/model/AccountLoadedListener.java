package a6z.com.newmemo.model;

/**
 * 监听帐号加载后的接口
 */
public interface AccountLoadedListener {
    void onSuccessful();

    void onFailure(String msg);
}
