package a6z.com.newmemo.model;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

public class AccountImport extends XMLHandler {

    //private List<ItemCategory> data;

    public AccountImport(File dir, String fileName) {
        super(dir, fileName);
        //dir.mkdirs();
    }

    @Override
    protected void parse(String xml) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        String curCategory = null;
        Account.AccountItem curItem = null;
        Account.AccountDetail curItemData = null;

        Boolean readDataText = false;
        StringReader stream = new StringReader(xml);

        parser.setInput(stream);
        int eventType = parser.getEventType();
        while (XmlPullParser.END_DOCUMENT != eventType) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                /*if (null == data) {
                    data = new ArrayList<ItemCategory>();
				} else {
					data.clear();
				}*/
                    break;
                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    if ("category".equals(name)) {
                        curCategory = parser.getAttributeValue(null, "caption");
                    } else if ("item".equals(name)) {
                        if (null != curCategory) {
                            curItem = Account.createItem(parser.getAttributeValue(null, "caption"), "", null);
                            //new Account.AccountItem(parser.getAttributeValue(null, "id").replaceAll("-", ""));
                            //curItem.setId(parser.getAttributeValue(null, "id"));
                            //curItem.setTitle(parser.getAttributeValue(null, "caption"));
                            curItem.addTag(curCategory);
                            Account.addItem(curItem, -1);
                            //curCategory.AddItem(curItem);
                        }
                    } else if ("data".equals(name)) {
                        if (null != curItem) {
                            curItemData = Account.AccountDetail.create(parser.getAttributeValue(null, "caption"), "");
                            readDataText = true;
                            curItem.addDetail(curItemData);
                        }
                    }
                    break;
                case XmlPullParser.TEXT:
                    if (null != curItemData && readDataText) {
                        readDataText = false;
                        String text = parser.getText();
                        if (null == text) {
                            text = "";
                        }
                        while (text.startsWith("\r") || text.startsWith("\n")
                                || text.startsWith("\t")) {
                            text = text.substring(1);
                        }
                        while (text.endsWith("\r") || text.endsWith("\n")
                                || text.endsWith("\t")) {
                            text = text.substring(0, text.length() - 1);
                        }
                        curItemData.setValue(text);
                    }
                    break;
            }
            eventType = parser.next();
        }

    }

    @Override
    protected String buildXML() throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
/*
        serializer.setOutput(stringWriter);
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "account");
		for (ItemCategory category : data) {
			serializer.startTag(null, "category");
			serializer.attribute(null, "id", category.getId());
			serializer.attribute(null, "caption", category.getCaption());
			for (Item item : category.getItems()) {
				serializer.startTag(null, "item");
				serializer.attribute(null, "id", item.getId());
				serializer.attribute(null, "caption", item.getCaption());
				for (ItemData data : item.getAllData()) {
					serializer.startTag(null, "data");
					serializer.attribute(null, "id", data.getKey());
					serializer.attribute(null, "caption", data.getCaption());
					serializer.text(data.getValue());
					serializer.endTag(null, "data");
				}
				serializer.endTag(null, "item");
			}
			serializer.endTag(null, "category");
		}
		serializer.endTag(null, "account");
		serializer.endDocument();
*/
        return stringWriter.toString();
    }

}
