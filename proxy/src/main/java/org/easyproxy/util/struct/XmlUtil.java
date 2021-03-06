package org.easyproxy.util.struct;/**
 * Description : 
 * Created by YangZH on 16-8-14
 *  下午11:08
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static org.easyproxy.constants.Const.*;
/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

public class XmlUtil {

    private static SAXReader reader = new SAXReader();
    private Document document = null;
    private Element root = null;
    private JSONObject object = new JSONObject();

    public XmlUtil(InputStream is) {
        try {
            document = reader.read(is);
            root = document.getRootElement();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XmlUtil(String xml_path) {
        try {
            InputStream is = new FileInputStream(xml_path);
            document = reader.read(is);
            root = document.getRootElement();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Iterator<Element> elementIterator = root.elementIterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            switch (element.getName()) {
                case PROXY_PASS:
                    JSONArray array = new JSONArray();
                    Iterator<Element> eleIt = element.elementIterator();
                    while (eleIt.hasNext()) {
                        Map<String, String> kv = new HashMap<String, String>();
                        Element ele = eleIt.next();
                        kv.put(PORT, ele.attributeValue(PORT));
                        kv.put(HOST, ele.attributeValue(HOST));
                        int weight = 0;
                        try{
                            weight = Integer.parseInt(ele.attributeValue(WEIGHT));
                        }catch (Exception e){
                            weight = 1;
                        }
                        if (weight<=0)
                            weight = 1;
                        kv.put(WEIGHT, String.valueOf(weight));
                        array.add(kv);
                    }
                    object.put(PROXY_PASS, array);
                    break;
                case CACHE_URL:
                    JSONArray arr = new JSONArray();
                    Iterator<Element> eleItr = element.elementIterator();
                    while (eleItr.hasNext()) {
                        Map<String, String> kv = new HashMap<String, String>();
                        Element ele = eleItr.next();
                        kv.put(URL, ele.attributeValue(URL));
                        kv.put(METHOD, ele.attributeValue(METHOD));
                        arr.add(kv);
                    }
                    object.put(CACHE_URL, arr);
                    break;
                case IP_FILTER:
                    JSONArray arr1 = new JSONArray();
                    Iterator<Element> eleItr1 = element.elementIterator();
                    while (eleItr1.hasNext()) {
                        Map<String, String> kv = new HashMap<String, String>();
                        Element ele = eleItr1.next();
                        kv.put(FILTERED_IP, ele.attributeValue(FILTERED_IP));
                        arr1.add(kv);
                    }
                    object.put(IP_FILTER, arr1);
                    break;
                default:
                    Iterator<Attribute> attrIterator = element.attributeIterator();
                    while (attrIterator.hasNext()) {
                        Attribute attr = attrIterator.next();
                        object.put(attr.getName(), attr.getValue());
                    }
                    break;
            }
        }
        JSONArray array = (JSONArray) object.get(PROXY_PASS);
        if (array.size()==0){
            object.put(PERSONAL_URL,"/*");
        }
    }

    public String xml2Json() {
        return object.toJSONString();
    }

    public void listAll() {
        System.out.println(object.toJSONString());
    }
}
