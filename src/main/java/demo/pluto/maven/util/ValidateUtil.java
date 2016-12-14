package demo.pluto.maven.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author A4YL9ZZ
 * <br/>
 *  一些常用的校验类
 */
public class ValidateUtil {
	
	/**
	 * 判断对象是否为空，如果为空返回true. <br/>
	 * 如果是null,直接返回true <br/>
	 * 如果是String，返回String.isEmpty() <br/>
	 * 如果是Collection，返回Collection.isEmpty()<br/>
	 * 如果是Map，返回Map.isEmpty()<br/>
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj){
		if(obj==null){
			return true;
		}
		
		if(obj instanceof Collection){
			Collection c= (Collection) obj;
			return c.isEmpty() ;
			
		}else if(obj instanceof String){
			String s= (String) obj;
			return s.isEmpty();
		}else if(obj instanceof Map){
			Map m= (Map) obj;
			return m.isEmpty();
		}
		return false;
	}
	

}
