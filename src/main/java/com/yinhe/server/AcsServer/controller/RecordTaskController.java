package com.yinhe.server.AcsServer.controller;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.util.Ajax;
import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.ParamGetBean;
import com.yinhe.server.AcsServer.backbean.RecordResultDetailBean;
import com.yinhe.server.AcsServer.backbean.RecordResultDetailBean2;
import com.yinhe.server.AcsServer.backbean.ResultQueryBean;
import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.ejb.ResultManagerEJB;
import com.yinhe.server.AcsServer.ejb.TaskEJB;
import com.yinhe.server.AcsServer.ejb.TaskHandler;
import com.yinhe.server.AcsServer.ejb.TaskSettingEJB;
import com.yinhe.server.AcsServer.enums.TaskResultCode;
import com.yinhe.server.AcsServer.tcpClient.TcpClient;
import com.yinhe.server.AcsServer.util.Resources;

@Named
@ViewScoped
public class RecordTaskController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3292765258285219868L;
	@Inject
	private Logger logger;
	@Inject
	private ResultManagerEJB resultManagerEJB;
	@Inject
	private TaskSettingEJB taskSettingEJB;
	@Inject
	private TaskEJB taskEJB;
	@Inject
	private ResultQueryBean queryBean;
	@Inject
	private TaskHandler m_handler;
	@Inject
	private CPEBean cpeBean;
	private String message;
	private List<RecordResultDetailBean> resultList;
	private List<RecordResultDetailBean2> resultList2;
	
	private static final String UPLOAD_FILE_TMP = System.getProperty("jboss.home.dir") + "/welcome-content/record";
	private Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private static final String TASK_NAME = "RECORD_TASK";
	
	//add
	@Inject
	private TcpClient tcpClient;
	private Integer currentGoal;
	private String currentFile;
	
	@PostConstruct
	public void init(){
		logger.info("[init] RecordTaskController record init");
		
		String o = getRequestParamMap().get("load");
		if (o != null && "lazy".equals(o)) {
			//do-nothing
		} else {
			queryRecordList();
		}
	}
	
	private void queryRecordList() {
		Object sn = session.get("serial_number");
		if(sn != null) {
			String serialNumber =  (String)sn;
			queryBean.setM_serialNumber(serialNumber);
			
			TaskBean taskBean = taskSettingEJB.findTaskByName(TASK_NAME);
			if (taskBean != null) {
				queryBean.setM_taskId(taskBean.getM_id());
			}
			
			//resultList = resultManagerEJB.listRecordList(queryBean);
			resultList2 = resultManagerEJB.listRecordList2(queryBean);
			hasDownload(); //判断是否已下载
		}
	}
	
	//判断文件是否已下载
	public void hasDownload() {
		List<String> file_list  = getAllDownloadFile();
		if (resultList2 != null && resultList2.size() > 0) {
			for (RecordResultDetailBean2 detailBean : resultList2) {
				String path = detailBean.getValue();
				int index = path.lastIndexOf("/");
				index++;
				String fileName = path.substring(index, path.length());
				if (file_list.contains(fileName)) {
					detailBean.setHasDownload(true);
				}
			}
		}
	}	
		
	//获取已下载文件
	public List<String> getAllDownloadFile(){
		 List<String> file_list = new ArrayList<String>();
		 String path = UPLOAD_FILE_TMP;
		 File file = new File(path);
		 if (file != null) {
			 File[] tempList = file.listFiles();
			 if (tempList != null) {
			     for (int i = 0; i < tempList.length; i++) {
					if (tempList[i].isFile()) {
						file_list.add(tempList[i].getName());
					}
				}
			     return file_list;
			  }
		 }
		 return null;
	} 			
		
	public String gobackDeviceInfo() {
		return "device_info.jsf";
	}
	
	public void queryCurrentPage() {
		this.queryRecordList();
	}

	public void queryPreviousPage() {
		queryBean.previous();
		this.queryRecordList();
	}

	public void queryNextPage() {
		queryBean.next();
		this.queryRecordList();
	}
	
	public void startRecordTask() {
		Object sn = session.get("serial_number");
		String serialNumber = "";
		if(sn != null) {
			serialNumber = (String)sn;
			logger.info("[startRecordTask] serialNumber = " + serialNumber);
			this.setCpeBean(m_handler.setCPEBean(serialNumber));
			
			List<ParamGetBean> paramGet_list = taskEJB.findByTaskName(TASK_NAME);
			if (paramGet_list == null || paramGet_list.size() == 0) {
				message = "请设置录制任务上报参数";
				return; 
			}
			if (cpeBean != null) {
				Long ms = 1000L;
				//删除已有的录制文件
				resultManagerEJB.deleteExistRecordResult(paramGet_list);
				
				message = m_handler.startRecordTask(cpeBean, paramGet_list, ms).toLocalString();
				queryRecordList();
			} else {
				message = TaskResultCode.TASK_START_RECORD_ERROR.toLocalString();
			}
		} 		
	}
	
	//下载录制文件
	public void download() {
		String path = getRequestParamMap().get("download_fileName");
		logger.info("[download] path = " + path);
		if (!Resources.isNullOrEmpty(path)) {
			Object sn = session.get("serial_number");
			if(sn != null) {
				String serialNumber =  (String)sn;
				CPEBean cpeBean = m_handler.setCPEBean(serialNumber);
				String host = "";
				try {
					logger.info("[download] url = " + cpeBean.getM_ip());
					URL u = new URL(cpeBean.getM_ip());
					//URL u = new URL("https://172.16.218.5");
					host = u.getHost();
				} catch (MalformedURLException e) {
					logger.info("[download] translate url throws" + e.toString());
					return ;
				}
				
				if (!Resources.isNullOrEmpty(host)) {
					int index = path.lastIndexOf("/");
					index++;
					String fileName = path.substring(index, path.length());
					if (!Resources.isNullOrEmpty(fileName)) {
						this.currentFile = fileName;
						
						TcpClient tcpClient = new TcpClient();
						tcpClient.download(host, path, fileName);
						message = tcpClient.getDownloadResult().toLocalString();
						
						//重新获取
						queryRecordList();
					} else {
						message = "获取文件名失败！";
					}
				} else {
					message = "下载失败,获取主机号失败！";
				}
			} 
		} 
	}
	
	public void downloadTest() {
		System.out.println("download, tcpClient = " + tcpClient);
		tcpClient.download("172.16.218.5", null, "123.ts");
		message = tcpClient.getDownloadResult().toLocalString();
		System.out.println(tcpClient.getDownloadResult().toLocalString());
	}
	
	public void getProgress(AjaxBehaviorEvent event) {
		 //获取进度
    	currentGoal = tcpClient.getCurrent();
    	Ajax.oncomplete("showProgressBar()");
    	
	}
	
	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}

	public ResultQueryBean getQueryBean() {
		return queryBean;
	}

	public void setQueryBean(ResultQueryBean queryBean) {
		this.queryBean = queryBean;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public CPEBean getCpeBean() {
		return cpeBean;
	}

	public void setCpeBean(CPEBean cpeBean) {
		this.cpeBean = cpeBean;
	}

	public List<RecordResultDetailBean> getResultList() {
		return resultList;
	}

	public void setResultList(List<RecordResultDetailBean> resultList) {
		this.resultList = resultList;
	}

	public List<RecordResultDetailBean2> getResultList2() {
		return resultList2;
	}

	public void setResultList2(List<RecordResultDetailBean2> resultList2) {
		this.resultList2 = resultList2;
	}

	public Integer getCurrentGoal() {
		return currentGoal;
	}

	public void setCurrentGoal(Integer currentGoal) {
		this.currentGoal = currentGoal;
	}

	public String getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(String currentFile) {
		this.currentFile = currentFile;
	}
	
}
