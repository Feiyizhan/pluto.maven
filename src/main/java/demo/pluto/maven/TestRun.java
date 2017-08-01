package demo.pluto.maven;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestRun {

    public static void main(String[] args) {
        
        List<String> list = new ArrayList<String>();
        List beanList = new ArrayList();
        beanList.add(new Bean("1","test1"));
        beanList.add(new Bean("2","test2"));
        for(int i=0;i<beanList.size();i++){
            System.out.println(beanList.get(i));
            System.out.println(beanList.get(i).getClass().getName());
        }
        list = (List<String>)beanList;
        List<Object> list2 = new ArrayList<Object>(); 
        list2 = (List<Object>)beanList;
        ObjectMapper om = new ObjectMapper();
        try {
            System.out.println(om.writeValueAsString(list));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        for(int i=0;i<list2.size();i++){
            System.out.println(list2.get(i).getClass().getName());
            System.out.println(list2.get(i));
        }
        if(beanList.get(0) instanceof String ){
            for(int i=0;i<list.size();i++){
                System.out.println(list.get(i).getClass().getName());
                System.out.println(list.get(i));
            }
        }else{
            System.out.println("类型转换不支持");
        }
        

        
    }


}
