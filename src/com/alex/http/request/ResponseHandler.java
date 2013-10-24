package com.alex.http.request;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alex.http.core.HttpLog;
import com.alex.http.core.HttpRequest;
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
	public void sendSuccessMessage(HttpRequest request,int requestId,int stuteCode, Object data){
		Object[] response = {request,stuteCode,data};
		sendMessage(obtainMessage(SUCCESS_MESSAGE, requestId, response));
	}
	
	/**
	 * 
	 * @param requestId
	 * @param stuteCode
	 * @param e
	 */
	public void sendErrorMessage(HttpRequest request,int requestId,int stuteCode,Throwable e){
		Object[] response = {request,stuteCode,e};
		sendMessage(obtainMessage(FAILURE_MESSAGE, requestId, response));
	}
	
	/**
	 * 开始发送请求
	 * @param requestId
	 */
	public void sendStartRequestMessage(HttpRequest request,int requestId){
		Object[] response = {request};
		sendMessage(obtainMessage(START_MESSAGE, requestId, response));
	}
	
	/**
	 * 完成请求
	 * @param requestId
	 */
	public void sendFinishRequestMessages(HttpRequest request,int requestId){
		Object[] response = {request};
		sendMessage(obtainMessage(FINISH_MESSAGE, requestId, response));
	}
	
	/**
	 * 重复发送请求
	 * @param requestId
	 * @param count
	 */
	public void sendReqeatRequestMessages(HttpRequest request,int requestId,int count){
		Object[] response = {request,count};
		sendMessage(obtainMessage(REPEAT_MESSAGE, requestId, response));
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
				mReponseDataListeners.onSuccessResult((HttpRequest)response[0],requestId, (Integer)response[1], (Object)response[2]);
			}
			break;
		}
		case FAILURE_MESSAGE:{
			if(mReponseDataListeners != null){
				HttpLog.print(this, requestId,"FAILURE_MESSAGE");
				Object[] response = (Object[])msg.obj;
				mReponseDataListeners.onErrorResult((HttpRequest)response[0],requestId, (Integer)response[1], (Throwable)response[2]);
			}
			break;
		}
		case START_MESSAGE:{
			
			if(mStateListeners != null){
				HttpLog.print(this, requestId,"START_MESSAGE");
				Object[] response = (Object[])msg.obj;
				mStateListeners.onStartRequest((HttpRequest)response[0],requestId);
			}
			break;
		}
		case FINISH_MESSAGE:{
			
			if(mStateListeners != null){
				HttpLog.print(this, requestId,"FINISH_MESSAGE");
				Object[] response = (Object[])msg.obj;
				mStateListeners.onFinishRequest((HttpRequest)response[0],requestId);
			}
			break;
		}
		case REPEAT_MESSAGE:{
			if(mStateListeners != null){
				Object[] response = (Object[])msg.obj;
				int count = (Integer)response[1];	
				HttpLog.print(this, requestId,"REPEAT_MESSAGE   count:"+count);
				mStateListeners.onRepeatRequest((HttpRequest)response[0],requestId,count);
			}
			break;
		}
		case UPDATE_MESSAGE:{
			
			Object[] response = (Object[])msg.obj;
			if(mReponseUpdateDataListeners !=null)
				mReponseUpdateDataListeners.updateDownloadData(
						(HttpRequest)response[0],
						requestId, 
						(Long)response[1], 
						(Long)response[2]);
			break;
		}
		case UPLOAD_MESSAGE:{
			Object[] response = (Object[])msg.obj;
			}
		break;
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
