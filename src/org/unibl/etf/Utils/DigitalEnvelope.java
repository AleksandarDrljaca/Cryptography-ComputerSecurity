package org.unibl.etf.Utils;

public class DigitalEnvelope {

	public static void generateSymmetricKey(String user) {
		UtilsConfig.executeCommand("./symKeyGen.sh ./FileSystem/"+user+"/"+user+"SymmKey.key",false);
		PublicKeyCryptography.EncryptSymmKey(user, "./FileSystem/"+user+"/"+user+"SymmKey.key");

	}
	public static void encryptFile(String user,String pathToFile,String symmKey) {
		//decrypting symmetric key for use
		PublicKeyCryptography.decryptSymmKey(user, symmKey);
		String keyPath=DigitalEnvelope.extractPath(symmKey);
		String keyName=DigitalEnvelope.extractFileName(symmKey);
		String decSymm=keyPath+keyName+".key";
		//------
		String fileName=extractFileName(pathToFile);
		String filePath=extractPath(pathToFile);
		System.out.println(pathToFile+"\n"+fileName+"\n"+filePath);
		UtilsConfig.executeCommand("openssl enc -aes-256-cbc -pbkdf2 -iter 10000 -kfile "+decSymm+" -base64 -in "+pathToFile+" -out "+ filePath+fileName+".enc",false);
		UtilsConfig.executeCommand("rm "+pathToFile,false);
		//encrypting symmetric key
		PublicKeyCryptography.EncryptSymmKey(user, decSymm);
		//-----
	}
	public static void decryptFile(String user,String pathToFile,String symmKey) {
		//decrypting symmetric key for use
		PublicKeyCryptography.decryptSymmKey(user, symmKey);
		String keyPath=DigitalEnvelope.extractPath(symmKey);
		String keyName=DigitalEnvelope.extractFileName(symmKey);
		String decSymm=keyPath+keyName+".key";
		//-------
		String fileName=extractFileName(pathToFile);
		String filePath=extractPath(pathToFile);
		UtilsConfig.executeCommand("openssl enc -d -aes-256-cbc -pbkdf2 -iter 10000 -kfile "+decSymm+" -base64 -in "+pathToFile+" -out "+ filePath+fileName+".dec",false);
		UtilsConfig.executeCommand("rm "+pathToFile,false);
		//encrypting symmetric key
		PublicKeyCryptography.EncryptSymmKey(user, decSymm);
		//-----
		
	}
	public static String extractFileName(String path) {
		
		int after=path.lastIndexOf('/');
		int until=path.lastIndexOf('.');
		return path.substring(after+1, until);
		
	}
	public static String extractPath(String keyPath) {
		
		int until=keyPath.lastIndexOf('/');
		return keyPath.substring(0, until+1);
	}
	
}
