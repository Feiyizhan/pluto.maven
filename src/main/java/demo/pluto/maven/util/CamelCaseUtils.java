package demo.pluto.maven.util;

/**
 * @author A4YL9ZZ
 * <br/> 驼峰命名法转换工具类
 *
 */
public class CamelCaseUtils {
	 
    private static final char SEPARATOR = '_';
 
    
    /**
     * 驼峰法转为下划线命名法(支持大驼峰和小驼峰）
     * @param s
     * @return
     */
    public static String camelCase2UnderlineName(String s) {
        if (s == null) {
            return null;
        }
 
        StringBuilder sb = new StringBuilder();
        //s=s.substring(0, 1).toLowerCase()+s.substring(1);
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
        	
            char c = s.charAt(i);
 
            boolean nextUpperCase = true;
 
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
 
            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
 
            sb.append(Character.toLowerCase(c));
        }
 
        return sb.toString();
    }
 
    /**
     * 下划线命名法转小驼峰命名法
     * @param s
     * @return
     */
    public static String underlineName2LowerCamelCase(String s) {
        if (s == null) {
            return null;
        }
 
        s = s.toLowerCase();
 
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
 
            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
 
        return sb.toString();
    }
 
    /**
     * 下划线命名法转大驼峰命名法
     * @param s
     * @return
     */
    public static String underlineName2UpperCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = underlineName2LowerCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    /**
     * 大驼峰转小驼峰
     * @param s
     * @return
     */
    public static String UpperCamelCase2LowerCamelCase(String s){
    	return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
    
    /**
     * 小驼峰转大驼峰
     * @param s
     * @return
     */
    public static String LowerCamelCase2UpperCamelCase(String s){
    	return s.substring(0, 1).toUpperCase()+ s.substring(1);
    }
 
    public static void main(String[] args) {
        System.out.println(CamelCaseUtils.camelCase2UnderlineName("ISOCertifiedStaff"));
        System.out.println(CamelCaseUtils.camelCase2UnderlineName("CertifiedStaff"));
        System.out.println(CamelCaseUtils.camelCase2UnderlineName("UserID"));
        System.out.println(CamelCaseUtils.underlineName2UpperCamelCase("iso_certified_staff"));
        System.out.println(CamelCaseUtils.underlineName2UpperCamelCase("certified_staff"));
        System.out.println(CamelCaseUtils.underlineName2LowerCamelCase("user_id"));
    }
}
