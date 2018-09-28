package com.zlw.lscode.core;

import java.util.Hashtable;
import java.util.Map;

/**
 * 资源ID分发器
 * @author zhaolewei
 */
public class IDDispatcher {
	private volatile Map<String, Long> resMap = new Hashtable<String, Long>();

	/**
	 * 录入资源ID
	 * @param name id的name
	 * @param id  id的value
	 */
	public synchronized void putResItem(String name, Long id) {
		if (resMap.containsKey(name) && resMap.get(name) >= id) {
			return;
		}
		resMap.put(name, id);
	}

	/**
	 * 计算ID的value
	 * @param name
	 * @return
	 */
	public synchronized Long getResNextId(String name) {
		Long value = resMap.get(name);
		if (value == null) {
			return -1L;
		}
		Long newValue = value + 1L;
		resMap.put(name, newValue);
		return newValue;
	}
}
