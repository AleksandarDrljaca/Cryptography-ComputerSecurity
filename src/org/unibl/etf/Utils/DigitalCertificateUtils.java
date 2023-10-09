package org.unibl.etf.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DigitalCertificateUtils {
	
		

		public static void createCertificateRequest(String cn,String o,String ou,String l,String st,String c) {
			generateRSAKeyPair(cn);
			UtilsConfig.executeCommand("openssl req -new -out "+ UtilsConfig.REQUESTS_PATH + "/" + cn+".csr"+" -key "+ UtilsConfig.PRIVATE_KEY_PATH+"/"+cn+"_PrivateKey" + ".key"+" -config "+UtilsConfig.OPENSSL_CFG_PATH
					+ " -subj " + "/CN=" + cn + "/O=" + o +"/OU=" + ou + "/L=" + l +"/ST=" + st +"/C=" + c,false);
		}
		public static void signCertificateRequest(String name) {
			UtilsConfig.executeCommand("openssl ca -batch -in " + UtilsConfig.REQUESTS_PATH + "/"+name+".csr " + "-out " + UtilsConfig.CERTS_PATH + "/" + name + ".crt" + " -config " + UtilsConfig.OPENSSL_CFG_PATH+" -key sigurnost -days 180",false);
			
		}
		public static boolean checkCertValidity(String name) {
			StringBuilder str=UtilsConfig.executeCommand("openssl verify -CAfile "+UtilsConfig.ROOT_CERT_PATH+" -verbose "+UtilsConfig.CERTS_PATH+"/"+name+".crt",true);
			String x=str.toString();
			if(!x.contains("OK")) return false;
			return true;
			
		}
		public static boolean certBelongsTo(String fstLogin,String ndLogin) {
			String subjInfo=UtilsConfig.executeCommand("openssl x509 -noout -subject -in "+UtilsConfig.CERTS_PATH+"/"+fstLogin+".crt",true).toString();
			return subjInfo.contains(ndLogin);
		}
		
		public static void generateRSAKeyPair(String name) {
			UtilsConfig.executeCommand("openssl genrsa -out "+UtilsConfig.PRIVATE_KEY_PATH+"/"+name+"_PrivateKey.key"+" 2048",false);
		}
		public static void revokeCertificate(String user) {
			UtilsConfig.executeCommand("openssl ca -revoke "+UtilsConfig.CERTS_PATH+"/"+user+".crt" +" -crl_reason certificateHold -config "+UtilsConfig.OPENSSL_CFG_PATH+" -passin pass:sigurnost",true);
			UtilsConfig.executeCommand("openssl ca -gencrl -out "+UtilsConfig.CRL_PATH+"/list.crl"+" -config "+UtilsConfig.OPENSSL_CFG_PATH+" -passin pass:sigurnost",false);
		}
		
		public static void reactivateCert(String user) throws IOException {
			
			String replacement=null;
			List<String> lines=new ArrayList<String>();
			try (BufferedReader reader = new BufferedReader(new FileReader(UtilsConfig.INDEX_PATH))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	
	            	if(line.contains(user+"/")) {
	            		
	                	String[] parts = line.split("\t");
	                	replacement="V   "+parts[1]+"       "+parts[3]+"  "+parts[4]+" "+parts[5];
	                	lines.add(replacement);
	                	
	                	
	                }else lines.add(line);
	            }
	           
	        
			 PrintWriter pw = null;
			
				pw=new PrintWriter(new BufferedWriter(new FileWriter(new File(UtilsConfig.INDEX_PATH))),false);
				for(int i=0;i<lines.size();i++) {
					if(i!=lines.size()-1) {
						pw.println(lines.get(i));
					}else {
						pw.print(lines.get(i));
					}
				}
				
				pw.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	
			UtilsConfig.executeCommand("openssl ca -gencrl -out "+UtilsConfig.CRL_PATH+"/list.crl"+" -config "+UtilsConfig.OPENSSL_CFG_PATH+" -passin pass:sigurnost",false);
		}
		
}
