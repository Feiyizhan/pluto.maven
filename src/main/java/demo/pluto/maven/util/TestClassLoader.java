
package demo.pluto.maven.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * TestClassLoader 自定义的类加载器，根据指定的Class名称加载Class对象。
 * 
 * @author huateng
 */
public class TestClassLoader extends ClassLoader {
	/**
	 * Comment for <code>ClassPath路径</code>
	 */
	//private static String classPath = ToolsKite.getPropertyValue(Constants.PROPERTIES_JUNIT_CLASS_PATH);

	/**
	 * 创建一个TestClassLoader对象。
	 */
	public TestClassLoader() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
	public synchronized Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
	    return loadClass(className,"");
	}
	
	
	public synchronized Class<?> loadClass(String className, String classPath)throws ClassNotFoundException {
	        Class<?> newClass;
	        //判断Class是否已加载。
            try {
                newClass = this.findLoadedClass(className);
                if(newClass!=null)
                    return newClass;
            } catch (Exception e) {}
              
	        // 使用系统类加载
	        try {
	            newClass = this.getClass().getClassLoader().loadClass(className);
	            return newClass;
	        } catch (ClassNotFoundException ex) {

	            File file;
	            // 根据ClassName获取Class文件存放路径
	            try {
	                file = getClassFile(className,classPath);
	            } catch (MalformedURLException e1) {
	                throw new ClassNotFoundException(className);
	            }
	            if (file != null) {

	                byte[] classData;

	                // 如果不是系统类，则试图从网络中指定的URL地址载入类。
	                try {
	                    // 用自定义方法载入类数据，存放于字节数组classData中。
	                    classData = getClassData(file);
	                    // 由字节数组所包含的数据建立一个class类型的对象。
	                    newClass = defineClass(className, classData, 0, classData.length);
	                    
	                    if (newClass == null) {
	                        throw new ClassNotFoundException(className);
	                    }
	                    if(newClass.getPackage()==null){ //同一个Package只允许加载一次，因此需要手动做重复性校验
	                        String packageName = className.substring(0,className.lastIndexOf('.'));
	                        definePackage(packageName,null,null,null,null,null,null,null);
	                    }

	                    
	                } catch (Exception e) {
	                    throw new ClassNotFoundException(className);
	                }

	                return newClass;
	            } else {
	                throw new ClassNotFoundException(className);
	            }
	        }
	    
	}

	/**
	 * 根据Class名称获取Class文件
	 * 
	 * @param className
	 * @return
	 * @throws MalformedURLException
	 */
	protected File getClassFile(String className,String classPath) throws MalformedURLException {
		if (className != null && className.trim().length() > 0) {
			String str = className.replace(".", File.separator);
			File file = new File(classPath, str + ".class");
			if (file.exists()) {
				return file;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 解析File对应Class文件，返回二进制数组。
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	protected byte[] getClassData(File file) throws IOException {
		try {
			// 从网络中采用URL类的方法载入指定URL地址的类的数据。
			//URLConnection connection = url.openConnection();
			//InputStream inputStream = connection.getInputStream();
			//length = connection.getContentLength();
//	          data = new byte[length];
//	            inputStream.read(data);
//	            inputStream.close();
//		    FileInputStream inputStream = new FileInputStream(file);
//		    data = new byte[1024];
		    
			return FileUtil.readFile(file);
		} catch (Exception e) {
			throw new IOException(file.getAbsolutePath());
		}
	}

	public static void main(String[] args){
	    TestClassLoader loader = new TestClassLoader();
	    try {
//            Class c = loader.loadClass("demo.pluto.maven.util.SqlUtil");
            Class c = loader.loadClass("com.mmm.fsfjos.bean.Config","C:\\github\\FSFJOS\\workspace\\FSFJOS\\build\\classes\\");
            System.out.println(c.getPackage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
	}
}
