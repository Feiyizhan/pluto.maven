package demo.pluto.maven.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

public class FileUtil {
    
	/**
	 * 以指定的编码读取文件，读取成功返回内容字符串，失败返回null
	 * @param fileName
	 * @param charset
	 * @return
	 */
	public static String readFile2String(String fileName,String charset){
		try {
			byte[] result = readFile(fileName);
			return new String(result,charset);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取文件，返回二进制内容。
	 * @author A4YL9ZZ 
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static byte[] readFile(String fileName) throws IOException{
	    return readFile(getFile(fileName));
	}
	
	/**
	 * 读取文件，返回二进制内容。
	 * @author A4YL9ZZ 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFile(File file) throws IOException{
	    FileInputStream fis = new FileInputStream(file); 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bufferBtyes = new byte[1024];
        int len=0;
        while((len=fis.read(bufferBtyes))>0){
            baos.write(bufferBtyes,0,len);
        }
        fis.close();
        byte[] result = baos.toByteArray();
        baos.close();
        return result;
	}
	
	/**
	 * 获取文件，如果当前目录没找到，则在当前classPath找。
	 * @param fileName
	 * @return
	 */
	public static File getFile(String fileName){
		URL url = FileUtil.class.getClassLoader().getResource(fileName);
		File file=null;
		if(url!=null){
		    try {
                fileName = URLDecoder.decode(url.getFile(),"UTF-8");
            } catch (UnsupportedEncodingException e) {}
		}
		
		file= new File(fileName);
		return file;
	}
	
	
	/**
     * 获取文件对应的InputStream，如果当前目录没找到，则在当前classPath找。
     * @param fileName
     * @return
	 * @throws FileNotFoundException 
     */
    public static InputStream getFileInput(String fileName) throws FileNotFoundException{
        
        InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        if(is!=null){
            return is;
        }
        return new FileInputStream(getFile(fileName));
    }
    
    
	
	/**
	 * 以指定格式内容写入文件
	 * @param fileName
	 * @param val
	 * @param charset
	 * @param append
	 * @return
	 */
	public static void writeFile(String fileName,String val,String charset,boolean append){
		try {
			FileWriter fw = new FileWriter(getFile(fileName),append);
			String str =  new String(val.getBytes(),charset) ;
			fw.write(str, 0, str.length());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	   /**
     * 创建文件,包括父目录
     * @param parent
     * @param file
     * @return
     */
    public static boolean createFile(String parent,String file){
        return createFile(new File(parent,file));
    }
    
    /**
     * 创建文件,包括父目录
     * @param file
     * @return
     */
    public static boolean createFile(String file){
        return createFile(new File(file));
    }
    
    /**
     * 创建文件,包括父目录
     * @param f
     * @return
     */
    public static boolean createFile(File f){
        try {
            if(f.exists()){
                f.delete();
            }
            return f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }
    
    /**
     * 创建目录,包括父目录
     * @param parent
     * @param file
     * @return
     */
    public static boolean createDir(String parent,String file){
        return createDir(new File(parent,file));
    }
    
    /**
     * 创建目录,包括父目录
     * @param dir
     * @return
     */
    public static boolean createDir(String dir){
        return createDir(new File(dir));
    }
    
    /**
     * 创建目录,包括父目录
     * @param f
     * @return
     */
    public static boolean createDir(File f){
        if(!f.exists()){
            return f.mkdirs();
        }

        return true;
    }
}
