package demo.pluto.maven;

import java.util.Arrays;

public class Test2 {

    public static void main(String[] args){
        String dataString="67||68||69||842||843||844||845||846||847||848";
        String[] dataStrings=dataString.split("\\|\\|");
        System.out.println(Arrays.toString(dataStrings));
        
        
    }
}
