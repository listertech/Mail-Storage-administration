package com.listerdigital.MSA.dao;

import com.jcraft.jsch.*;

import com.listerdigital.MSA.domain.Folder;
import com.listerdigital.MSA.repository.*;
import java.sql.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import oracle.jdbc.pool.OracleDataSource;
import java.util.Properties;
import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.annotation.*;

@SuppressWarnings("unused")
public class FolderDao {
	static List<String> pathArray = new ArrayList<String>();
	static String client[];
	String user = "";
	String password = "";
	String host = "";
	int port;
	String privateKey = "";
    Resource resource;
    File f;
    Logger logger=LoggerFactory.getLogger(FolderDao.class);
    Properties props = new Properties();
	InputStream fis = null;
	OracleDataSource oracleDS = null;
	public FolderDao() throws IOException {
		try{
			Resource dbprop=new ClassPathResource("db.properties");
			fis=dbprop.getInputStream();
			props.load(fis);
			oracleDS = new OracleDataSource();
			oracleDS.setURL(props.getProperty("ORACLE_DB_URL"));
			oracleDS.setUser(props.getProperty("ORACLE_DB_USERNAME"));
			oracleDS.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));
			Connection con=oracleDS.getConnection();
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
		try {
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
			Session session1 = jsch.getSession(user, host, port);
			session1.setPassword(password);
			java.util.Properties config1 = new java.util.Properties();
			config1.put("StrictHostKeyChecking", "no");
			session1.setConfig(config1);
			session1.connect();
			ChannelSftp sftpChannel1 = (ChannelSftp) session1.openChannel("sftp");

			sftpChannel1.connect();
			sftpChannel.cd("MSA");

			sftpChannel1.cd("MSA/metadata");
			createData(sftpChannel, sftpChannel1);
			//logger.info(sftpChannel.pwd()+","+sftpChannel1.pwd());
			//sftpChannel1.cd("Flipkart");
			//sftpChannel1.rmdir("Data");
			//sftpChannel1.cd("..");
			deleteData(sftpChannel, sftpChannel1);
			String path = "MSA";
			getPath(sftpChannel, path);
			for (int i = 0; i < pathArray.size(); i++) {
				//logger.info(pathArray.get(i));
				// logger.info(pathArray.get(i).split("/").length);
				client = pathArray.get(i).split("/");
				for (int j = 0; j < client.length; j++) {
					//logger.info(client[j]);
				}
				createFolderJson(sftpChannel1, pathArray.get(i));
			}
			
			//For deleting a folder
			
			//removeFolder("MSA/Flipkart","Emails");
			//
			
			
			//
			sftpChannel.disconnect();
			session.disconnect();
			sftpChannel1.disconnect();
			session1.disconnect();
		} catch (JSchException | SftpException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void createData(ChannelSftp sftpChannel, ChannelSftp sftpChannel1)
			throws SftpException, JSchException {
		Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*"); // List source
																// directory
																// structure.
		for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the
														// list to get
														// file/folder names.
			ChannelSftp s = sftpChannel;
			ChannelSftp s1 = sftpChannel1;
			if (oListItem.getAttrs().isDir()) {

				// logger.info(oListItem.getFilename());
				if (!oListItem.getFilename().equals("metadata")) {
					//logger.info(oListItem.getFilename());
					try {
						s1.cd(oListItem.getFilename());
					} catch (Exception e) {
						s1.mkdir(oListItem.getFilename());
						s1.cd(oListItem.getFilename());
					} finally {
						s.cd(oListItem.getFilename());
						createData(s, s1);
						s.cd("..");
						s1.cd("..");
					}
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	public  void deleteData(ChannelSftp sftpChannel, ChannelSftp sftpChannel1) throws SftpException, JSchException {
		Vector<ChannelSftp.LsEntry> list = sftpChannel1.ls("*"); 
		//Vector<ChannelSftp.LsEntry> list1 = sftpChannel.ls("*");// List source
		// directory
		// structure.
		for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the
			// list to get
			// file/folder names.
			ChannelSftp s = sftpChannel;
			ChannelSftp s1 = sftpChannel1;
			if (oListItem.getAttrs().isDir()) {

				 //logger.info(oListItem.getFilename());
				if (!oListItem.getFilename().equals("metadata")) {
					
					try {
						//logger.info("Inside"+s1.pwd()+"/"+oListItem.getFilename());
						//logger.info("Inside"+s.pwd()+"/"+oListItem.getFilename());
						s.cd(oListItem.getFilename());
						s1.cd(oListItem.getFilename());
						deleteData(s, s1);
						s1.cd("..");
						s.cd("..");
					} 
					catch (Exception e) {
						//logger.info("Inside"+s1.pwd()+"..."+oListItem.getFilename());
						s1.cd(oListItem.getFilename());
						deleteSubData(s1);
						s1.cd("..");
						s1.rmdir(oListItem.getFilename());
					}
					finally {
						
					}
				}
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public  void deleteSubData(ChannelSftp sftpChannel) throws SftpException{
		Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*");
		for (ChannelSftp.LsEntry oListItem : list){
			if(!oListItem.getAttrs().isDir()){
				sftpChannel.rm(oListItem.getFilename());
			}
			else{
				sftpChannel.cd(oListItem.getFilename());
				int flag=0;
				Vector<ChannelSftp.LsEntry> list1 = sftpChannel.ls("*");
				sftpChannel.cd("..");
				if(list1.isEmpty()){
					sftpChannel.rmdir(oListItem.getFilename());
				}
				else{
					sftpChannel.cd(oListItem.getFilename());
					deleteSubData(sftpChannel);
					sftpChannel.cd("..");
				}
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	public void getPath(ChannelSftp sftpChannel, String path) throws SftpException, JSchException {
		Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*"); // List source
																// directory
																// structure.
		if (list.size() < 1) {
			pathArray.add(path);
		} else {
			int count = 0;
			for (ChannelSftp.LsEntry li : list) {
				if (li.getAttrs().isDir()) {
					count++;
				}
			}
			if (count == 0) {
				pathArray.add(path);
			}
		}
		for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the
														// list to get
														// file/folder names.
			if (oListItem.getAttrs().isDir()) {

				// logger.info(oListItem.getFilename());
				if (!oListItem.getFilename().equals("metadata")) {
					sftpChannel.cd(oListItem.getFilename());
					getPath(sftpChannel, path + "/" + oListItem.getFilename());
					sftpChannel.cd("..");
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	public  void createFolderJson(ChannelSftp sftpChannel, String path) throws SftpException, IOException {
		Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*");
		for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the
														// list to get
														// file/folder names.
			if (oListItem.getAttrs().isDir()) {
				int flag = 0;
				// logger.info(client[2]);
				if (oListItem.getFilename().equals(client[1])) {
					sftpChannel.cd(client[1]);
					// attrs =
					// sftpChannel.stat("MSA/metadata/"+client[2]+"/folder.json");
					Vector<ChannelSftp.LsEntry> fl = sftpChannel.ls("*");
					for (ChannelSftp.LsEntry file : fl) {
						if (!file.getAttrs().isDir()) {
							if (file.getFilename().equals("folder.json")) {
								flag++;
							}
						}
					}

					if (flag != 0) {
						// attrs = sftpChannel.stat("folder.json");
					} else {
						InputStream obj_InputStream = new ByteArrayInputStream("".getBytes());
						sftpChannel.put(obj_InputStream, "folder.json");
						// int t=12;
					}
					// finally {
					InputStream out = sftpChannel.get("folder.json");
					String theString = IOUtils.toString(out, "UTF-8");
					Folder f = new Folder();
					f.setPath(path);
					f.setName(client[(client.length) - 1]);
					OutputStream in = sftpChannel.put("folder.json");
					BufferedWriter bw = new BufferedWriter(new PrintWriter(in));
					bw.write(getFolderJson(f, theString));
					bw.close();
					sftpChannel.cd("..");
					// }
				}

			}
		}
	}

	public  String getFolderJson(Folder f, String theString)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		if (!theString.equals("")) {

			FolderRepository fr = mapper.readValue(theString, FolderRepository.class);
			List<Folder> folders = fr.getFolder();
			int flag = 0;
			for (int i = 0; i < folders.size(); i++) {
				if (folders.get(i).getPath().equals(f.getPath())) {
					folders.remove(i);
					folders.add(f);
					flag++;
				}
			}
			if (flag == 0) {
				folders.add(f);
				fr.setTokencount(fr.getTokencount() + 1);
			}
			return (mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fr));

		} else {

			ObjectNode folder = mapper.createObjectNode();
			folder.put("name", f.getName());
			folder.put("path", f.getPath());

			ArrayNode folderlist = mapper.createArrayNode();
			folderlist.add(folder);

			ObjectNode folderTable = mapper.createObjectNode();
			folderTable.put("tokencount", "1");
			folderTable.putPOJO("folder", folderlist);

			return (mapper.writerWithDefaultPrettyPrinter().writeValueAsString(folderTable));

		}
	}
	
	
	@SuppressWarnings("unchecked")
	public  void removeFolderJson(ChannelSftp sftpChannel, Folder f) throws SftpException, IOException {
		Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*");
		for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the
														// list to get
														// file/folder names.
			if (oListItem.getAttrs().isDir()) {
				int flag = 0;
				// logger.info(client[2]);
				if (oListItem.getFilename().equals(client[1])) {
					sftpChannel.cd(client[1]);
					// attrs =
					// sftpChannel.stat("MSA/metadata/"+client[2]+"/folder.json");
					Vector<ChannelSftp.LsEntry> fl = sftpChannel.ls("*");
					for (ChannelSftp.LsEntry file : fl) {
						if (!file.getAttrs().isDir()) {
							if (file.getFilename().equals("folder.json")) {
								flag++;
							}
						}
					}

					if (flag != 0) {
						// attrs = sftpChannel.stat("folder.json");
					} else {
						InputStream obj_InputStream = new ByteArrayInputStream("".getBytes());
						sftpChannel.put(obj_InputStream, "folder.json");
						// int t=12;
					}
					// finally {
					InputStream out = sftpChannel.get("folder.json");
					String theString = IOUtils.toString(out, "UTF-8");
					OutputStream in = sftpChannel.put("folder.json");
					BufferedWriter bw = new BufferedWriter(new PrintWriter(in));
					bw.write(deleteFolderJson(f,theString));
					bw.close();
					sftpChannel.cd("..");
					// }
				}

			}
		}
	}
	
	
	public  String deleteFolderJson(Folder f, String theString) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();

		if (!theString.equals("")) {

			FolderRepository fr = mapper.readValue(theString, FolderRepository.class);
			List<Folder> folders = fr.getFolder();
			int flag = 0;
			for (int i = 0; i < folders.size(); i++) {
				if (folders.get(i).getPath().equals(f.getPath())) {
					folders.remove(i);
					flag++;
				}
			}
			if (flag != 0) {
				fr.setTokencount(fr.getTokencount()-flag);
			}
			return (mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fr));

		} else {

			return (theString);

		}
	}
	public void createFolder(String path,String fname){
		try{
			Resource dbprop=new ClassPathResource("db.properties");
			fis=dbprop.getInputStream();
			props.load(fis);
			oracleDS = new OracleDataSource();
			oracleDS.setURL(props.getProperty("ORACLE_DB_URL"));
			oracleDS.setUser(props.getProperty("ORACLE_DB_USERNAME"));
			oracleDS.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));
			Connection con=oracleDS.getConnection();
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
		try {
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
			logger.info("Crating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");

			sftpChannel.connect();

			logger.info("SFTP Channel created.");
			Session session1 = jsch.getSession(user, host, port);
			session1.setPassword(password);
			java.util.Properties config1 = new java.util.Properties();
			config1.put("StrictHostKeyChecking", "no");
			session1.setConfig(config1);
			session1.connect();
			ChannelSftp sftpChannel1 = (ChannelSftp) session1.openChannel("sftp");
			sftpChannel1.connect();
			sftpChannel.cd(path);
			try{
				sftpChannel.cd(fname);
			}
			catch(Exception e){
				sftpChannel.mkdir(fname);
				sftpChannel.cd(fname);
			}
			sftpChannel.cd("..");
			
			sftpChannel.disconnect();
			session.disconnect();
			sftpChannel1.disconnect();
			session1.disconnect();
		} catch (JSchException | SftpException e) {
			e.printStackTrace();
		}
	}
	public void removeFolder(String path,String name) throws IOException{
		try{
			Resource dbprop=new ClassPathResource("db.properties");
			fis=dbprop.getInputStream();
			props.load(fis);
			oracleDS = new OracleDataSource();
			oracleDS.setURL(props.getProperty("ORACLE_DB_URL"));
			oracleDS.setUser(props.getProperty("ORACLE_DB_USERNAME"));
			oracleDS.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));
			Connection con=oracleDS.getConnection();
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
		try {
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
			logger.info("Crating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");

			sftpChannel.connect();

			logger.info("SFTP Channel created.");
			Session session1 = jsch.getSession(user, host, port);
			session1.setPassword(password);
			java.util.Properties config1 = new java.util.Properties();
			config1.put("StrictHostKeyChecking", "no");
			session1.setConfig(config1);
			session1.connect();
			ChannelSftp sftpChannel1 = (ChannelSftp) session1.openChannel("sftp");

			sftpChannel1.connect();
			sftpChannel1.cd("MSA/metadata");
			Folder f=new Folder();
			f.setPath(path+"/"+name);
			f.setName(name);
			client = f.getPath().split("/");
			removeFolderJson(sftpChannel1,f);
			remove(sftpChannel,path,name);
			sftpChannel1.cd("..");
			sftpChannel.disconnect();
			session.disconnect();
			sftpChannel1.disconnect();
			session1.disconnect();
		}
		catch (JSchException | SftpException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void remove(ChannelSftp sftpChannel,String path,String name) throws SftpException{
		try{
			sftpChannel.cd(path);
			sftpChannel.rmdir(name);
		}
		catch(Exception e){
			sftpChannel.cd(name);
			Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*");
			for (ChannelSftp.LsEntry oListItem : list) {
				if(!oListItem.getAttrs().isDir()){
					sftpChannel.rm(oListItem.getFilename());
				}
				else{
					sftpChannel.cd(oListItem.getFilename());
					deleteSubData(sftpChannel);
					sftpChannel.cd("..");
					sftpChannel.rmdir(oListItem.getFilename());
				}
			}
			sftpChannel.cd("..");
			sftpChannel.rmdir(name);
		}
	}
}

