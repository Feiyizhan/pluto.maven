package demo.pluto.maven;

import java.net.URL;

public class PathTest {
    public static void main(String[] args){
        URL url =PathTest.class.getClassLoader().getResource("./");
        System.out.println(url.getPath());
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
        System.out.println(System.getProperty("user.dir"));
        
        
    }
}
