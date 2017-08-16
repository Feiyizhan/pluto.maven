package demo.pluto.maven.executor;

import java.util.Date;
import java.util.concurrent.Callable;

import javax.management.RuntimeErrorException;

/**
 * @author Pluto Xu a4yl9zz
 * 
 */
public final class StringTask implements Callable<String> {
    String name;
    boolean errorFlag;

    public StringTask(String name) {
        this.name = name;
        this.errorFlag = false;
    }

    public String call() {
        // Long operations
        System.out.println(Thread.currentThread().getId()+":"+Thread.currentThread().getName());
        System.out.println("Run("+name+"):"+ new Date().getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(errorFlag){
            throw new RuntimeErrorException(null, "出错了");
        }
        System.out.println("Return("+name+"):"+new Date().getTime());
        return "Run" + name;
    }

    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

}
