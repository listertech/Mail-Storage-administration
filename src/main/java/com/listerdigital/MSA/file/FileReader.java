package com.listerdigital.MSA.file;

import com.jcraft.jsch.*;

import oracle.jdbc.pool.OracleDataSource;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.sql.*;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import java.util.Properties;
import javax.sql.DataSource;

@SuppressWarnings("unused")
public class FileReader {
	String user = "";
    String password = "";
    String host = "";
    int port=22;
    String theString="";
    String privateKey = "";
    Resource resource;
    File f;
    Properties props = new Properties();
	InputStream fis = null;
	OracleDataSource oracleDS = null;
    public String getJSONString(String remoteFile){
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
		    	System.out.println(f.getAbsolutePath());
		    	privateKey= f.toString();
	    	}
	    	catch(Exception e){
	    		System.out.println("Exception occured"+e);
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
        System.out.println("Establishing Connection...");
        session.connect();
        System.out.println("Connection established.");
        System.out.println("Crating SFTP Channel.");
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        //ChannelSftp sftpChannel2 = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        //sftpChannel2.connect();
        System.out.println("SFTP Channel created.");
        
        /*sftpChannel.cd("Souvik/newfolder");
        File f=new File(remoteFile);
        sftpChannel.put(new FileInputStream(f), f.getName());*/
        //sftpChannel.mkdir("MSA");
        //sftpChannel.mkdir("MSA/metadata");
        sftpChannel.cd("MSA/metadata");
		//InputStream obj_InputStream = new ByteArrayInputStream("".getBytes());
	    //sftpChannel.put(obj_InputStream,remoteFile);
        InputStream out= null;
        out= sftpChannel.get(remoteFile);
        theString = IOUtils.toString(out,"UTF-8");
        //System.out.println("String succesfully converted:"+theString);
		sftpChannel.disconnect();
        session.disconnect();
    }
    catch(JSchException | SftpException | IOException e)
	{
	    System.out.println(e);
	}
    return theString;
}
    public void putJSONString(String remoteFile,String theString){
    	try
        {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKey);
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties(); 
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            //ChannelSftp sftpChannel2 = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            //sftpChannel2.connect();
            System.out.println("SFTP Channel created.");
            
            /*sftpChannel.cd("Souvik/newfolder");
            File f=new File(remoteFile);
            sftpChannel.put(new FileInputStream(f), f.getName());*/
            //sftpChannel.mkdir("MSA");
            //sftpChannel.mkdir("MSA/metadata");
            sftpChannel.cd("MSA/metadata");
    		//InputStream obj_InputStream = new ByteArrayInputStream("".getBytes());
    	    //sftpChannel.put(obj_InputStream,remoteFile);
            OutputStream in= null;
            in= sftpChannel.put(remoteFile);
            BufferedWriter bw = new BufferedWriter(new PrintWriter(in));
    		bw.write(theString);
    		bw.close();
            //System.out.println("String succesfully converted:"+theString);
    		sftpChannel.disconnect();
            session.disconnect();
        }
        catch(JSchException | SftpException | IOException e)
    	{
    	    System.out.println(e);
    	}
    }
	
}
