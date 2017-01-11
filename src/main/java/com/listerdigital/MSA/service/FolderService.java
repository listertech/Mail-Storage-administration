package com.listerdigital.MSA.service;

import java.io.IOException;

import com.listerdigital.MSA.dao.FolderDao;

public class Folderservice {
	FolderDao fd;
	public Folderservice() throws IOException{
		fd=new FolderDao();
	}
	public void removeFolder(String currentdir,String folder) throws IOException{
		fd.removeFolder(currentdir, folder);
	}
	public void createFolder(String currentdir,String folder) throws IOException{
		fd.createFolder(currentdir, folder);
	}
}
