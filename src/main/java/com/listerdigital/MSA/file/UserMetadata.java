package com.listerdigital.MSA.file;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.listerdigital.MSA.domain.*;
import com.listerdigital.MSA.repository.*;

import oracle.jdbc.pool.OracleDataSource;
import java.util.Properties;
import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

@SuppressWarnings("unused")
public class UserMetadata {
	public boolean getUser(User u) throws JsonParseException, JsonMappingException, IOException, CloneNotSupportedException{
		FileReader fr=new FileReader();
		ObjectMapper mapper = new ObjectMapper();
		String username;
		String password;
		String role;
		Properties props = new Properties();
		InputStream fis = null;
		OracleDataSource oracleDS = null;
		
		//List<User> list;
		
		//UserRepository usr=mapper.readValue(fr.getJSONString("user.json"), UserRepository.class);
		
		
		//list=usr.getUsers();
		
		try{
			Resource dbprop=new ClassPathResource("db.properties");
			fis=dbprop.getInputStream();
			props.load(fis);
			oracleDS = new OracleDataSource();
			oracleDS.setURL(props.getProperty("ORACLE_DB_URL"));
			oracleDS.setUser(props.getProperty("ORACLE_DB_USERNAME"));
			oracleDS.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));
			Connection con=oracleDS.getConnection();
		    PreparedStatement ps=con.prepareStatement("Select * from user_msa");
		    ResultSet rs=ps.executeQuery();
		    while(rs.next()){
		    	username=rs.getString("username");
		    	password=rs.getString("password");
		    	role=rs.getString("role");
		    	if(u.getUsername().equals(username) && u.getPassword().equals(password)){
					u.setRole(role);
			        return true;
				}
		    }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
		
		/*
		
		for(int i=0;i<usr.getTokencount();i++){
			if(u.getUsername().equals(list.get(i).getUsername()) && u.getPassword().equals(list.get(i).getPassword())){
				UserService userv=new UserService();
		        userv.registerUser(u,list.get(i));
		        return true;
			}
		}
		
		*/
        return false;
	}
}
