package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {
	public static void writeToFile(String toWrite, String fileName, boolean append) {
		try {
			File f = new File(fileName);
			if(!append) {
				if(f.exists()) {
					throw new Exception("已经有文件存在");
				} 
			} 
			if(!f.exists()) {
				f.createNewFile();
			}
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f, append));
			bos.write(toWrite.getBytes());
			bos.flush();
			bos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
