package org.unibl.etf.Utils;

import org.unibl.etf.User.User;

public class DigitalSignature {

	public static void signFileSegment(String file,User user) {
		String fileName=DigitalEnvelope.extractFileName(file);
		String numOfSegment = fileName.replaceAll("[^0-9]", "");
		String signPath=DigitalEnvelope.extractPath(file);
		UtilsConfig.executeCommand("openssl dgst -sha1 -sign "+user.getPrivateKey()+" -out "+signPath+"digitalSignature"+numOfSegment+".sign "+ file,false);
	}
	public static String verifySignature(String file,String signature,String key) {
		return UtilsConfig.executeCommand("openssl dgst -sha1 -signature "+signature+" -prverify "+key+" "+file,true).toString();
	}
	
}
