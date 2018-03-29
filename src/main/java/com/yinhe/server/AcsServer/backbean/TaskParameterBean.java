package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

public class TaskParameterBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3485058367846127929L;
	/**
	 * 
	 */
	public static final String m_periodicEnable = "Device.Task.PeriodicEnable";
	public static final String m_period = "Device.Task.Period";
	public static final String m_stop = "Device.Task.Stop";
	
	private boolean periodicEnable;
	private Long period;
	private boolean stop;
	
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
}
