package com.packt.MSA.domain;

public class Permission {
	private boolean createclient;
	private boolean setstoragepath;
	private boolean assignbcc;
	public boolean isCreateclient() {
		return createclient;
	}
	public void setCreateclient(boolean createclient) {
		this.createclient = createclient;
	}
	public boolean isSetstoragepath() {
		return setstoragepath;
	}
	public void setSetstoragepath(boolean setstoragepath) {
		this.setstoragepath = setstoragepath;
	}
	public boolean isAssignbcc() {
		return assignbcc;
	}
	public void setAssignbcc(boolean assignbcc) {
		this.assignbcc = assignbcc;
	}
	
}
