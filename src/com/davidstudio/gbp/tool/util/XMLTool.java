package com.davidstudio.gbp.tool.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLTool {
	
	private static String xmlfile;
	
	public static void updateXMLFile(String xml, String nodepath, String content){
		try {
			xmlfile = xml;
			FileInputStream fis = new FileInputStream(xmlfile);			
			SAXReader reader = new SAXReader(false);	
			reader.setEntityResolver(getResolver());
		
			Document doc = reader.read(fis);	
			updateCommonDocument(doc, nodepath, content);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
	}
	
	public static void updateCacheFile(String xml, String nodepath, String eleVal, LinkedHashMap<String, String> keyVals){
		try {
			xmlfile = xml;
			FileInputStream fis = new FileInputStream(xmlfile);			
			SAXReader reader = new SAXReader(false);	
			reader.setEntityResolver(getResolver());
		
			Document doc = reader.read(fis);	
			updateCacheDocument(doc, nodepath, eleVal, keyVals);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
	}
	
	@SuppressWarnings("rawtypes")
	private static void updateCommonDocument(Document doc, String nodepath, String content){
		//Element father = doc.getRootElement().element("bean").element("property").element("list");
		Element father = (Element) doc.selectNodes(nodepath).get(0);
		
		//�жϸ�element�Ƿ��Ѿ�����
		Iterator it = father.elementIterator("value");
		boolean ischecked = false;
		while(it.hasNext()){
			Element ele = (Element) it.next();
			if(ele.getText().trim().equals(content)){
				ischecked = true;
				break;
			}
		}
		
		if(ischecked){
			//TODO �Ƿ������ʾ
			return;
		}
		
		Element element = father.addElement("value");
		element.setText(content);
		
		writeDocument(doc);
	}
	
	@SuppressWarnings("rawtypes")
	private static void updateCacheDocument(Document doc, String nodepath, String eleVal, LinkedHashMap<String, String> keyVal){
		if(keyVal == null)
			return;
		Element father = (Element) doc.selectNodes(nodepath).get(0);
		
		//�жϸ�element�Ƿ��Ѿ�����
		Iterator it = father.elementIterator(eleVal);
		boolean ischecked = false;
		while(it.hasNext()){
			Element ele = (Element) it.next();
			if(ele.attribute("key").equals(keyVal.get("key"))){
				ischecked = true;
				break;
			}
		}
		
		if(ischecked){
			return;
		}
		
		Element element = father.addElement(eleVal);
		Iterator<String> its = keyVal.keySet().iterator();
		while(its.hasNext()){
			String key = its.next();
			element.addAttribute(key, keyVal.get(key));
		}
		
		writeDocument(doc);
	}
	
	private static void writeDocument(Document doc){
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GBK");
			
			FileOutputStream fos = new FileOutputStream(xmlfile);
			XMLWriter writer = new XMLWriter(fos, format);
	
			writer.write(doc);
			writer.close();		
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static EntityResolver getResolver(){
		return new EntityResolver(){
//			@Override
			public InputSource resolveEntity(String publicId, String systemId)throws SAXException, IOException {
				return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='utf-8'?>\n".getBytes()));
			}
		};
	}

}
