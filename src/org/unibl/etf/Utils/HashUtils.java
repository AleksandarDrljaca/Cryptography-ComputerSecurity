package org.unibl.etf.Utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class HashUtils {
	
	public static String getHash(String string) throws NoSuchAlgorithmException {
		
		MessageDigest md=MessageDigest.getInstance("SHA-512");
		md.update(string.getBytes());
		byte[] hash = md.digest();
		return bytesToHex(hash);
	}
	public static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}

}
