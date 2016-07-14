package com.hcb.screensync.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
	/**
	 * 读取配置文件
	 * @param key
	 */
	public static String getConfigValue(String filePath,String key){
		File file = new File(System.getProperty("user.dir")
				+File.separator+filePath);
		if(!file.exists()){
			return null;
		}
		FileInputStream inpf = null;
		try {
			inpf = new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Properties p = new Properties();
		try {
			p.load(inpf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String value = p.getProperty(key);
		if("".equals(value)){
			return null;
		}
		return value;
	}
	
	
	/**
	 * 读取文件内容
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String Read(String filename) throws Exception{
		File file = new File(filename);
		StringBuilder sb = new StringBuilder();
		if(!file.exists()){
			throw new Exception("file not found");
		}else{
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader bw = new BufferedReader(isr);
			String str = "";
			while((str=bw.readLine())!=null){
				sb.append(str);
			}
		}
		return sb.toString();
	}
	
	
	
	/**
	 * 保存文件到指定位置	
	 * @param filename	保存的文件路径
	 * @param str		保存的字符串
	 */
	public static void Write(String filename, String str) {
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
			try {
				FileOutputStream fs = new FileOutputStream(file,true);//追加
				OutputStreamWriter ow = new OutputStreamWriter(fs,"UTF-8");
				BufferedWriter bw = new BufferedWriter(ow);
				bw.write(str);
				bw.flush();
				bw.newLine();
				bw.flush();
				fs.close();
				ow.close();
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	
	
    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     * @throws Exception 
     */
    public static void DeleteFolder(String Path) throws Exception {
        File file = new File(Path);
        if (file.exists()) {
        	 if (file.isFile()) {  
                 _deleteFile(Path);
             } else {  
                  _deleteDirectory(Path);
             }
        }
    }
    
    
    /**
     * 递归删除文件夹下所有目录
     * @param   Path 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    private static boolean _deleteDirectory(String Path) {
        File dirFile = new File(Path);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
            	_deleteFile(files[i].getAbsolutePath());
            }
            else {
            	_deleteDirectory(files[i].getAbsolutePath());
            }
        }
        if (dirFile.delete()){	//文件夹为空时删除目录
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * 删除文件
     * @param   Path    删除文件的文件名
     * @return 删除成功返回true，否则返回false
     */
    private static void _deleteFile(String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }
    
    
    
    
    /**
     * 执行压缩操作
     * @param srcPathName 被压缩的文件/文件夹
     */
    public static void compress(String srcPathName,String zipFile) {
    	File outfile = new File(zipFile);
        File file = new File(srcPathName);  
        if (!file.exists()){
        	throw new RuntimeException(srcPathName + "not found");  
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outfile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());  
            ZipOutputStream out = new ZipOutputStream(cos);  
            String basedir = "";  
            compressByType(file, out, basedir);  
            out.close();  
        } catch (Exception e) { 
        	e.printStackTrace();
        }  
    }  
  
    
    /**
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法
     * @param file 
     * @param out
     * @param basedir
     */
    private static void compressByType(File file, ZipOutputStream out, String basedir) {  
        /* 判断是目录还是文件 */  
        if (file.isDirectory()) { 
            compressDirectory(file, out, basedir);  
        } else {   
            compressFile(file, out, basedir);  
        }  
    }  
  
    /**
     * 压缩一个目录
     * @param dir
     * @param out
     * @param basedir
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {  
        if (!dir.exists()){
        	 return;  
        }
           
        File[] files = dir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            /* 递归 */  
        	compressByType(files[i], out, basedir + dir.getName() + "/");  
        }  
    }  
  
    /**
     * 压缩一个文件
     * @param file
     * @param out
     * @param basedir
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {  
        if (!file.exists()) {  
            return;  
        }  
        try {  
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));  
            ZipEntry entry = new ZipEntry(basedir + file.getName());  
            out.putNextEntry(entry);  
            int count;  
            byte data[] = new byte[8192];
            while ((count = bis.read(data, 0, 8192)) != -1) {
                out.write(data, 0, count);  
            }  
            bis.close();
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
}
