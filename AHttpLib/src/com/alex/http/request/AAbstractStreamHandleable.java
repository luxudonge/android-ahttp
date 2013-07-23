package com.alex.http.request;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

/**
 * 
 * 解析类
 * 
 * @author Alex.Lu
 * 
 */
public abstract class AAbstractStreamHandleable implements AHandleable{

	
	public abstract Object handle(long requestId,InputStream inputStream);

	/**
	 * 返回一个数据流
	 * @param paramInputStream 数据流
	 * @param paramBundle 请求中带的数据
	 * @return 返回数据
	 * @throws IOException 
	 */
	
	@Override
	public Object handle(int requestId,HttpEntity entity) throws IOException{
        Object data = handle(requestId, entity.getContent());
		return data;
		
	}
	
}
