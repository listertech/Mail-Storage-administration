package com.listerdigital.MSA.file;
import java.util.List;

import com.listerdigital.MSA.dao.ClientDao;
import com.listerdigital.MSA.domain.*;
import com.listerdigital.MSA.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import java.io.*;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.sql.*;

import oracle.jdbc.pool.OracleDataSource;
import java.util.Properties;
import javax.sql.DataSource;

@SuppressWarnings("unused")
public class ClientFile {
	String user = "";
    String password = "";
    String host = "";
    int port=22;
    String theString="";
    String privateKey="";
    Resource resource;
    File f;
    List<Client> clientList;
    Logger logger=LoggerFactory.getLogger(ClientFile.class);
    Properties props = new Properties();
	InputStream fis = null;
	OracleDataSource oracleDS = null;
	public List<Client> getallClients(){
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
	        logger.info("Crating SFTP Channel.");
	        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
	        //ChannelSftp sftpChannel2 = (ChannelSftp) session.openChannel("sftp");
	        sftpChannel.connect();
	        //sftpChannel2.connect();
	        logger.info("SFTP Channel created.");
	        sftpChannel.cd("MSA/metadata");
			//InputStream obj_InputStream = new ByteArrayInputStream("".getBytes());
		    //sftpChannel.put(obj_InputStream,remoteFile);
	        InputStream out= null;
	        out= sftpChannel.get("client.json");
	        theString = IOUtils.toString(out,"UTF-8");
	        ObjectMapper mapper=new ObjectMapper();
	        ClientRepository cr=mapper.readValue(theString, ClientRepository.class);
	        for(int i=0;i<cr.getTokencount();i++){
	        	new ClientDao(cr.getClient().get(i));
	        }
	        cr=mapper.readValue(theString, ClientRepository.class);
	        clientList=cr.getClient();
	        //logger.info("String succesfully converted:"+theString);
			sftpChannel.disconnect();
	        session.disconnect();
	    }
	    catch(JSchException | SftpException | IOException e)
		{
		    e.printStackTrace();;
		}
		return clientList;
	}
}
