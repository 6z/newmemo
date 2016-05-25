package a6z.com.newmemo.Utils;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 字符串加密解密辅助类
 */
public class CryptoUtil {
    // 密钥

    private final static char[] HEX = "0123456789ABCDEF".toCharArray();

    private static String str2HexStr(String str, int maxLen) {
        if (maxLen != 32) {
            throw new IllegalArgumentException("maxLen");
        }
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int len = maxLen / 2;
        int bit;
        for (int i = 0; i < len; i++) {
            if (i < bs.length) {
                bit = (bs[i] & 0x0f0) >> 4;
                sb.append(HEX[bit]);
                bit = bs[i] & 0x0f;
                sb.append(HEX[bit]);
            } else {
                int over = i - bs.length;
                int first = over / 16;
                int second = over % 16;
                sb.append(HEX[first]);
                sb.append(HEX[second]);
            }
        }
        return sb.toString();
    }

    public static String encrypt(final String plainMessage, final String key) {
        final String symKeyHex = str2HexStr(key, 32);
        final byte[] symKeyData = Base64.decode(symKeyHex, Base64.DEFAULT);

        final byte[] encodedMessage = plainMessage.getBytes(Charset
                .forName("UTF-8"));
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int blockSize = cipher.getBlockSize();

            // create the key
            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

            // generate random IV using block size (possibly create a method for
            // this)
            final byte[] ivData = new byte[blockSize];
            final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            rnd.nextBytes(ivData);
            final IvParameterSpec iv = new IvParameterSpec(ivData);

            cipher.init(Cipher.ENCRYPT_MODE, symKey, iv);

            final byte[] encryptedMessage = cipher.doFinal(encodedMessage);

            // concatenate IV and encrypted message
            final byte[] ivAndEncryptedMessage = new byte[ivData.length
                    + encryptedMessage.length];
            System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
            System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage,
                    blockSize, encryptedMessage.length);

            final String ivAndEncryptedMessageBase64 = Base64.encodeToString(ivAndEncryptedMessage, Base64.DEFAULT);

            return ivAndEncryptedMessageBase64;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid AES key");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during encryption", e);
        }
    }

    public static String decrypt(final String ivAndEncryptedMessageBase64, final String key) {
        final String symKeyHex = str2HexStr(key, 32);
        final byte[] symKeyData = Base64.decode(symKeyHex, Base64.DEFAULT);

        final byte[] ivAndEncryptedMessage = Base64.decode(ivAndEncryptedMessageBase64, Base64.DEFAULT);
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int blockSize = cipher.getBlockSize();

            // create the key
            final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

            // retrieve random IV from start of the received message
            final byte[] ivData = new byte[blockSize];
            System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
            final IvParameterSpec iv = new IvParameterSpec(ivData);

            // retrieve the encrypted message itself
            final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length
                    - blockSize];
            System.arraycopy(ivAndEncryptedMessage, blockSize,
                    encryptedMessage, 0, encryptedMessage.length);

            cipher.init(Cipher.DECRYPT_MODE, symKey, iv);

            final byte[] encodedMessage = cipher.doFinal(encryptedMessage);

            // concatenate IV and encrypted message
            final String message = new String(encodedMessage,
                    Charset.forName("UTF-8"));

            return message;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(
                    "key argument does not contain a valid AES key");
        } catch (BadPaddingException e) {
            // you'd better know about padding oracle attacks
            return null;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Unexpected exception during decryption", e);
        }
    }
}