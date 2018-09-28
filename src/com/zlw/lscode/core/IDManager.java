package com.zlw.lscode.core;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.zlw.lscode.core.XmlFileReader.OnXmlItemDataListener;

/**
 * IDManager
 * 用于刷新新的资源文件的ID值
 * @author zhaolewei
 */
public class IDManager {
	private static volatile IDManager instance;

	private IDDispatcher idDispatcher;
	private XmlFileReader xmlFileReader;
	private ExecutorService threadPool;
	private volatile int taskCount;
	private File outputFile;

	private IDManager() {
		idDispatcher = new IDDispatcher();
		threadPool = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
					@Override
					public Thread newThread(Runnable runnable) {
						return new Thread(runnable, "IdReadThread");
					}
				});
	}

	public static IDManager getInstance() {
		if (instance == null) {
			synchronized (IDDispatcher.class) {
				if (instance == null) {
					instance = new IDManager();
				}
			}
		}
		return instance;
	}

	/**
	 * 刷新资源文件ID属性
	 * @param outputFile 输出文件路径
	 * @param targetFile 需要刷新的资源文件
	 * @param files ID库资源的文件
	 */
	public void startRefreshXmlId(File outputFile, File targetFile,
			File... files) {
		CopyFileUtil.copyFile(targetFile, outputFile);
		this.outputFile = outputFile;
		taskCount = files.length;
		for (int i = 0; i < taskCount; i++) {
			threadPool.execute(getParseRunable(files[i]));
		}
	}

	/**
	 * 获取解析Runable 
	 * 工作内容： 将xml文件的ID值录入到idDispatcher中
	 * @param file xml文件
	 * @return Runnable
	 */
	private Runnable getParseRunable(File file) {
		return new Runnable() {
			@Override
			public void run() {
				xmlFileReader = new XmlFileReader();
				xmlFileReader.parseXmlFile(file, new OnXmlItemDataListener() {

					@Override
					public void onXmlItemData(String type, String id) {
						idDispatcher.putResItem(type, Long
								.parseLong(id.substring(2, id.length()), 16));
					}
				});
				taskCount--;
				checkFinish();
			}
		};
	}

	/**
	 * 检查资源文件是否录入完成
	 */
	private synchronized void checkFinish() {
		if (taskCount == 0) {
			System.out.println("录入完成，开始刷新ID");
			refreshId();
		}
	}

	/**
	 * 刷新ID并写入至outputFile
	 */
	private void refreshId() {
		if (outputFile == null) {
			return;
		}
		XmlFileIdWriter xmlFileIdWriter = new XmlFileIdWriter();
		xmlFileIdWriter.dispatcherId(outputFile, idDispatcher);
		System.out.println("写入完成");
	}

}
