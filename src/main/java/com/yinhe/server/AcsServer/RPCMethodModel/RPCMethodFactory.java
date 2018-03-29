package com.yinhe.server.AcsServer.RPCMethodModel;

import javax.ejb.Singleton;

import com.yinhe.server.AcsServer.ejb.RPCMethod;

@Singleton
public class RPCMethodFactory {
	private RPCMethod m_abstractMethod;
	private static RPCMethodFactory m_rpcMethodFactory = null;
	
	public RPCMethodFactory() {
	}

	public synchronized static RPCMethodFactory getInstance() {
		if (m_rpcMethodFactory == null) {
			m_rpcMethodFactory = new RPCMethodFactory();
		}
		return m_rpcMethodFactory;
	}

	public RPCMethod getAbstractMethod(String req_name) throws Exception{	
		try {
			m_abstractMethod = (RPCMethod) Class.forName("com.yinhe.server.AcsServer.RPCMethodModel." + req_name).newInstance();
		} catch (Exception ex) {
			System.out.println("[getAbstractMethod]:Create "+req_name+"method fail!" + ex.getMessage());
		} 
		return m_abstractMethod;
	}
}
