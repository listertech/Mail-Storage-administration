package com.packt.MSA.service;

import com.packt.MSA.domain.*;

public class UserService {
	public void registerUser(User u,User v) throws CloneNotSupportedException{
		//System.out.println(v.getRole()+","+u.getRole());
		u.setRole(v.getRole());
	}
}
