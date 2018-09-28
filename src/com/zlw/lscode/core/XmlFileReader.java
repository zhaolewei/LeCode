package com.zlw.lscode.core;

import java.io.File;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Xml文件解析器
 * 获取xml数据的type和id值
 * @author zhaolewei
 */
public class XmlFileReader {
	DocumentBuilder documentBuilder;
	public XmlFileReader() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析xml文件
	 * @param file xml文件
	 * @param listener 回调每组数据的type和id值
	 */
	public synchronized void parseXmlFile(File file,
			OnXmlItemDataListener listener) {
		if (!file.exists()) {
			System.out.println("文件不存在，请检查路径");
			return;
		}

		if (listener == null) {
			System.out.println("listener 不能为空");
			return;
		}

		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(file);
			Element root = document.getRootElement();
			for (Iterator<Element> i = root.elementIterator("public"); i
					.hasNext();) {
				Element item = i.next();
				String type = item.attributeValue("type");
				String id = item.attributeValue("id");
				listener.onXmlItemData(type, id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听每组数据的type和id值
	 */
	public interface OnXmlItemDataListener {
		/**
		 * 一组数据解析完成
		 * @param type
		 * @param id
		 */
		void onXmlItemData(String type, String id);
	}
}
