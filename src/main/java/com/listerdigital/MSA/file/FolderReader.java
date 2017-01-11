package com.listerdigital.MSA.file;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.*;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.*;
import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.listerdigital.MSA.dao.ClientDaoTest;
import com.listerdigital.MSA.domain.*;

@SuppressWarnings("unused")
public class FolderReader {
	String user = "";
    String password = "";
    String host = "";
    int port=22;
    String theString="";
    String privateKey = "";
    Resource resource;
    File f;
    List<String> folderlist;
    Logger logger=LoggerFactory.getLogger(FolderReader.class);
	@SuppressWarnings("unchecked")
	public List<String> getFolders(String dir){
	    try{
	    	Class.forName("oracle.jdbc.driver.OracleDriver");
		    Connection con=DriverManager.getConnection("jdbc:oracle:thin:@10.106.20.63:1521:dexter","training5","training5");
		    PreparedStatement ps=con.prepareStatement("Select * from sshcredentials_msa");
		    ResultSet rs=ps.executeQuery();
		    while(rs.next()){
		    	user=rs.getString(1);
		    	host=rs.getString(2);
		    	port=Integer.parseInt(rs.getString(3));
		    	try{
		    		resource = new ClassPathResource("esl");
			    	f=resource.getFile();
			    	logger.info(f.getAbsolutePath());
			    	privateKey= f.toString();
		    	}
		    	catch(Exception e){
		    		logger.info("Exception occured"+e);
		    	}
		    	//privateKey=rs.getString(4);
		    }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    try
	    {
	        JSch jsch = new JSch();
	        jsch.addIdentity(privateKey);
	        Session session = jsch.getSession(user, host, port);
	        session.setPassword(password);
	        java.util.Properties config = new java.util.Properties(); 
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        logger.info("Establishing Connection...");
	        session.connect();
	        logger.info("Connection established.");
	        logger.info("Creating SFTP Channel.");
	        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
	        sftpChannel.connect();
	        logger.info("SFTP Channel created.");
	        logger.info(sftpChannel.pwd());
	        logger.info(dir);
	        sftpChannel.cd(dir);
	        logger.info(sftpChannel.pwd());
	        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*");
	        folderlist=new ArrayList<String>();
	        for (ChannelSftp.LsEntry oListItem : list) {
	        	if(oListItem.getAttrs().isDir()){
	        		folderlist.add(oListItem.getFilename());
	        	}
	        }
			sftpChannel.disconnect();
	        session.disconnect();
	    }
	    catch(JSchException | SftpException e)
		{
		    e.printStackTrace();
		}
	    return folderlist;
	}
}
