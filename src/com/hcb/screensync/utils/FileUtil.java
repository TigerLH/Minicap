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
	 * ��ȡ�����ļ�
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
	 * ��ȡ�ļ�����
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
	 * �����ļ���ָ��λ��	
	 * @param filename	������ļ�·��
	 * @param str		������ַ���
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
				FileOutputStream fs = new FileOutputStream(file,true);//׷��
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
     * ����·��ɾ��ָ����Ŀ¼���ļ������۴������
     *@param sPath  Ҫɾ����Ŀ¼���ļ�
     *@return ɾ���ɹ����� true�����򷵻� false��
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
     * �ݹ�ɾ���ļ���������Ŀ¼
     * @param   Path ��ɾ��Ŀ¼���ļ�·��
     * @return  Ŀ¼ɾ���ɹ�����true�����򷵻�false
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
        if (dirFile.delete()){	//�ļ���Ϊ��ʱɾ��Ŀ¼
            return true;
        } else {
            return false;
        }
    }
    
    
    /**
     * ɾ���ļ�
     * @param   Path    ɾ���ļ����ļ���
     * @return ɾ���ɹ�����true�����򷵻�false
     */
    private static void _deleteFile(String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }
    
    
    
    
    /**
     * ִ��ѹ������
     * @param srcPathName ��ѹ�����ļ�/�ļ���
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
     * �ж���Ŀ¼�����ļ����������ͣ��ļ�/�ļ��У�ִ�в�ͬ��ѹ������
     * @param file 
     * @param out
     * @param basedir
     */
    private static void compressByType(File file, ZipOutputStream out, String basedir) {  
        /* �ж���Ŀ¼�����ļ� */  
        if (file.isDirectory()) { 
            compressDirectory(file, out, basedir);  
        } else {   
            compressFile(file, out, basedir);  
        }  
    }  
  
    /**
     * ѹ��һ��Ŀ¼
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
            /* �ݹ� */  
        	compressByType(files[i], out, basedir + dir.getName() + "/");  
        }  
    }  
  
    /**
     * ѹ��һ���ļ�
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
