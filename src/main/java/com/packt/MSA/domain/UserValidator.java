package com.packt.MSA.domain;

import java.io.IOException;

import org.springframework.validation.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.packt.MSA.file.UserMetadata;

public class UserValidator implements Validator {
	public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    
	public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "Username", "Username.empty");
        User u=(User) obj;
        UserMetadata umd=new UserMetadata();
        try {
			if(!umd.getUser(u)){
				e.rejectValue("password", "error.login","Either the username or password is incorrect");
			}
			
			
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //u.setRole("SUPER_USER");
        /*
        if (!(u.getUsername().equals("souvikduttachoudhury32@gmail.com"))) {
            e.rejectValue("username", "error.username","wrong username");
        } else if (!(u.getPassword().equals("lister"))) {
            e.rejectValue("password", "error.password","wrong password");
        }
        */ catch (CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

}
