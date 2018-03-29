package com.yinhe.server.AcsServer.enums;

import com.yinhe.server.AcsServer.util.Resources;

public enum ImportXmlResult {
	XML_IS_NULL,						//文件为空或格式不正确
	NO_NEED_TO_UPDATE,					//已更新到最新
	IMPORT_XML_SUCCESS,                 //导入成功
	IMPORT_XML_FAILED,					//导入失败
	IMPORT_XML_UPDATE_NODE_SUCCESS,		//导入且更新节点成功
	IMPORT_XML_UPDATE_NODE_FAILED,		//导入且更新节点失败
//	IMPORT_XML_DELETE_NODE_SUCCESS,		//导入且删除节点成功
	IMPORT_XML_DELETE_NODE_FAILED,		//导入且删除节点失败
//	IMPORT_XML_ADD_NODE_SUCCESS,		//导入且添加节点成功
	IMPORT_XML_ADD_NODE_FAILED,			//导入且添加节点失败
//	
//	IMPORT_XML_UPDATE_AND_DELETE_NODE_SUCCESS, 						//导入且更新和删除节点成功
//	IMPORT_XML_UPDATE_AND_ADD_NODE_SUCCESS,         				//导入且更新和添加节点成功
//	IMPORT_XML_UPDATE_AND_DELETE_NODE_NEED_UPDATE_SETTINGS_SUCCESS, //导入且更新和删除节点成功，关于该节点的设置和告警已被删除
	IMPORT_XML_DELETE_NODE_NEED_UPDATE_SETTINGS_SUCCESS,			//导入且删除节点成功，关于该节点的设置和告警已被删除
	IMPORT_XML_UPDATE_NODE_NEED_UPDATE_SETTINGS_SUCCESS,			//导入且更新节点成功，关于该节点的设置如有需要请重新设置
	IMPORT_XML_DELETE_AND_UPDATE_NODE_NEED_UPDATE_SETTINGS_SUCCESS; //导入且更新和删除节点成功，删除的节点相关的设置已被删除，更新的节点相关的设置如有需要请重新设置
	
	public String toLocalString() {
		return Resources.getLocalName(this.name());
	}
}
