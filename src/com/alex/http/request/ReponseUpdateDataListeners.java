package com.alex.http.request;

import com.alex.http.core.HttpRequest;


/**
 * 
 * 资源下载监听
 * 
 * @author Alex.Lu
 *
 */
public interface ReponseUpdateDataListeners {

	/**
	 * 
	 * @param requestId 资源请求id
	 * @param curSize 当前下载的资源大小
	 * @param allSize 资源总大小
	 */
	public void updateDownloadData(HttpRequest request,int requestId,long curSize,long allSize);
	
}
