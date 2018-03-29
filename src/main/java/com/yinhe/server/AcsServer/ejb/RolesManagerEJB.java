package com.yinhe.server.AcsServer.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.data.RolesRespository;
import com.yinhe.server.AcsServer.model.Roles;

@Stateless
public class RolesManagerEJB {

	@Inject
	private RolesRespository respository;
	
	public List<Roles> getRoleList(){
		return respository.listRoles();
	}
	
	public Roles findRoleById(Long roleId){
		if(roleId != null){
			Roles role = respository.findById(roleId);
			return role;
		}
		return null;
	}
	
}
