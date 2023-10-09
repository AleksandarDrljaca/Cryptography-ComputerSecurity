package org.unibl.etf.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtil {

	public static Object loadDB() {
		
		ObjectInputStream ois=null;
		Object dataBase=null;
		try {
			ois=new ObjectInputStream(new FileInputStream(new File("list.ser")));
			dataBase= (Object) ois.readObject();
			ois.close();
			
		}
		catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				ois.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		return dataBase;
}
public static boolean saveDB(Object db) {
	ObjectOutputStream oos=null;
	boolean result=false;
	try {
		oos=new ObjectOutputStream(new FileOutputStream(new File("list.ser")));
		oos.writeObject(db);
		result=true;
		oos.close();
	}catch(IOException e) {
		e.printStackTrace();
	}
	finally {
		try {
			oos.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	return result;
}
}
