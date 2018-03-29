package com.yinhe.server.AcsServer.ejb;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.RPCMethodModel.Inform;
import com.yinhe.server.AcsServer.struct.Event;
import com.yinhe.server.AcsServer.struct.EventStruct;


@Stateless
public class InformParse {
	@Inject
	private InformHandleEJB informHandle;	
	@Inject
	private Logger m_log;
	public InformHandleEJB getInformHandle()
	{
		return informHandle;
	}
	
	public String parseInform(CPEProcess cpeProcess,Inform inform)
	{
		String eventString = "";
		try
		{
			EventStruct[] events = inform.getEvent().getEventCodes();
			
			for (EventStruct eventStruct : events)
			{
				m_log.info("[parseInform] eventCode = " + eventStruct.getEvenCode());
				if(cpeProcess == null){
					m_log.info("[parseInform] cpeProcess == null!");
				}
				if(eventStruct.getEvenCode().equals(Event.BOOTSTRAP))
				{
					informHandle.bootStrapEvent(cpeProcess,inform);
					eventString = Event.BOOTSTRAP;
				}
				else if(eventStruct.getEvenCode().equals(Event.BOOT))
				{
					informHandle.bootEvent(cpeProcess,inform);
					eventString = Event.BOOT;		
				}
				else if(eventStruct.getEvenCode().equals(Event.CONNECTION_REQUEST))
				{
					informHandle.connectionRequestEvent(cpeProcess,inform);
					eventString = Event.CONNECTION_REQUEST;
				}
				
				else if(eventStruct.getEvenCode().equals(Event.TRANSFER_COMPLETE))
				{
					informHandle.transferCompleteEvent(cpeProcess,inform);
					eventString = Event.TRANSFER_COMPLETE;
				}
				else if(eventStruct.getEvenCode().equals(Event.VALUE_CHANGE))
				{
					informHandle.valueChangeEvent(cpeProcess,inform);
					eventString = Event.VALUE_CHANGE;
				}
				else if(eventStruct.getEvenCode().equals(Event.DIALGONSTICS_COMPLETE))
				{
					informHandle.diagnosticsCompleteEvent(cpeProcess,eventStruct.getCommandKey());
					eventString = Event.DIALGONSTICS_COMPLETE;
				}
				else if(eventStruct.getEvenCode().equals(Event.KICKED))
				{
					informHandle.kickedEvent(cpeProcess,inform);
					eventString = Event.KICKED;
				}
				else if(eventStruct.getEvenCode().equals(Event.PERIODIC))
				{
					informHandle.periodicEvent(cpeProcess,inform);       //更新数据库的lastContact
					eventString = Event.PERIODIC;
				}
				else if(eventStruct.getEvenCode().equals(Event.SCHEDULED))
				{
					informHandle.scheduleEvent(cpeProcess,inform);
					eventString = Event.SCHEDULED;
				}
			}
			
		} catch (Exception e)
		{
			m_log.info("[parseInform] exception " + e.getMessage());
			eventString = "ParseInform Error!";
			return eventString;
		}
	 return eventString;
	}

}
