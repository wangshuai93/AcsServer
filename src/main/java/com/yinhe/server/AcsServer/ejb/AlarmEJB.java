package com.yinhe.server.AcsServer.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.AlarmBean;
import com.yinhe.server.AcsServer.backbean.AlarmQueryBean;
import com.yinhe.server.AcsServer.data.AlarmRespository;
import com.yinhe.server.AcsServer.model.Alarm;

@Stateless
public class AlarmEJB {
	
	@Inject
	private AlarmRespository m_alarmRespository;
	
	public List<AlarmBean> getAllAlarmings(AlarmQueryBean m_queryBean){
		m_queryBean.setMaxByCount(m_alarmRespository.getAlarmingSize());
		List<AlarmBean> tempAlarmBeanList = new ArrayList<AlarmBean>();
		List<Alarm> alarmList = new ArrayList<Alarm>();
		alarmList = m_alarmRespository.getAlarmList(m_queryBean);
		int rowId = 1;
		if(null != alarmList && 0 != alarmList.size()){
			for(Alarm alarm : alarmList){
				AlarmBean bean = new AlarmBean();
				bean.setM_rowId(rowId++);
				bean.setM_id(alarm.getM_id());
				bean.setM_alarmTime(alarm.getM_alarmTime());
				bean.setM_description(alarm.getM_description());
				bean.setM_devices(alarm.getM_devices());
				bean.setM_paramPath(alarm.getM_paramPath());
				bean.setM_taskName(alarm.getM_taskName());
				tempAlarmBeanList.add(bean);
			}
		}
		return tempAlarmBeanList;
	}
	
}
