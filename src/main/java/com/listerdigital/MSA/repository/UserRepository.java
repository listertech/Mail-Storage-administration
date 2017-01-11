package com.listerdigital.MSA.repository;
import java.util.*;

import org.springframework.stereotype.Repository;

import com.listerdigital.MSA.domain.*;

@Repository
public class UserRepository {
	private int tokencount;
	private List<User> users;
	public int getTokencount() {
		return tokencount;
	}
	public void setTokencount(int tokencount) {
		this.tokencount = tokencount;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
