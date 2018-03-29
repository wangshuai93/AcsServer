package com.yinhe.server.AcsServer.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.QueryCPEBean;
import com.yinhe.server.AcsServer.ejb.CPEHandler;
import com.yinhe.server.AcsServer.enums.CPEResultCode;
import com.yinhe.server.AcsServer.util.Resources;

@Named("cPEController")
@RequestScoped
public class CPEController {

	@Inject
	private CPEHandler m_handler;
	@Inject
	private CPEBean m_cpeBean;
	@Inject
	private Logger log;
	@Inject
	private QueryCPEBean m_queryCPEBean;

	private List<CPEBean> m_devices = new ArrayList<CPEBean>();
	private List<CPEBean> m_toDels = new ArrayList<CPEBean>();
	private QueryCPEBean m_query;
	private String m_cpeMessage;

	private Map<String, String> requestParamMap;
	private Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

	private List<Long> m_computerRoomIdList;
	private String m_status;
	private String param_value;

	@PostConstruct
	public void init() {
		log.info("[init] CPEController record init");
		String o = getRequestParamMap().get("load");
		if (o != null && "lazy".equals(o)) {
			log.info("[init] CPEController load: lazy!");
		} else {
			queryRecordList();
		}
//		listAll();	
	}

	/**
	 * @Title: addDevice
	 * @author: wr@yinhe.com
	 * @Description: Add Device By m_Bean
	 * @param
	 * @return void
	 * @throws
	 * @date 2017年2月24日 上午11:41:21
	 */
	public void addDevice() {
		if (Resources.isNullOrEmpty(m_cpeBean.getM_serialNumber())) {
			m_cpeMessage = CPEResultCode.CPE_ADD_SERIALNUMBER_IS_NULL
					.toLocalString();
			return;
		}

		if (Resources.isNullOrEmpty(m_cpeBean.getM_macAddress())) {
//			m_cpeMessage = CPEResultCode.CPE_ADD_MACADDRESS_IS_NULL
//					.toLocalString();
//			return;
		} else {
//			String correctMacAddress = "[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}";
//			String macaddress = m_cpeBean.getM_macAddress();
//			Pattern pattern = Pattern.compile(correctMacAddress);
//			Matcher matcher = pattern.matcher(macaddress);
//			if (!matcher.matches()) {
////				m_cpeMessage = CPEResultCode.CPE_ADD_MACADDRESS_TPYE_ERROR
////						.toLocalString();
//				return;
//			}
		}
		if(Resources.isNullOrEmpty(m_cpeBean.getM_positionName())){
			m_cpeMessage = CPEResultCode.CPE_ADD_POSITION_NAME_NULL.toLocalString();
			return;
		}
		
		//读取隐藏域中的值
		String positionX = getRequestParamMap().get("positionX");
		String positionY = getRequestParamMap().get("positionY");
		log.info("[addDevice] positionX = " + positionX);
		log.info("[addDevice] positionY = " + positionY);
		if(!"".equals(positionX) && !"".equals(positionY)){
			m_cpeBean.setM_positionX(Integer.parseInt(positionX));
			m_cpeBean.setM_positionY(Integer.parseInt(positionY));
		}
		
		CPEResultCode cpeResultCode = m_handler.addDevice(m_cpeBean);
		if (null != cpeResultCode) {
			m_cpeMessage = cpeResultCode.toLocalString();
			return;
		}

		listAll();
	}

	/**
	 * @Title: delDevice
	 * @author: wr@yinhe.com
	 * @Description: 删除设备
	 * @param
	 * @return void
	 * @throws
	 * @date 2017年2月25日 下午4:09:19
	 */
	public void delDevice() {
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		// "select_item" xhtml中所选择的要删除的列表的名字
		String[] strIdArray = context.getRequestParameterValuesMap().get(
				"select_item");
		if (null == strIdArray || strIdArray.length == 0) {
			m_cpeMessage = CPEResultCode.CPE_DELETE_CHECKED_IS_NULL
					.toLocalString();
			return;
		}
		List<Long> idList = new ArrayList<Long>();
		try {
			for (String str : strIdArray) {
				idList.add(Long.parseLong(str));
			}
		} catch (Exception e) {
			m_cpeMessage = CPEResultCode.CPE_DELETE_CHECKED_IS_NULL
					.toLocalString();
			return;
		}
		m_queryCPEBean.setIdList(idList);
		List<CPEBean> m_toDels = m_handler.select(m_queryCPEBean);
		CPEResultCode cpeResultCode = m_handler.delDevice(m_toDels);

		if (null == cpeResultCode) {
			m_cpeMessage = CPEResultCode.CPE_DELETE_CHECKED_ERROR
					.toLocalString();
		} else {
			m_cpeMessage = cpeResultCode.toLocalString();
			// 删除后刷新所有设备列表
			listAll();
		}
	}

	/**
	 * @Title: listAll
	 * @author: wr@yinhe.com
	 * @Description: 获取所有设备
	 * @param
	 * @return void
	 * @throws
	 * @date 2017年3月1日 下午7:35:41
	 */
	public void listAll() {
		this.m_devices = m_handler.listAll();
		if (null != m_devices) {
			setM_devices(m_devices);
		} else {
			m_cpeMessage = CPEResultCode.CPE_IS_NULL.toLocalString();
		}
	}

	/**
	 * @Title: select
	 * @author: wr@yinhe.com
	 * @Description: 查询设备
	 * @param
	 * @return void
	 * @throws
	 * @date 2017年2月25日 下午4:18:01
	 */
	public void select() {
		m_queryCPEBean.setM_serialNumber(m_cpeBean.getM_serialNumber());
		m_devices = m_handler.select(m_queryCPEBean);
	}
	
	public void selectByStatus(ValueChangeEvent event){
		String status = (String)event.getNewValue();
		log.info("[select] get into selectByStatus,status = " + status);
		if(status != null){
			if(status.equals("all")){
				listAll();
			}else{
				List<CPEBean> cpe_list =  m_handler.listAll();
				int count = 0;
				for(CPEBean cpe  : cpe_list){
					if(cpe.getM_status().equals(status)){
						cpe.setM_rowId(++count);
						m_devices.add(cpe);
					}
				}
			}
		}
	}

	/**
	 * @Title: queryRecordListByAjax
	 * @author: wr@yinhe.com
	 * @Description: 模糊查询
	 * @param @param e
	 * @return void
	 * @throws
	 * @date 2017年2月27日 下午12:22:51
	 */
	public void queryRecordListByAjax(AjaxBehaviorEvent e) {
		select();
	}

	/**
	 * @Title: gotoDevicesDetailPage
	 * @author: wr@yinhe.com
	 * @Description: 跳转设备详情页面
	 * @param @return
	 * @return String
	 * @throws
	 * @date 2017年2月27日 下午4:59:51
	 */
	public String gotoDevicesDetailPage() {
		// 获取网页中的 detail_serial_number
		// device_manage.xhtml
		//test
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String[] idArray = context.getRequestParameterValuesMap().get("detail_serial_number");
		if (idArray == null || idArray.length == 0) {
			return null;
		}
		String serialNumber = "";
		try {
			serialNumber = idArray[0];
			log.info("[gotoDevicesDetailPage] detail_serial_number == " + serialNumber);
		} catch (Exception e) {
			return null;
		}		
		session.put("serial_number", serialNumber);		
		setM_cpeBean(m_handler.setCPEBean(serialNumber));		
		return "device_info.jsf";
	}
	
	// device_manage.xhtml
	public String gotoDevicesUpdatePage() {
		// 获取网页中的 detail_serial_number
//		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//		String[] idArray = context.getRequestParameterValuesMap().get("detail_serial_number");
		String sn = "";
		sn = (String)session.get("serial_number");
//		if (idArray == null || idArray.length == 0) {
//			return null;
//		}
		if (sn == "") {
			return null;
		}
//		String sn = "";
		try {
//			sn = idArray[0];
			log.info("[gotoDevicesUpdatePage] detail_serial_number == " + sn);
		} catch (Exception e) {
			return null;
		}		
		session.put("serial_number", sn);		
		m_cpeBean = m_handler.setCPEBean(sn);
		return "update_device.jsf";
	}
	
	public String update(){
		String sn = (String)session.get("serial_number");
		log.info("[update] serial_number == " + sn);
		if(sn != null){
			m_cpeBean.setM_serialNumber(sn);
			if(Resources.isNullOrEmpty(m_cpeBean.getM_positionName()) && Resources.isNullOrEmpty(m_cpeBean.getM_ip()) ){
				m_cpeMessage = CPEResultCode.CPE_UPDATE_VALUE_NULL.toLocalString();
				return "";
			}
			log.info("[update] positionName = " + m_cpeBean.getM_positionName());
			log.info("[update] ip = " + m_cpeBean.getM_ip());
			m_cpeMessage = m_handler.updateDevice(m_cpeBean).toLocalString();
		}
		return "";
	}
	 
	/**
	 * 获取参数map
	 * 
	 * @return
	 */
	private Map<String, String> getRequestParamMap() {
		if (requestParamMap == null) {
			requestParamMap = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
		}
		return requestParamMap;
	}

	// device_info.xhtml
	public String gotoAddBroadcastTaskPage() {
		String serialnum = getRequestParamMap().get("broadcast_serial_number");
		setM_cpeBean(m_handler.setCPEBean(serialnum));
		return "add_broadcast_task.jsf";
	}
	

	public String returnAddBroadcastTask() {
		return "add_broadcast_task.jsf";
	}
	
	public void queryRecordList(){
		this.m_devices = m_handler.listAllDevices(m_queryCPEBean);
	}
	
	public void queryFirstPage(){
		m_queryCPEBean.setCurrent(1);
		queryRecordList();
	}
	
	public void queryCurrentPage() {
		this.queryRecordList();
	}
	
	public void queryPreviousPage() {
		m_queryCPEBean.previous();
		this.queryRecordList();
	}

	public void queryNextPage() {
		m_queryCPEBean.next();
		this.queryRecordList();
	}
	
	public List<CPEBean> getM_devices() {
		return m_devices;
	}

	public void setM_devices(List<CPEBean> m_devices) {
		this.m_devices = m_devices;
	}

	public List<CPEBean> getM_toDels() {
		return m_toDels;
	}

	public void setM_toDels(List<CPEBean> m_toDels) {
		this.m_toDels = m_toDels;
	}

	public QueryCPEBean getM_query() {
		return m_query;
	}

	public void setM_query(QueryCPEBean m_query) {
		this.m_query = m_query;
	}

	public String getM_cpeMessage() {
		return m_cpeMessage;
	}

	public void setM_cpeMessage(String m_cpeMessage) {
		this.m_cpeMessage = m_cpeMessage;
	}

	public CPEHandler getM_handler() {
		return m_handler;
	}

	public void setM_handler(CPEHandler m_handler) {
		this.m_handler = m_handler;
	}

	public CPEBean getM_cpeBean() {
		return m_cpeBean;
	}

	public void setM_cpeBean(CPEBean m_cpeBean) {
		this.m_cpeBean = m_cpeBean;
	}

	public QueryCPEBean getM_queryCPEBean() {
		return m_queryCPEBean;
	}

	public void setM_queryCPEBean(QueryCPEBean m_queryCPEBean) {
		this.m_queryCPEBean = m_queryCPEBean;
	}

	public List<Long> getM_computerRoomIdList() {
		return m_computerRoomIdList;
	}

	public void setM_computerRoomIdList(List<Long> m_computerRoomIdList) {
		this.m_computerRoomIdList = m_computerRoomIdList;
	}
	
	public String gotoPingResultPage(){
		return "result_ping.jsf";
	}

	public String getM_status() {
		return m_status;
	}

	public void setM_status(String m_status) {
		this.m_status = m_status;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}
	
}
