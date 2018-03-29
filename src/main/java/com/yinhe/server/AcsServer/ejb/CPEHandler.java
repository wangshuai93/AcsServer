package com.yinhe.server.AcsServer.ejb;

import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.QueryCPEBean;
import com.yinhe.server.AcsServer.backbean.QueryTaskResultBean;
import com.yinhe.server.AcsServer.enums.CPEResultCode;
import com.yinhe.server.AcsServer.enums.TaskResultCode;

@Stateful
public class CPEHandler {

	@Inject
	private CPEManager m_cpeManager;
	@Inject
	private CPEBean m_cpeBean;

	public CPEResultCode addDevice(CPEBean device) {
		return m_cpeManager.addDevice(device);
	}

	public CPEResultCode delDevice(List<CPEBean> device) {
		return m_cpeManager.delDevice(device);
	}

	/*public CPEResultCode update(CPEBean device) {
		return m_cpeManager.update(device);
	}*/

	//地图调用显示所有
	public List<CPEBean> listAll() {
		return m_cpeManager.listAll();
	}

	//页面调用分页
	public List<CPEBean> listAllDevices(QueryCPEBean m_queryCPEBean){
		return m_cpeManager.listAllDevices(m_queryCPEBean);
	}
	
	public List<CPEBean> select(QueryCPEBean query) {
		return m_cpeManager.select(query);
	}

	public CPEBean setCPEBean(String serialNumber) {
		this.m_cpeBean = m_cpeManager.setCPEBean(serialNumber);
		return m_cpeBean;
	}
	
	public TaskResultCode stop(CPEBean device, String type, long ms) {
		return m_cpeManager.stop(device, type, ms);
	}

	public CPEResultCode updateDevice(CPEBean cpeBean){
		return m_cpeManager.updateDevice(cpeBean);
	}
	
	public CPEResultCode updateDevicePosition(Long id, Integer x, Integer y){
		return m_cpeManager.updateDevicePosition(id, x, y);
	}

}
