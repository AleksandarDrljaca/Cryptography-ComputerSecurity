package org.unibl.etf.Utils;
import org.unibl.etf.Exceptions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import org.unibl.etf.Tuple.*;
import org.unibl.etf.User.User;
public class FileHandler {
	
	private static int generateSegmentNum() {
		Random r=new Random();
		return r.nextInt(8-4)+4;
	}
	public static void read(String directory,User user ) throws IOException, UnAutohrizedChangeException {
		//decrypt segments, verify segment signatures and merge segments into one file
		
		String downloadFileName=directory.substring(directory.lastIndexOf('/')+1, directory.length());
		ArrayList<Tuple<String,String>>signSegPair =listFiles(directory);
		for(var t:signSegPair) {
			DigitalEnvelope.decryptFile(user.getUsername(),t.Item2(),user.getSymmetricKey());
		}
		signSegPair=listFiles(directory);
		boolean noAuthorizationChanged=verifySegmentSignatures(signSegPair,user);
		if(!noAuthorizationChanged) {
			throw new UnAutohrizedChangeException("Unauthorized changes are made! ");
		}
		String[] files=new String[signSegPair.size()];
		for(int i=0;i<signSegPair.size();i++) files[i]=signSegPair.get(i).Item2();
		
		 List<byte[]> chunks = new ArrayList<>();
	        for (String fileName : files) {
	            Path path = Paths.get(fileName);
	            chunks.add(Files.readAllBytes(path));
	        }
	        int totalSize = 0;
	        for (byte[] chunk : chunks) {
	            totalSize += chunk.length;
	        }
	        byte[] combined = new byte[totalSize];
	        int offset = 0;
	        for (byte[] chunk : chunks) {
	            System.arraycopy(chunk, 0, combined, offset, chunk.length);
	            offset += chunk.length;
	        }
	        Files.write(Paths.get("./Download/"+user.getUsername()+"/"+downloadFileName), combined);
	        for(var t:signSegPair) {
				DigitalEnvelope.encryptFile(user.getUsername(),t.Item2(),user.getSymmetricKey());
			}
	}
	public static void split(File file,User user) {
		
		int chunkSize=(int) (file.length()/generateSegmentNum());
		String fileName=DigitalEnvelope.extractFileName(file.getPath());
		String uploadFileDir=user.getFileSystemPath()+"/"+fileName+"/";

		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[chunkSize];
		
			int bytesRead = fis.read(buffer);
			int chunkNumber = 0;
			UtilsConfig.executeCommand("mkdir "+uploadFileDir+fileName+"_"+(chunkNumber+1),false);
			FileOutputStream fos = new FileOutputStream(uploadFileDir+fileName+"_"+(chunkNumber+1)+"/"+fileName+"_"+(chunkNumber+1) + ".bin");
			fos.write(buffer, 0, bytesRead);
			DigitalSignature.signFileSegment(uploadFileDir+fileName+"_"+(chunkNumber+1)+"/"+fileName+"_"+(chunkNumber+1) + ".bin",user);
			DigitalEnvelope.encryptFile(user.getUsername(),uploadFileDir+fileName+"_"+(chunkNumber+1)+"/"+fileName+"_"+(chunkNumber+1) + ".bin", user.getSymmetricKey());
			bytesRead = fis.read(buffer);
			while (bytesRead != -1) {
				fos.close();
				chunkNumber++;
				UtilsConfig.executeCommand("mkdir "+uploadFileDir+fileName+"_"+(chunkNumber+1),false);
				fos = new FileOutputStream(uploadFileDir+fileName+"_"+(chunkNumber+1)+"/"+fileName+"_"+(chunkNumber+1) + ".bin");
				fos.write(buffer, 0, bytesRead);
				DigitalSignature.signFileSegment(uploadFileDir+fileName+"_"+(chunkNumber+1)+"/"+fileName+"_"+(chunkNumber+1) + ".bin",user);
				DigitalEnvelope.encryptFile(user.getUsername(),uploadFileDir+fileName+"_"+(chunkNumber+1)+"/"+fileName+"_"+(chunkNumber+1) + ".bin", user.getSymmetricKey());
				bytesRead = fis.read(buffer);
			}
			fos.close();
			fis.close();

		}catch (IOException e) {
			e.printStackTrace();

		}
	}
	public static ArrayList<Tuple<String,String>> listFiles(String path) {
		ArrayList<String> subDirs=new ArrayList<String>();
		ArrayList<String> list=new ArrayList<String>();
		File mainDir=new File(path);
		File[] fileList = mainDir.listFiles();
		for(var f:fileList) {
			 subDirs.add(f.getPath());
		}
		for(var x:subDirs) {
			File xx=new File(x);
			File[] fs = xx.listFiles();
			for(var f:fs) {
				 if(f.isFile()) list.add(f.getPath());
			}
		}
		Collections.sort(list);
		
		ArrayList<Tuple<String,String>> fileSignaturePairs=new ArrayList<Tuple<String,String>>();
		for(int i=0;i<list.size();i+=2) {
			fileSignaturePairs.add(new Tuple<String,String>(list.get(i),list.get(i+1)));
			
		}
		
		return fileSignaturePairs;
		
	}
	private static boolean verifySegmentSignatures(ArrayList<Tuple<String,String>> segments,User user) {
		for(var t:segments) {
			String status=DigitalSignature.verifySignature(t.Item2(), t.Item1(),user.getPrivateKey());
			if(!status.contains("OK")) return false;
		}
		return true;
	}
	
	
}
	

