package com.alex.http.request;

/**
 * 
 * 上传数据时监听上传的状况
 * 
 * @author Administrator
 *
 */
public interface UploadDataListeners {

	/**
	 * 状态返回
	 * 
	 * @param reqeustId 
	 * @param count 一共多少个资源（暂时只支持一个）
	 * @param index 第一几个资源 （为0）
	 * @param currentSize 当前上传多少
	 * @param allSize 当前资源的大小
	 */
	public void updataUploadData(int reqeustId,int count,int index,int currentSize,int allSize);
}
