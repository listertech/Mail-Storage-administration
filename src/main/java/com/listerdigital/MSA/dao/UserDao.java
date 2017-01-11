package com.listerdigital.MSA.dao;

import com.listerdigital.MSA.domain.*;

public class UserDao {
	public void registerUser(User u,User v) throws CloneNotSupportedException{
		//System.out.println(v.getRole()+","+u.getRole());
		u.setRole(v.getRole());
	}
}
