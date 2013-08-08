package com.alex.http.request;


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
	public void updateDownloadData(int requestId,long curSize,long allSize);
	
}
