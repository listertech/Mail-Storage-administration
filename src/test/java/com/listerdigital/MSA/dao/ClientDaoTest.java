package com.listerdigital.MSA.dao;

import org.junit.*;
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
public class ClientDaoTest {
	private static ClientDao clientdao;
	private static Client client;
	
	Logger logger=LoggerFactory.getLogger(ClientDaoTest.class);
	
	@BeforeClass
	public static void initClientDao() throws IOException{
		clientdao=new ClientDao();
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
		client=new Client();
		client.setName("TestClient");
		client.setBcc("testbcc@listerdigital.com");
		clientdao=new ClientDao(client);
	}
}
