package com.zlw.lscode.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlFileIdWriter {
	DocumentBuilder documentBuilder;
	public XmlFileIdWriter() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新xml Id值并写入到文件
	 * @param file 输出文件
	 * @param idDispatcher id分发器
	 */
	public synchronized void dispatcherId(File file,
			IDDispatcher idDispatcher) {
		if (!file.exists()) {
			System.out.println("文件不存在，请检查文件路径");
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
				String lastValue = item.attributeValue("id");
				String newValue = formatId(idDispatcher.getResNextId(type));
				item.addAttribute("id", newValue);
				System.out.println(
						"lastValue:" + lastValue + " ; newValue:" + newValue);
			}
			saveFile(document, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 将修改好的xml写入至文件
	 */
	private void saveFile(Document document, File file) {
		OutputFormat format = new OutputFormat();
		format.setEncoding("utf-8");

		FileOutputStream outStream = null;
		XMLWriter writer = null;
		try {
			outStream = new FileOutputStream(file);
			writer = new XMLWriter(outStream, format);

			writer.setEscapeText(false);
			writer.write(document);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 格式化16进制的值
	 * 示例： 2f -> 0x002f
	 * @param id 需要格式化的值
	 * @return 格式化输出的值
	 */
	private String formatId(Long id) {
		StringBuilder stringBuilder = new StringBuilder();
		String newId = Long.toHexString(id);
		int loseZeroCount = 4 - newId.length() % 4;

		stringBuilder.append("0x");
		for (int i = 0; i < loseZeroCount; i++) {
			stringBuilder.append("0");
		}
		stringBuilder.append(newId);
		return stringBuilder.toString();
	}
}
