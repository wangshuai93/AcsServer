package com.yinhe.server.AcsServer.backbean;

import java.util.Date;

public class RecordTaskParameter extends TaskParameter {
	private static final long serialVersionUID = -5929483685758602678L;
	public static final String m_maxSize = "Device.LAN.VideoRecord.MaxSize";
	public static final String m_channel = "Device.LAN.VideoRecord.Channel";
	public static final String m_startTime = "Device.LAN.VideoRecord.StartTime";
	public static final String m_endTime = "Device.LAN.VideoRecord.EndTime";
	
	public static final String m_state = "Device.LAN.VideoRecord.State";
	public static final String m_url = "Device.LAN.VideoRecord.VideoUrl";

	private Long maxSize;
	private String channel;
	private Date startTime;
	private Date endTime;

	public Long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Long maxSize) {
		this.maxSize = maxSize;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
