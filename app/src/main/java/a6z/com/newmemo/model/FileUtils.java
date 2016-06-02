package a6z.com.newmemo.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtils {
    public static void copyFile(File fromFile, File toFile, boolean rewrite) {
        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try {
            FileInputStream fosFrom = new FileInputStream(fromFile);
            FileOutputStream fosTo = new FileOutputStream(toFile);

            BufferedInputStream inStream = new BufferedInputStream(fosFrom);
            byte[] buffer = new byte[(int) fromFile.length()];
            inStream.read(buffer);

            fosTo.write(buffer, 0, buffer.length);

            fosFrom.close();
            fosTo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,
                        null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static byte[] readFile(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        file.setReadable(true);
        FileInputStream fileStream = new FileInputStream(file);
        BufferedInputStream stream = new BufferedInputStream(fileStream);
        byte[] buffer = new byte[(int) file.length()];
        stream.read(buffer);
        stream.close();
        return buffer;
    }

    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static void saveFile(File file, byte[] content) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        file.setWritable(true);
        FileOutputStream outStream = new FileOutputStream(file);
        OutputStreamWriter streamWriter = new OutputStreamWriter(outStream);
        BufferedWriter fileWriter = new BufferedWriter(streamWriter);

        fileWriter.write(new String(content));

        fileWriter.flush();
        fileWriter.close();
    }

    public static void saveFile(String file, byte[] content) throws IOException {
        saveFile(new File(file), content);
    }
}
