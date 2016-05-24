package a6z.com.newmemo.Utils;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象序列化和反序列化,并保存到文件的辅助类
 */
public class SerializationUtil {
    private static final String cryptoSeed = "2016";

    public static void Serialize(Context context, Object obj, String filename) throws Exception {
           /*
    * 1、根据上下文对象能快速得到一个文件输出流对象；
    * 2、私有操作模式：创建出来的文件只能被本应用访问，其他应用无法访问该文件：Context.MODE_PRIVATE；
    * 另外采用私有操作模式创建的文件，写入的内容会覆盖原文件的内容。
    * 3、openFileOutput()方法的第一个参数用于指定文件名称，不能包含路径分隔符"/"，如果文件不存在，
    * Android会自动创建它，创建的文件保存在/data/data/<package name>/files目录，如/data/data/org.example.files/files.
    */
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //构造一个字节输出流
        ObjectOutputStream oos = new ObjectOutputStream(baos); //构造一个类输出流
        oos.writeObject(obj); //写这个对象
        byte[] buf = baos.toByteArray(); //从这个地层字节流中把传输的数组给一个新的数组
        oos.flush();
        oos.close();

        buf = CryptoUtil.encrypt(cryptoSeed, buf);

        FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        //把字符串传化为二进制数据写入到文件中
        outStream.write(buf);
        //然后关掉这个流
        outStream.close();
    }

    public static Object Deserialze(Context context, String filename) throws Exception {

           /*
    * 1、从上下文对象中得到一个文件输入流对像，context.openFileInput(filename)得到文件输入流对象；
    * 2、
    */
        FileInputStream inStream = context.openFileInput(filename);
        //把每次读到的数据都存放在内存中
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //定义数组大小
        byte[] buffer = new byte[1024];
        int len;
        //读取这个输入流数组,判断数据是否读完
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        //从内存中获取得到的数据
        byte[] data = outStream.toByteArray();

        data = CryptoUtil.decrypt(cryptoSeed, data);

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

}
