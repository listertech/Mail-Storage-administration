package com.packt.MSA.file;
import com.packt.MSA.domain.*;
import com.packt.MSA.repository.UserRepository;
import com.packt.MSA.repository.*;
import com.packt.MSA.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

public class UserMetadata {
	public boolean getUser(User u) throws JsonParseException, JsonMappingException, IOException, CloneNotSupportedException{
		FileReader fr=new FileReader();
		ObjectMapper mapper = new ObjectMapper();
		
		//List<User> userList=mapper.readValue(, valueType)
		//List<User> list = mapper.readValue(fr.getJSONString(), TypeFactory.defaultInstance().constructCollectionType(List.class, User.class));
		/*
		UserService userv=new UserService();
        userv.registerUser(u);
        */
		
		
		//Mapper to Access Repository
		
		/*
		AccessRepository acs=mapper.readValue(fr.getJSONString("access.json"), AccessRepository.class);
		
		List<Access> aclist;
		aclist=acs.getAccess();
		
		int token=acs.getTokencount();
		
		System.out.println(token);
		*/
		
		List<User> list;
		
		UserRepository usr=mapper.readValue(fr.getJSONString("user.json"), UserRepository.class);
		
		
		list=usr.getUsers();
		
		//u.setRole("UMD");
		//System.out.println(list.get(0).getRole());
		
		for(int i=0;i<usr.getTokencount();i++){
			if(u.getUsername().equals(list.get(i).getUsername()) && u.getPassword().equals(list.get(i).getPassword())){
				UserService userv=new UserService();
		        userv.registerUser(u,list.get(i));
		        return true;
			}
		}
		
		
		
		
		//usr.getUsers().add(list.get(2));
		
		//System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usr));
		
		//Overwrite the json String
		//fr.putJSONString("user.json",mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usr));
		
		
		
		
		                               
		
		//System.out.println(usr.getTokencount());
        return false;
	}
}
