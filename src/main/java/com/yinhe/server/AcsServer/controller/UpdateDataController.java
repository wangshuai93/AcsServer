package com.yinhe.server.AcsServer.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import com.yinhe.server.AcsServer.backbean.UpdateDataBean;
import com.yinhe.server.AcsServer.ejb.UpdateDataEJB;
import com.yinhe.server.AcsServer.model.UpdateDataRecord;
import com.yinhe.server.AcsServer.util.XlsxXlxConverter;
import com.yinhe.server.AcsServer.util.XmlConverter;

@Named
@RequestScoped
public class UpdateDataController {
	
	private static final String m_filePath = System.getProperty("jboss.home.dir")+"/welcome-content/datamodel/";

	@Inject
	private Logger log;
	@Inject
	private UpdateDataEJB m_updateDataEJB;
	
	private List<UpdateDataBean> updateDataBeanList; 
	private String m_importMessage;
	
	@PostConstruct
	public void init(){
		log.info("UpdateDataController init()");
		updateDataBeanList = new ArrayList<UpdateDataBean>();
		getAllXlsxFiles();
	}
	
	public void getAllXlsxFiles(){
		File afile = new File(m_filePath);
		File[] files = afile.listFiles();
		if(files != null){
			int m_rowId = 0;
			for(File file : files){
				if(file.getName().contains("_") && file.getName().endsWith(".xlsx")){
					log.info("[getAllXlsxFiles()] file name = " + file.getName());
					UpdateDataBean updateDataBean = new UpdateDataBean();
					String[] nameSplit = file.getName().split("_");
					String version = nameSplit[0];
					updateDataBean.setM_rowId(++m_rowId);
					updateDataBean.setM_version(version);
					updateDataBean.setM_name(file.getName());
					
					UpdateDataRecord updateDataRecord = m_updateDataEJB.findByFileName(file.getName());
					if(updateDataRecord != null){
						updateDataBean.setM_isCurrentVersion(updateDataRecord.getM_isCurrentVersion());
						updateDataBean.setM_date(updateDataRecord.getM_updateTime());
					}else{
						updateDataBean.setM_isCurrentVersion(false);
					}
					try {
						updateDataBeanList.add(updateDataBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else{
			return;
		}
	}
	
	/**
	 * 导入tr.xml文档
	 */
	public void importXml(){
		//页面选中的文件名
		String fileName = getRequestParamMap().get("fileName");
		String path =  m_filePath + fileName;
		
		XmlConverter xmlConverter = null;
		XlsxXlxConverter xlsxXlxConverter = null;
		
		if(path.endsWith(".xml")){
			try {
			     xmlConverter = new XmlConverter(); 
			     xmlConverter.parseXML(path);
			     String message = m_updateDataEJB.importXml(xmlConverter.getNodelist());
			     setM_importMessage(message);
			     if(message.contains("成功") || message.contains("SUCCESS")){
			    	 log.info("[importXml] fileName = " + fileName);
					 m_updateDataEJB.updateDetail(fileName);
			     }
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(path.endsWith(".xlsx") || path.endsWith(".xls")){
			try {
				xlsxXlxConverter = new XlsxXlxConverter();
				xlsxXlxConverter.parseXlsxXls(path);
				String message = m_updateDataEJB.importXml(xlsxXlxConverter.getNodelist());
				setM_importMessage(message);
				if(message.contains("成功") || message.contains("SUCCESS")){
					log.info("[importXml] fileName = " + fileName);
					m_updateDataEJB.updateDetail(fileName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	public void uploadListener(FileUploadEvent event){
		log.info("[uploadListener] get into uploadListener!");
        UploadedFile uploadedFile = event.getUploadedFile();    
        File destinationFile = null;
        InputStream uploadFileStream = null;       
        try {
            uploadFileStream = uploadedFile.getInputStream();
            File tmpDirectory = new File(m_filePath);        
            // 新建暂存目录
            if (!tmpDirectory.exists()) {
                tmpDirectory.mkdirs();
            }        
            //inputStream写入文件
            destinationFile = new File(tmpDirectory, new String(uploadedFile.getName().getBytes("ISO_8859_1"), "UTF-8"));
            log.info("[uploadListener] destinationFile name: " + new String(uploadedFile.getName().getBytes("ISO_8859_1"), "UTF-8"));
            log.info("[uploadListener] destinationFile path: " + destinationFile.getAbsolutePath());
            Files.copy(uploadFileStream, destinationFile.toPath(),StandardCopyOption.REPLACE_EXISTING);  
        } catch (IOException e) {
        	log.info("[uploadListener] uploadFile Excepiton!"); 
        } finally {           
            try {
                uploadFileStream.close();               
            } catch (IOException e) {
            	log.info("[uploadListener] close uploadFileStream Exception!");
            }     
        }		
	}

	public List<UpdateDataBean> getUpdateDataBeanList() {
		return updateDataBeanList;
	}

	public void setUpdateDataBeanList(List<UpdateDataBean> updateDataBeanList) {
		this.updateDataBeanList = updateDataBeanList;
	}

	public String getM_importMessage() {
		return m_importMessage;
	}

	public void setM_importMessage(String m_importMessage) {
		this.m_importMessage = m_importMessage;
	}
}
