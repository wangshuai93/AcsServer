package com.yinhe.server.AcsServer.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import com.yinhe.server.AcsServer.backbean.UploadFileBean;

@Named
@RequestScoped
public class UpgradeController {
	private String m_upgradeMessage;
	private UploadedFile m_uploadFile;
	private static final String m_savePath = System.getProperty("jboss.home.dir")+"/welcome-content/";
	private String m_fileDir = "upload";
	private String m_fileName;
	private List<UploadFileBean> m_uploadFileList;
	@Inject 
	private Logger m_log;
	
	@PostConstruct
	public void init(){
		m_log.info("[init] UpgradeController init");
		m_log.info("[init] m_savePath = " + m_savePath);
		m_uploadFileList = new ArrayList<UploadFileBean>();
		getAllFileList();
	}
	
	public void getAllFileList(){
		String path = m_savePath + "upload";
		File file = new File(path);
		File[] tempList = file.listFiles();
		if(tempList != null){
			for (int i = 0; i < tempList.length; i++) {
				if(tempList[i].isFile()){
					//m_log.info("[getAllFileList] tempList[" + i + "] = " + tempList[i]);
					UploadFileBean uploadFileBean = new UploadFileBean(tempList[i].getName(), tempList[i].getPath());
					m_uploadFileList.add(uploadFileBean);
				}
			}
		}
	}
	
	public void deleteFile(){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String[] idArray = context.getRequestParameterValuesMap().get("delete_file_name");
		if (idArray == null || idArray.length == 0) {
			return;
		}
		String name = idArray[0];
		String path = m_savePath + "upload";
		File file = new File(path);
		File[] tempList = file.listFiles();
		m_log.info("[deleteFile] before m_uploadFileList.size = " + m_uploadFileList.size());
		for (int i = 0; i < tempList.length; i++) {
			if(tempList[i].isFile()){
				if(tempList[i].getName().equals(name)){
					tempList[i].delete();
				}
			}
		}
		m_uploadFileList = new ArrayList<UploadFileBean>();
		tempList = file.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if(tempList[i].isFile()){
				UploadFileBean uploadFileBean = new UploadFileBean(tempList[i].getName(), tempList[i].getPath());
				m_uploadFileList.add(uploadFileBean);
			}
		}
		m_log.info("[deleteFile] after m_uploadFileList.size = " + m_uploadFileList.size());
	}

	public void upload(org.primefaces.event.FileUploadEvent event){
		m_log.info("[upload] get into upload");
		
		org.primefaces.model.UploadedFile uploadFile = event.getFile();		
		SimpleDateFormat df = new SimpleDateFormat();
		Random rd = new Random();
		File filePath = new File(m_savePath);
		if(!filePath.isDirectory()){
			filePath.mkdir();
		}
		String fileType = uploadFile.getFileName().substring(uploadFile.getFileName().lastIndexOf("."));
		m_fileName = "upload/" + df.format(new java.util.Date()) 
				+ rd.nextInt(10) + rd.nextInt(10) + rd.nextInt(10)+fileType;
		m_log.info("[upload] m_fileName = " +  m_fileName);
		InputStream is;
		try {
			is = uploadFile.getInputstream();
		    File dir = new File(m_savePath + m_fileName);
		    OutputStream fout = new FileOutputStream(dir);
		    byte[] b = new byte[1024];
		    int len = 0;
		    while((len = is.read(b)) != -1){
			   fout.write(b,0,len);	
		    }
		    fout.close();
		    is.close();
		    m_upgradeMessage = "文件上传成功";
		} catch (IOException e) {
			m_upgradeMessage = "文件上传失败";
			e.printStackTrace();
		}
   }	
	
	public void uploadListener(FileUploadEvent event){
		m_log.info("[uploadListener] get into uploadListener");
        UploadedFile uploadedFile = event.getUploadedFile();    
        File destinationFile = null;
        InputStream uploadFileStream = null;       
        try {
            uploadFileStream = uploadedFile.getInputStream();
            File tmpDirectory = new File(m_savePath + m_fileDir);        
            // 新建暂存目录
            if (!tmpDirectory.exists()) {
                tmpDirectory.mkdirs();
            }        
            //inputStream写入文件
            destinationFile = new File(tmpDirectory, new String(uploadedFile.getName().getBytes("ISO_8859_1"), "UTF-8"));
            m_log.info("[uploadListener] destinationFile name: " + new String(uploadedFile.getName().getBytes("ISO_8859_1"), "UTF-8"));
            m_log.info("[uploadListener] destinationFile path: " + destinationFile.getAbsolutePath());
            Files.copy(uploadFileStream, destinationFile.toPath(),StandardCopyOption.REPLACE_EXISTING);  
            UploadFileBean uf = new UploadFileBean(uploadedFile.getName(),destinationFile.getAbsolutePath());
            m_uploadFileList.add(uf);
        } catch (IOException e) {
        	m_log.info("[uploadListener] uploadFile Excepiton"); 
        } finally {           
            try {
                uploadFileStream.close();               
            } catch (IOException e) {
            	m_log.info("[uploadListener] close uploadFileStream Exception");
            }     
        }		
	}
	
	public String getM_upgradeMessage() {
		return m_upgradeMessage;
	}

	public void setM_upgradeMessage(String m_upgradeMessage) {
		this.m_upgradeMessage = m_upgradeMessage;
	}

	public UploadedFile getM_uploadFile() {
		return m_uploadFile;
	}

	public void setM_uploadFile(UploadedFile m_uploadFile) {
		this.m_uploadFile = m_uploadFile;
	}

	public String getM_fileName() {
		return m_fileName;
	}

	public void setM_fileName(String m_fileName) {
		this.m_fileName = m_fileName;
	}

	public List<UploadFileBean> getM_uploadFileList() {
		return m_uploadFileList;
	}

	public void setM_uploadFileList(List<UploadFileBean> m_uploadFileList) {
		this.m_uploadFileList = m_uploadFileList;
	}
}
