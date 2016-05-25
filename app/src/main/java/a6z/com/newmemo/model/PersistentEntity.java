package a6z.com.newmemo.model;

/**
 * 持久化实体基类.
 */
public class PersistentEntity {

    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
