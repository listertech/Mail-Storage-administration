package com.listerdigital.MSA.service;

import java.io.IOException;

//import com.listerdigital.MSA.dao.AWSClientDao;
import com.listerdigital.MSA.dao.ClientDao;
import com.listerdigital.MSA.domain.Client;

public class Clientservice {
	ClientDao cd;
	public Clientservice(Client client) throws IOException{
		cd=new ClientDao(client);
	}
	public Clientservice() throws IOException{
		cd=new ClientDao();
	}
	public boolean clientExists(String name) throws IOException{
		return cd.clientExists(name);
	}
	public void deleteClient(String cname) throws IOException{
		cd.deleteClient(cname);
	}
}
