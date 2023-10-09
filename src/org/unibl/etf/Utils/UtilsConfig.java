package org.unibl.etf.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class UtilsConfig {
	
	public static final String REQUESTS_PATH="./src/PKI/requests";
	public static final String PRIVATE_KEY_PATH="./src/PKI/private";
	public static final String CRL_PATH="./src/PKI/crl";
	public static final String NEWCERTS_PATH="./src/PKI/newcerts";
	public static final String CERTS_PATH="./src/PKI/certs";
	public static final String ROOT_KEY_PATH="./src/PKI/private/private4096.key";
	public static final String ROOT_CERT_PATH="./src/PKI/rootca.pem";
	public static final String OPENSSL_CFG_PATH="./src/PKI/openssl.cnf";
	public static final String INDEX_PATH="./src/PKI/index.txt";
	public static UserDB dataBase=new UserDB();
	/*public UtilsConfig() {
		dataBase=UserDB.loadDB();
	}*/

	public static StringBuilder executeCommand(String command, boolean out) {
		int exitVal=-1;
		StringBuilder output=null;
		try {
			Process process = Runtime.getRuntime().exec(command);
	        
			output=new StringBuilder();
			BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line=reader.readLine())!=null) {
				output.append(line+"\n");
				
				
			}
			exitVal=process.waitFor();
			
		}catch(InterruptedException|IOException e) {
			e.printStackTrace();
		}
		
		
		if(exitVal==0) {
			System.out.println("Success");
			System.out.println(output);
			if(out) return output;
			
				
			
			
		}
		else System.out.println("Error occured!");
		return null;
	}
	public static void executeCommand(String[] command) {
		 StringBuilder output = new StringBuilder();
	        try {
	            Process process = new ProcessBuilder(command).start();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

	            String line;
	            while ((line = reader.readLine()) != null) {
	                output.append(line).append("\n");
	            }

	            int exitValue = process.waitFor();
	            if (exitValue == 0) {
	                System.out.println("Successful execution. Output: ");
	                System.out.println(output);
	            } else {
	                System.out.println("Execution failed with exit code " + exitValue + ". Output: ");
	                System.out.println(output);
	            }
	        } catch (InterruptedException | IOException e) {
	            System.out.println("Error executing command: " + e.getMessage());
	        }
		
	}

	
	

}
