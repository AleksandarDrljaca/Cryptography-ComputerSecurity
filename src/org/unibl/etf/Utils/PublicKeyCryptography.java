package org.unibl.etf.Utils;

public class PublicKeyCryptography {
	
	public static void EncryptSymmKey(String user,String symmKey) {
		String privateKey=UtilsConfig.PRIVATE_KEY_PATH+"/"+user+"_PrivateKey.key";
		String privateKeyName=DigitalEnvelope.extractFileName(privateKey);
		String privateKeyPath=DigitalEnvelope.extractPath(privateKey);
		String symmKeyName=DigitalEnvelope.extractFileName(symmKey);
		String symmKeyPath=DigitalEnvelope.extractPath(symmKey);
		String pubKey=privateKeyPath+"pub"+privateKeyName+".key";
		UtilsConfig.executeCommand("openssl rsa -in "+privateKey+" -out "+pubKey+" -inform PEM -outform PEM -pubout",false);
		UtilsConfig.executeCommand("openssl rsautl -encrypt -in "+symmKey+" -out "+ symmKeyPath+symmKeyName+".enc "+"-inkey "+ pubKey+" -pubin",false);
		UtilsConfig.executeCommand("rm "+pubKey,false);
		UtilsConfig.executeCommand("rm "+symmKey,false);
		
	}
	public static void decryptSymmKey(String user,String symmKey) {
		String privateKey=UtilsConfig.PRIVATE_KEY_PATH+"/"+user+"_PrivateKey.key";
		String symmKeyName=DigitalEnvelope.extractFileName(symmKey);
		String symmKeyPath=DigitalEnvelope.extractPath(symmKey);
		UtilsConfig.executeCommand("openssl rsautl -decrypt -in "+symmKey+" -out "+symmKeyPath+symmKeyName+".key "+"-inkey "+ privateKey,false);
		UtilsConfig.executeCommand("rm "+symmKey,false);
	}
	
}
