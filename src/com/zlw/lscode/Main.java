package com.zlw.lscode;

import java.io.File;

import com.zlw.lscode.core.IDManager;

public class Main {
	public static void main(String[] args) {
		File publicFile = new File("E:\\EC\\LSCode\\src\\res\\public.xml");
		File overlayFile = new File("E:\\EC\\LSCode\\src\\res\\overlay.xml");
		File targetFile = new File("E:\\EC\\LSCode\\src\\res\\3rd.xml");
		File outputFile = new File("E:\\EC\\LSCode\\src\\res\\result.xml");
		IDManager idManager = IDManager.getInstance();
		idManager.startRefreshXmlId(outputFile, targetFile, publicFile,
				overlayFile);
	}
}
