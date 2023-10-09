package org.unibl.etf.User;


import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import org.unibl.etf.Utils.DigitalEnvelope;
import org.unibl.etf.Utils.HashUtils;
import org.unibl.etf.Utils.UtilsConfig;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String username;
	private String password;
	private int numOfWrongEntries=0;
	private String fileSystemPath;
	private String symmetricKey;
	private String privateKey;
	public User(String name,String pass) {
		this.username=name;
		try {
			this.password=HashUtils.getHash(pass);
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		UtilsConfig.executeCommand("mkdir ./Download/"+name,false);
		UtilsConfig.executeCommand("mkdir ./FileSystem/"+name,false);
		fileSystemPath="./FileSystem/"+name;
		DigitalEnvelope.generateSymmetricKey(name);
		symmetricKey=fileSystemPath+"/"+name+"SymmKey.enc";
		privateKey=UtilsConfig.PRIVATE_KEY_PATH+"/"+username+"_PrivateKey.key";
	}
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public int getNumOfWrongEntries() {
		return this.numOfWrongEntries;
	}
	public void setNumOfWrongEntries(int num) {
		this.numOfWrongEntries=num;
	}
	public String getFileSystemPath() {
		return fileSystemPath;
	}
	public String getSymmetricKey() {
		return symmetricKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	@Override 
	public String toString() {
		return username+" "+password;
	}

}
