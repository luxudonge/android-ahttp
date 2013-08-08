package com.alex.http.request;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alex.http.core.HttpLog;

/**
 * 
 * 数据接收
 * 
 * @author Alex.Lu
 *
 */
public class ResponseHandler {

	/*数据请求成功消息*/
	protected static final int SUCCESS_MESSAGE = 0;
	/*数据请求失败消息*/
	protected static final int FAILURE_MESSAGE = 1;
	/*请求开始消息*/
	protected static final int START_MESSAGE = 2;
	/*请求结束消息*/
	protected static final int FINISH_MESSAGE = 3;
	/*请求数据更新消息*/
	protected static final int UPDATE_MESSAGE = 4;
	/*请求重复请求*/
	protected static final int REPEAT_MESSAGE = 5;
	/*请求上传数据*/
	protected static final int UPLOAD_MESSAGE = 6;
	
	
	private ReponseUpdateDataListeners mReponseUpdateDataListeners;
	private ReponseDataListeners mReponseDataListeners;
	private StateListeners mStateListeners;
	private UploadDataListeners mUploadDataListeners;
	
	private Handler mHandler;
	
	public ResponseHandler(){
		if(Looper.myLooper() != null){
			mHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					ResponseHandler.this.handleMessage(msg);
				}
			};
		}
	}
	
	/**
	 * 设置数据更新请求监听
	 * @param reponseUpdateDataListeners
	 */
	public void setReponseUpdateDataListeners(ReponseUpdateDataListeners reponseUpdateDataListeners){
		mReponseUpdateDataListeners = reponseUpdateDataListeners;
	}
	
	/**
	 * 设置请求数据返回监听
	 * @param reponseDataListeners
	 */
	public void setReponseDataListeners(ReponseDataListeners reponseDataListeners){
		mReponseDataListeners = reponseDataListeners;
	}
	
	/**
	 * 设置请求状态监听
	 * @param stateListeners
	 */
	public void setStateListeners(StateListeners stateListeners){
		mStateListeners = stateListeners;
	}
	
	/**
	 * 上传文件监听
	 * @param uploadDataListeners
	 */
	public void setUploadDataListeners(UploadDataListeners uploadDataListeners){
		mUploadDataListeners = uploadDataListeners;
	}
	
	private Message obtainMessage(int responseMessage,int requestId,Object response){
		Message msg = null;
		if(mHandler != null){
			msg = mHandler.obtainMessage(responseMessage, requestId, 0,response);
		}else{
			 msg = Message.obtain();
	         msg.what = responseMessage;
	         msg.arg1 = requestId;
	         msg.obj = response;
		}
		return msg;
	}
	/**
	 * 发送成功消息
	 * @param requestId 请求唯一标示
	 * @param stuteCode http响应状态
	 * @param data 数据
	 */
	public void sendSuccessMessage(int requestId,int stuteCode, Object data){
		Object[] response = {stuteCode,data};
		sendMessage(obtainMessage(SUCCESS_MESSAGE, requestId, response));
	}
	
	/**
	 * 
	 * @param requestId
	 * @param stuteCode
	 * @param e
	 */
	public void sendErrorMessage(int requestId,int stuteCode,Throwable e){
		Object[] response = {stuteCode,e};
		sendMessage(obtainMessage(FAILURE_MESSAGE, requestId, response));
	}
	
	/**
	 * 开始发送请求
	 * @param requestId
	 */
	public void sendStartRequestMessage(int requestId){
		sendMessage(obtainMessage(START_MESSAGE, requestId, null));
	}
	
	/**
	 * 完成请求
	 * @param requestId
	 */
	public void sendFinishRequestMessages(int requestId){
		sendMessage(obtainMessage(FINISH_MESSAGE, requestId, null));
	}
	
	/**
	 * 重复发送请求
	 * @param requestId
	 * @param count
	 */
	public void sendReqeatRequestMessages(int requestId,int count){
		sendMessage(obtainMessage(REPEAT_MESSAGE, requestId, count));
	}
	
	/**
	 * 资源下载请求
	 * @param requestId
	 * @param size
	 * @param all
	 */
	public void sendUpdateDataMessage(int requestId,long size,long all){
		Object[] response = {Long.valueOf(size),Long.valueOf(all)};
		sendMessage(obtainMessage(UPDATE_MESSAGE, requestId, response));
	}
	
	/**
	 * 资源上传请求
	 * @param requestId
	 * @param count
	 * @param index
	 * @param currentSize
	 * @param allSize
	 */
	public void sendUpdateUploadDataMessaget(int requestId,int count,int index,int currentSize,int allSize){
		Object[] response = {Integer.valueOf(count),Integer.valueOf(index),Integer.valueOf(currentSize),Integer.valueOf(allSize)};
		sendMessage(obtainMessage(UPLOAD_MESSAGE, requestId, response));
	}
	
	private void handleMessage(Message msg){
		int requestId = msg.arg1;
		switch (msg.what) {
		case SUCCESS_MESSAGE:{
			if(mReponseDataListeners != null){
				HttpLog.print(this, requestId,"SUCCESS_MESSAGE");
				Object[] response = (Object[])msg.obj;
				mReponseDataListeners.onSuccessResult(requestId, (Integer)response[0], (Object)response[1]);
			}
			break;
		}
		case FAILURE_MESSAGE:{
			if(mReponseDataListeners != null){
				HttpLog.print(this, requestId,"FAILURE_MESSAGE");
				Object[] response = (Object[])msg.obj;
				mReponseDataListeners.onErrorResult(requestId, (Integer)response[0], (Throwable)response[1]);
			}
			break;
		}
		case START_MESSAGE:{
			
			if(mStateListeners != null){
				HttpLog.print(this, requestId,"START_MESSAGE");
				mStateListeners.onStartRequest(requestId);
			}
			break;
		}
		case FINISH_MESSAGE:{
			
			if(mStateListeners != null){
				HttpLog.print(this, requestId,"FINISH_MESSAGE");
				mStateListeners.onFinishRequest(requestId);
			}
			break;
		}
		case REPEAT_MESSAGE:{
			if(mStateListeners != null){
				int count = (Integer)msg.obj;	
				HttpLog.print(this, requestId,"REPEAT_MESSAGE   count:"+count);
				mStateListeners.onRepeatRequest(requestId,count);
			}
			break;
		}
		case UPDATE_MESSAGE:{
			
			Object[] response = (Object[])msg.obj;
			if(mReponseUpdateDataListeners !=null)
				mReponseUpdateDataListeners.updateDownloadData(
						requestId, 
						(Long)response[0], 
						(Long)response[1]);
			break;
		}
		case UPLOAD_MESSAGE:{
			Object[] response = (Object[])msg.obj;
			if(mUploadDataListeners != null){
				mUploadDataListeners.updataUploadData(
						requestId, 
						(Integer)response[0], 
						(Integer)response[1], 
						(Integer)response[2], 
						(Integer)response[3]);
			}
		}
		default:
			break;
		}
	}
	
	
	private void sendMessage(Message msg){
		if(mHandler != null){
			mHandler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
	}
}
