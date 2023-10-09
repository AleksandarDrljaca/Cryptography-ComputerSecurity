package org.unibl.etf.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


import org.unibl.etf.User.User;

public class UserDB  {
	
	private  List<User> users;
	
	@SuppressWarnings("unchecked")
	public UserDB() {
		File f=new File("list.ser");
		if(!f.exists()) {
			users=new ArrayList<User>();
			System.out.println("Empty");
		}
		else {
			System.out.println("Not empty");
			users=(List<User>) SerializationUtil.loadDB();
			users.stream().forEach(e->System.out.println(e));
		}
		
	}
	public void serializeList() {
		SerializationUtil.saveDB(users);
	}
	public boolean userExists(String user) {
		Predicate<User> isMatch=p -> p.getUsername().equals(user);
		return users.stream().anyMatch(isMatch);
	}
	
	public void addToDB(String user,String password){
		users.add(new User(user,password));
		serializeList();
	}
	
	public User getUser(String user){
		
		User currUser=null;
		for(User u:users) {
			if(u.getUsername().equals(user))
				currUser=u;
		}
		return currUser;
		
	}
	
	
	
	
}
