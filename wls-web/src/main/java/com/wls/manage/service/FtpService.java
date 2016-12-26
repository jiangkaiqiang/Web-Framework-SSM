package com.wls.manage.service;

import java.util.List;

import com.wls.manage.dto.UploadFileEntity;

public interface FtpService {

	//public final static String HOST = "10.25.192.102";

	public final static String PUB_HOST = "42.121.130.177";

	public final static String USER_NAME = "pwftp";

	public final static String PASSWORD = "!@QWaszx0o";

	public final static int PORT = 21;

	public final static String BASEDIR = "/data";

	public final static int READPORT = 8089;
	
	public final static String READ_URL = "ftp://+"+"+username"+":"+PASSWORD+"@42.121.130.177/";
	
	//public final static String READ_URL = "http://139.196.189.93:8089/";
	
	//ftp://42.121.130.177/data/ueditor/20161217/1481958350959078009.png

	boolean uploadFile(UploadFileEntity uploadFile);

	boolean uploadFileList(List<UploadFileEntity> uploadFileList);
	
	boolean deleteFile(String url);

	boolean deleteByLocation(String location);
}
