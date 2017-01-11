package com.listerdigital.MSA.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.listerdigital.MSA.domain.*;

@Repository
public class AccessRepository {
	private int tokencount;
	private List<Access> access;
	public int getTokencount() {
		return tokencount;
	}
	public void setTokencount(int tokencount) {
		this.tokencount = tokencount;
	}
	public List<Access> getAccess() {
		return access;
	}
	public void setAccess(List<Access> access) {
		this.access = access;
	}
	
}
