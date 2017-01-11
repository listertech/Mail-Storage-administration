package com.listerdigital.MSA.dao;

import com.listerdigital.MSA.domain.Folder;
import java.io.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FolderDaoTest {
	@SuppressWarnings("unused")
	private static FolderDao folderdao;
	@SuppressWarnings("unused")
	private static Folder folder;
	
	Logger logger=LoggerFactory.getLogger(FolderDaoTest.class);
	
	@BeforeClass
	public static void initFolderDao() throws IOException{
		folderdao=new FolderDao();
	}
	
	@Before
	public void beforeTest(){
		logger.info("Testing started");
	}
	
	@After
	public void afterTest(){
		logger.info("Testing completed");
	}
	
	@Test
	public void check() throws IOException{
		logger.info("test going on...");
		try{
		 folderdao=new FolderDao();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

