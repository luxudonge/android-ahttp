package com.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PostFile {

	private File mFile;
	
	private String mFileName;
	
	private String mFilePath;
	
	private String mParameterName;
	
	private String mContentType = "application/octet-stream";
	
	public PostFile(String fileName,String filePath,String parameterName,String contenType){
		mFilePath = filePath;
		mFileName = fileName;
		mFile = new File(fileName);
		mParameterName = parameterName;
		mContentType = contenType;
		mFilePath = filePath;
	}
	
	public PostFile(String filePath,String parameterName){
		mFilePath = filePath;
		int end = filePath.lastIndexOf("/");
		mFileName = filePath.substring(end+1);
		mFile = new File(filePath);
		mParameterName = parameterName;
	}
	
	public FileInputStream getInputStream(){
		FileInputStream inStream = null;
		try {
            inStream = new FileInputStream(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		return inStream;
	}
	
	public String getFilePath(){
		return mFilePath;
	}
	
	public String getFileName(){
		return mFileName;
	}
	
	public String getParameterName(){
		return mParameterName;
	}
	
	public String getContentType(){
		return mContentType;
	}
}
