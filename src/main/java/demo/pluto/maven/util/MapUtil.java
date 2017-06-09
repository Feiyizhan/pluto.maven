package demo.pluto.maven.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Map的工具类
 * @author A4YL9ZZ pxu3@mmm.com
 *
 */
public class MapUtil {

    /**
     * 精确比较两个Map是否一致。<br/>
     * 包括K的顺序，值。V的顺序，值。
     * @author A4YL9ZZ pxu3@mmm.com
     * @param map1
     * @param map2
     * @return
     */
    public static <K,V> boolean compare(Map<K,V> map1,Map<K, V> map2){
        if(map1==map2){
            return true;
        }
        if((map1==null && map2 != null) || (map1!=null && map2 == null)){
            return false;
        }

        if(map1.size()!=map2.size()){
            return false;
        }
        
        Set<K> keySet1 =map1.keySet();
        Set<K> keySet2 = map2.keySet();
        if(keySet1.size()!=keySet2.size()){
            return false;
        }
        Iterator<K> k1Iterator = keySet1.iterator();
        Iterator<K> k2Iterator = keySet2.iterator();
        K k1=null;
        K k2 =null;
        while(k1Iterator.hasNext() && k2Iterator.hasNext() ){
            k1 = k1Iterator.next();
            k2 = k2Iterator.next();
            if(!k1.equals(k2)){
                return false;
            }
        }
        
        Set<Entry<K,V>> entrySet1= map1.entrySet();
        Set<Entry<K,V>> entrySet2= map2.entrySet();    
        Iterator<Entry<K,V>> entry1Iterator = entrySet1.iterator();
        Iterator<Entry<K,V>> entry2Iterator = entrySet2.iterator();
        Entry<K, V> entry1 =null;
        Entry<K, V> entry2 =null;
        while(entry1Iterator.hasNext() && entry2Iterator.hasNext()){
            entry1 = entry1Iterator.next();
            entry2 = entry2Iterator.next();
            if(!entry1.getValue().equals(entry2.getValue())){
                return false;
            }
        }
        
        return true;
    }
    
}
