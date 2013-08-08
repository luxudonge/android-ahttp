package com.alex.http.request;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * 
 * 解析字符串数据
 * 
 * @author Alex.Lu
 * 
 */
public abstract class AbstractStringHandleable implements Handleable{

	
	public abstract Object handle(long requestId,String content);

	/**
	 * 返回一个数据流
	 * @param paramInputStream 数据流
	 * @param paramBundle 请求中带的数据
	 * @return 返回数据
	 * @throws IOException 
	 */
	@Override
	public Object handle(int requestId,HttpEntity entity) throws IOException{
		entity = new BufferedHttpEntity(entity);
        String responseBody = EntityUtils.toString(entity, "UTF-8");
        Object data = handle(requestId, responseBody);
		return data;
		
	}
	
}
