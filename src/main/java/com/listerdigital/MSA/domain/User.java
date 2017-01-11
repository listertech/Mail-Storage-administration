package com.listerdigital.MSA.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
 

public class User implements Cloneable {
    @NotEmpty
    @Email
    private String username;
     
    @NotEmpty(message = "Please enter your password.")
    @Size(min = 6, max = 15, message = "Your password must between 6 and 15 characters")
    private String password;

    private String role;
    
    public User(){
    	
    }
    
    public Object clone()throws CloneNotSupportedException{  
    	return super.clone();  
    }  


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
 
   
}
