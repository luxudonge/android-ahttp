package com.alex.http.request;

import java.io.IOException;

import org.apache.http.HttpEntity;

import com.alex.http.exception.HttpException;

/**
 * 
 * 解析类
 * 
 * @author Alex.Lu
 * 
 */
public interface Handleable {

	

	/**
	 * 返回一个数据流
	 * @param paramInputStream 数据流
	 * @param paramBundle 请求中带的数据
	 * @return 返回数据
	 * @throws IOException 
	 */
	public Object handle(int requestId,HttpEntity paramInputStream) throws HttpException, IOException;
}
