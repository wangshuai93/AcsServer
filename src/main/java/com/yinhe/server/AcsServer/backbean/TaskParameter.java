package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TaskParameter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5269107406663911006L;
	public static final String m_periodicEnable = "Device.Task.PeriodicEnable";
	public static final String m_period = "Device.Task.Period";
	public static final String m_stop = "Device.Task.Stop";
	
	private boolean periodicEnable;
	private Long period;
	private boolean stop;
	private List<NodeModelDetailBean> param_set_list = new ArrayList<NodeModelDetailBean>();
	
	public boolean isPeriodicEnable() {
		return periodicEnable;
	}
	public void setPeriodicEnable(boolean periodicEnable) {
		this.periodicEnable = periodicEnable;
	}
	public Long getPeriod() {
		return period;
	}
	public void setPeriod(Long period) {
		this.period = period;
	}
	public boolean isStop() {
		return stop;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	public List<NodeModelDetailBean> getParam_set_list() {
		return param_set_list;
	}
	public void setParam_set_list(List<NodeModelDetailBean> param_set_list) {
		this.param_set_list = param_set_list;
	}	
}
