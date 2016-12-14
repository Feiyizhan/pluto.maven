package demo.pluto.maven.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



import org.springframework.web.util.HtmlUtils;



/**
 * @author A4YL9ZZ pxu3@mmm.com
 * <br/> 对Object 中所有String值进行HtmlEscape操作。
 * <br/>HtmlEscape 操作调用的是org.springframework.web.util.HtmlUtils.htmlEscape
 */
public class ObjectEscapeUtil {
    
    
    
    /**
     * 基础数据包装类型的Class Name全称。（不包括String）
     */
    private static  List<String> types = new ArrayList<String>();
    /**
     *  基础数据类型
     */
    private static List<String> baseTypes = new ArrayList<String>();
    /**
     * 明确不需要处理的返回类型
     */
    private static List<String> otherTypes = new ArrayList<String>();
    static {
        types.add("java.lang.Integer");
        types.add("java.lang.Double");
        types.add("java.lang.Float");
        types.add("java.lang.Long");
        types.add("java.lang.Short");
        types.add("java.lang.Byte");
        types.add("java.lang.Boolean");
        types.add("java.lang.Character");
        baseTypes.add("int");
        baseTypes.add("double");
        baseTypes.add("long");
        baseTypes.add("short");
        baseTypes.add("byte");
        baseTypes.add("boolean");
        baseTypes.add("char");
        baseTypes.add("float");
        otherTypes.add("java.util.Date");
        otherTypes.add("java.sql.Date");
        
    };


    /**
     * @author A4YL9ZZ pxu3@mmm.com
     * 对Object中的所有的有get和set方法的String类型成员属性进行HTMLEscape操作。
     * <br/> 在执行htmlEscape之前，先执行htmlUnescape。防止重复Escape。
     * <br/> Object中的成员属性如果也是POJO Object。那么将会递归处理。
     * <br/> Object中的成员属性如果是集合（包括Map,Array,Collection) 也会对每个元素进行以上递归处理。
     * @param obj
     * @return
     */
    public static Object htmlEscape(Object obj) {
        if (obj == null) {
            return null;
        }
        
        //过滤掉基础数据类型和包装类
        if(obj instanceof Long
                ||obj instanceof Boolean
                ||obj instanceof Integer
                ||obj instanceof Double
                ||obj instanceof Float
                ||obj instanceof Short
                ||obj instanceof Byte
                ||obj instanceof Boolean
                ||obj instanceof Character
                ){
            return obj;
        }

        //只对String的包装类进行直接转换
        if (obj instanceof String) {
            //先进行Unescape在Escape，防止对已经转义的字符再此转义。
            return HtmlUtils.htmlEscape(HtmlUtils.htmlUnescape(obj.toString()));
        } else if (obj instanceof Collection) { //集合的处理
            Collection c = (Collection) obj;
            Object[] objArray = c.toArray();
            c.clear();
            for (Object o : objArray) {
                c.add(htmlEscape(o));
            }
            return c;
        } else if (obj instanceof Map) { //Map的处理
            Map map = (Map) obj;
            for (Object key : map.keySet()) {
                map.put(key, htmlEscape(map.get(key)));
            }
            return map;
        } else if (obj.getClass().isArray()) {//数据的处理
            Object[] objArray = (Object[]) obj;
            for (int i = 0; i < objArray.length; i++) {
                objArray[i] = htmlEscape(objArray[i]);
            }
            return objArray;
        } else { 
            //获取所有的成员方法
            List<Method> methods = getClassMemberMethods(obj.getClass());
            Map<String,Object> map = new HashMap<String,Object>();
            for(Method m:methods){
                try {
                    
                    if(!m.getReturnType().getName().equals("void")
                            && !baseTypes.contains(m.getReturnType().getName())
                            && !types.contains(m.getReturnType().getName())
                            && !otherTypes.contains(m.getReturnType().getName())){
                      if(m.getName().startsWith("get")){
                          //调用get方法获取值并转换
                         Object o = htmlEscape(m.invoke(obj));
                         map.put(m.getName().substring(3), o);
                      }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            //转换后调用set方法
            for(Method m:methods){
                try {
                    if(m.getReturnType().getName().equals("void")
                            && m.getName().startsWith("set")){
                         if(map.containsKey(m.getName().substring(3))){
                             Object o = map.get(m.getName().substring(3));
                             if(o!=null) {
                                 m.invoke(obj,o);
                             } 
                         }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


        return obj;

    }

    /**
     * 获取class的所有符合要求的成员方法
     * @author A4YL9ZZ pxu3@mmm.com
     * @param cls
     * @return
     */
    public static List<Method> getClassMemberMethods(Class<?> cls){
        List<Method> list = new ArrayList<Method>();
        if(cls ==Object.class){
            return list;
        }
        
        //获取成员属性
        Map<String,Class<?>> fields = _findFields(cls,null);
        
        //获取成员方法
        Method[] methods = cls.getDeclaredMethods();
        for(Method m:methods){
            if (!_isIncludableMemberMethod(m)) {
                continue;
            }
            String name = m.getName();
            if(name.startsWith("get")){
                Class<?> c =fields.get(name.substring(3));
                if(c!=null && c==m.getReturnType()){  //只处理有字段对应的get方法
                    list.add(m); 
                }
                    
            }else if(name.startsWith("set") 
                    && m.getTypeParameters().length==1){
                Class<?> c =fields.get(name.substring(3));
                //只处理有字段对应的set方法
                if(c!=null && c==m.getParameterTypes()[0])
                    list.add(m); 
            }
        }
        return list;
    }
    
    

    /**
     * 获取class的所有符合要求的字段
     * @author A4YL9ZZ pxu3@mmm.com
     * @param c
     * @param fields
     * @return
     */
    protected static Map<String,Class<?>> _findFields(Class<?> c,Map<String,Class<?>> fields)
    {
        Class<?> parent = c.getSuperclass();
        if (parent != null) {

            fields = _findFields(parent,fields);
            for (Field f : c.getDeclaredFields()) {
                if (!_isIncludableField(f)) {
                    continue;
                }

                if (fields == null) {
                    fields = new LinkedHashMap<String,Class<?>>();
                }
                fields.put(upperFirstChar(f.getName()), f.getClass());
            }

        }
        return fields;
    }
    
   
    
    
    /**
     * 首字母大写
     * @author A4YL9ZZ pxu3@mmm.com
     * @param str
     * @return
     */
    private static String upperFirstChar(String str){
        return str.substring(0,1).toUpperCase()+ str.substring(1);
    }
    
    
    /**
     * 判断是否是需要包含的成员方法
     * @author A4YL9ZZ pxu3@mmm.com
     * @param m
     * @return
     */
    protected static boolean _isIncludableMemberMethod(Method m)
    {
        if (Modifier.isStatic(m.getModifiers())) {
            return false;
        }
        if (m.isSynthetic() || m.isBridge()) {
            return false;
        }
        
        if(!Modifier.isPublic(m.getModifiers())){
            return false;
        }
        int pcount = m.getParameterTypes().length;
        return (pcount <= 1);
    }
    
    
    /**
     * 判断是否是需要包含的成员字段
     * @author A4YL9ZZ pxu3@mmm.com
     * @param f
     * @return
     */
    private static boolean _isIncludableField(Field f){
        if (f.isSynthetic()) {
            return false;
        }
        int mods = f.getModifiers();
        if (Modifier.isStatic(mods)) {
            return false;
        }
        if(Modifier.isTransient(mods)){
            return false;
        }
        if(Modifier.isFinal(mods)){
            return false;
        }
        
        return true;
    }
    
    
    public static void main(String[] args) {
        String str = "<dfsadfsadfa></dsfsdfsdf> dsfasd\"dsfsdfsdfasd\"dfasdfasd";
        System.out.println(str);
//        System.out.println(htmlEscape(new WarrantyContract()));
//        System.out.println(getClassMethods(WarrantyContract.class));
    }
}
