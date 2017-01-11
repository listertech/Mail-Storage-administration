package com.listerdigital.MSA.repository;
import java.util.List;

import com.listerdigital.MSA.domain.*;

public class FolderRepository {
	private int tokencount;
	private List<Folder> folder;
	public int getTokencount() {
		return tokencount;
	}
	public void setTokencount(int tokencount) {
		this.tokencount = tokencount;
	}
	public List<Folder> getFolder() {
		return folder;
	}
	public void setFolder(List<Folder> folder) {
		this.folder = folder;
	}
	
}
