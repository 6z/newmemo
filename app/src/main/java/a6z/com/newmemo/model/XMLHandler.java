package a6z.com.newmemo.model;

import java.io.File;
import java.util.Calendar;

public abstract class XMLHandler {
    protected static final String BACKUP_DIR = "backup";
    private static final String KEY = "2013Newzhi0510";
    protected boolean backupNeeded = false;
    protected File backupFileDir;
    protected File fileDir;
    protected String xmlFileName;

    public XMLHandler(File dir, String fileName) {
        xmlFileName = fileName;
        fileDir = dir;
        //backupFileDir = new File(dir.getAbsolutePath() + "/" + BACKUP_DIR);

    }

    protected abstract void parse(String xml) throws Exception;

    protected abstract String buildXML() throws Exception;

    public void save() throws Exception {
        String strXML = buildXML();
        saveString(strXML);
    }

    public void load() throws Exception {
        /*
         * File xmlFile = new File(fileDir, xmlFileName); if (!xmlFile.exists())
		 * { return; } xmlFile.setReadable(true); FileInputStream fileStream =
		 * new FileInputStream(xmlFile); BufferedInputStream stream = new
		 * BufferedInputStream(fileStream); byte[] buffer = new byte[(int)
		 * xmlFile.length()]; stream.read(buffer); String decryText =
		 * SimpleCrypto.decrypt(KEY, new String(buffer)); parse(decryText);
		 * stream.close(); backupNeeded = true;
		 */
        String content = loadString();
        if (content == "") {
            return;
        }
        parse(content);
    }

    public String loadString() throws Exception {
        byte[] fileContent = FileUtils.readFile(new File(fileDir, xmlFileName));
        if (fileContent == null) {
            return "";
        } else {
            String decryText = new String(fileContent);// new String(SimpleCrypto.decode(fileContent));
            parse(decryText);
            backupNeeded = true;
            return decryText;
        }
        /*
         * File xmlFile = new File(fileDir, xmlFileName); if (!xmlFile.exists())
		 * { return ""; } xmlFile.setReadable(true); FileInputStream fileStream
		 * = new FileInputStream(xmlFile); BufferedInputStream stream = new
		 * BufferedInputStream(fileStream); byte[] buffer = new byte[(int)
		 * xmlFile.length()]; stream.read(buffer); String decryText = new
		 * String(SimpleCrypto.decode(buffer)); parse(decryText);
		 * stream.close(); backupNeeded = true; return decryText;
		 */
    }

	/*
     * public String loadString(String fileName) throws IOException { File
	 * xmlFile = new File(fileDir, fileName); if (!xmlFile.exists()) { return
	 * ""; } xmlFile.setReadable(true); FileInputStream fileStream = new
	 * FileInputStream(xmlFile); BufferedInputStream stream = new
	 * BufferedInputStream(fileStream); byte[] buffer = new byte[(int)
	 * xmlFile.length()]; stream.read(buffer); String text = new String(buffer);
	 * stream.close(); return text; }
	 */

    public void saveString(String strXML) throws Exception {
        if (backupNeeded) {
            backup();
        }
        //byte[] encryBytes = SimpleCrypto.encode(strXML.getBytes());
        //FileUtils.saveFile(new File(fileDir, xmlFileName), encryBytes);
    }

	/*
     * public void saveString(String strXML, String fileName) throws IOException
	 * { File xmlFile = new File(fileDir, fileName); if (!xmlFile.exists()) {
	 * xmlFile.createNewFile(); } xmlFile.setWritable(true); FileOutputStream
	 * outStream = new FileOutputStream(xmlFile); OutputStreamWriter
	 * streamWriter = new OutputStreamWriter(outStream); BufferedWriter
	 * fileWriter = new BufferedWriter(streamWriter); try {
	 * fileWriter.write(strXML); } catch (Exception e) {
	 * e.printStackTrace(); } fileWriter.flush(); fileWriter.close(); }
	 */

    private void backup() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int date = now.get(Calendar.DATE);
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int seconds = now.get(Calendar.SECOND);
        int milliseconds = now.get(Calendar.MILLISECOND);
        String backupFileName = "account_" + year + "_" + month + "_" + date
                + "_" + hours + "_" + minutes + "_" + seconds + "_"
                + milliseconds + ".bak";
        File sourceFile = new File(fileDir, xmlFileName);
        File destFile = new File(backupFileDir, backupFileName);
        FileUtils.copyFile(sourceFile, destFile, true);
        backupNeeded = false;
    }
}
