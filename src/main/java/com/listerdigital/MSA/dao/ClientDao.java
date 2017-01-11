package com.listerdigital.MSA.dao;

import com.jcraft.jsch.*;
import java.sql.*;
import org.slf4j.Logger;
import com.listerdigital.MSA.domain.*;
import com.listerdigital.MSA.repository.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apache.commons.io.IOUtils;
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
public class ClientDao {
	public int mailcount;
	// sftpChannel;
	// ChannelSftp sftpChannel1;
	String user = "";
	String password = "";
	String host = "";
	int port;
	String privateKey = "";
	Resource resource;
	File f;
	Logger logger = LoggerFactory.getLogger(ClientDao.class);
	Properties props = new Properties();
	InputStream fis = null;
	OracleDataSource oracleDS = null;
	public ClientDao() {
		try {
			Resource dbprop=new ClassPathResource("db.properties");
			fis=dbprop.getInputStream();
			props.load(fis);
			oracleDS = new OracleDataSource();
			oracleDS.setURL(props.getProperty("ORACLE_DB_URL"));
			oracleDS.setUser(props.getProperty("ORACLE_DB_USERNAME"));
			oracleDS.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));
			Connection con=oracleDS.getConnection();
			PreparedStatement ps = con.prepareStatement("Select * from sshcredentials_msa");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = rs.getString(1);
				host = rs.getString(2);
				port = Integer.parseInt(rs.getString(3));
				try {
					resource = new ClassPathResource("esl");
					f = resource.getFile();
					System.out.println(f.getAbsolutePath());
					privateKey = f.toString();
				} catch (Exception e) {
					System.out.println("Exception occured" + e);
				}
				// privateKey=rs.getString(4);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	public ClientDao(Client c1) throws IOException {
		try {
			Resource dbprop=new ClassPathResource("db.properties");
			fis=dbprop.getInputStream();
			props.load(fis);
			oracleDS = new OracleDataSource();
			oracleDS.setURL(props.getProperty("ORACLE_DB_URL"));
			oracleDS.setUser(props.getProperty("ORACLE_DB_USERNAME"));
			oracleDS.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));
			Connection con=oracleDS.getConnection();
			PreparedStatement ps = con.prepareStatement("Select * from sshcredentials_msa");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = rs.getString(1);
				host = rs.getString(2);
				port = Integer.parseInt(rs.getString(3));
				try {
					resource = new ClassPathResource("esl");
					f = resource.getFile();
					System.out.println(f.getAbsolutePath());
					privateKey = f.toString();
				} catch (Exception e) {
					System.out.println("Exception occured" + e);
				}
				// privateKey=rs.getString(4);
			}
		} catch (Exception e) {
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
			if (c1.getBcc().equals("")) {
				c1.setBcc(c1.getName().toLowerCase() + "@listerdigital.com");
			}
			if (c1.getImagePath().equals("")) {
				c1.setImagePath("resources/images/" + c1.getName() + ".jpg");
			}
			c1.setPercentageConsumed();
			create(sftpChannel, sftpChannel1, c1);
			sftpChannel.disconnect();
			session.disconnect();
			sftpChannel1.disconnect();
			session1.disconnect();
		} catch (JSchException | SftpException e) {
			System.out.println(e);
		}
	}

	public void create(ChannelSftp sftpChannel, ChannelSftp sftpChannel1, Client cl) throws SftpException, IOException {
		mailcount = 0;
		cl.setTotal_mails(getMails(sftpChannel, cl.getName()));
		try {
			cl.setLastUpdated(sftpChannel.lstat(cl.getName()).getMtimeString());
		} catch (Exception e) {
			Date d = new Date();
			cl.setLastUpdated(d.toString());
		}
		try {
			cl.setFolderSize(sftpChannel.lstat(cl.getName()).getSize());
		} catch (Exception e) {
			cl.setFolderSize(0);
		}
		setJson(sftpChannel, sftpChannel1, cl, cl.getName());
	}

	@SuppressWarnings("unchecked")
	public int getMails(ChannelSftp sftpChannel, String fname) throws SftpException {
		Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*");
		for (ChannelSftp.LsEntry oListItem : list) {
			if (oListItem.getAttrs().isDir()) {
				if (oListItem.getFilename().equals(fname)) { // Key Factor
					sftpChannel.cd(fname);
					// System.out.println("Inside "+fname);
					Vector<ChannelSftp.LsEntry> list1 = sftpChannel.ls("*");
					int count = 0;
					for (ChannelSftp.LsEntry file : list1) {
						if (file.getAttrs().isDir()) {
							count++;
						}
					}
					if (count == 0) {
						// System.out.println("Inside count");
						for (ChannelSftp.LsEntry file1 : list1) {
							if (!file1.getAttrs().isDir()) {
								mailcount++;
							}
						}
					}

					for (ChannelSftp.LsEntry file : list1) {
						if (file.getAttrs().isDir()) {
							getMails(sftpChannel, file.getFilename());
						}
					}
					sftpChannel.cd("..");
				}
				// sftpChannel.cd("..");
			}

		}
		return mailcount;
	}

	@SuppressWarnings("unchecked")
	public void setJson(ChannelSftp sftpChannel, ChannelSftp sftpChannel1, Client cl, String fname)
			throws SftpException, IOException {
		Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*");
		int flag = 0;
		Vector<ChannelSftp.LsEntry> fl = sftpChannel1.ls("*");
		for (ChannelSftp.LsEntry file : fl) {
			if (!file.getAttrs().isDir()) {
				if (file.getFilename().equals("client.json")) {
					flag++;
				}
			}
		}

		if (flag != 0) {
			// attrs = sftpChannel.stat("folder.json");
		} else {
			InputStream obj_InputStream = new ByteArrayInputStream("".getBytes());
			sftpChannel1.put(obj_InputStream, "client.json");
			// int t=12;
		}
		// finally {
		InputStream out = sftpChannel1.get("client.json");
		String theString = IOUtils.toString(out, "UTF-8");
		OutputStream in = sftpChannel1.put("client.json");
		BufferedWriter bw = new BufferedWriter(new PrintWriter(in));
		bw.write(getClientJson(cl, theString));
		bw.close();
		sftpChannel.cd("..");

	}

	public String getClientJson(Client cl, String theString) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		if (!theString.equals("")) {

			ClientRepository cr = mapper.readValue(theString, ClientRepository.class);
			List<Client> clients = cr.getClient();
			int flag = 0;
			for (int i = 0; i < clients.size(); i++) {
				if (clients.get(i).getName().equals(cl.getName())) {
					clients.remove(i);
					clients.add(cl);
					flag++;
				}
			}
			if (flag == 0) {
				clients.add(cl);
				cr.setTokencount(cr.getTokencount() + 1);
			}
			return (mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cr));

		} else {

			ObjectNode client = mapper.createObjectNode();
			client.put("name", cl.getName());
			client.put("bcc", cl.getBcc());
			client.put("folderSize", cl.getFolderSize());
			client.put("lastUpdated", cl.getLastUpdated());
			client.put("total_mails", cl.getTotal_mails());
			client.put("imagePath", cl.getImagePath());

			ArrayNode clientlist = mapper.createArrayNode();
			clientlist.add(client);

			ObjectNode clientTable = mapper.createObjectNode();
			clientTable.put("tokencount", "1");
			clientTable.putPOJO("client", clientlist);

			return (mapper.writerWithDefaultPrettyPrinter().writeValueAsString(clientTable));

		}
	}

	public boolean clientExists(String cname) throws IOException {
		try {
			JSch jsch = new JSch();
			jsch.addIdentity(privateKey);
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			Logger logger = LoggerFactory.getLogger(ClientDao.class);
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
			InputStream out = null;
			out = sftpChannel1.get("client.json");
			String theString = IOUtils.toString(out, "UTF-8");
			sftpChannel.disconnect();
			session.disconnect();
			sftpChannel1.disconnect();
			session1.disconnect();
			ObjectMapper mapper = new ObjectMapper();
			ClientRepository cr = mapper.readValue(theString, ClientRepository.class);
			List<Client> list = cr.getClient();
			for (int i = 0; i < list.size(); i++) {
				if (cname.equals(list.get(i).getName())) {
					return true;
				}
			}
			return false;
		} catch (JSchException | SftpException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void deleteClient(String cname) throws IOException {
		try {
			JSch jsch = new JSch();
			jsch.addIdentity(privateKey);
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			Logger logger = LoggerFactory.getLogger(ClientDao.class);
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
			InputStream out = null;
			out = sftpChannel1.get("client.json");
			String theString = IOUtils.toString(out, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			ClientRepository cr = mapper.readValue(theString, ClientRepository.class);
			List<Client> list = cr.getClient();
			for (int i = 0; i < list.size(); i++) {
				if (cname.equals(list.get(i).getName())) {
					System.out.println("Removing" + cname);
					list.remove(i);
					cr.setTokencount(cr.getTokencount() - 1);
				}
			}
			theString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cr);
			OutputStream in = sftpChannel1.put("client.json");
			BufferedWriter bw = new BufferedWriter(new PrintWriter(in));
			bw.write(theString);
			bw.close();
			sftpChannel.disconnect();
			session.disconnect();
			sftpChannel1.disconnect();
			session1.disconnect();
		} catch (JSchException | SftpException e) {
			e.printStackTrace();
		}
	}
}
