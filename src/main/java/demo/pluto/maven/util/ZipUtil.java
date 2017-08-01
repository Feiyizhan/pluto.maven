package demo.pluto.maven.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Zip文件相关工具包
 * @author A4YL9ZZ 
 *
 */
public class ZipUtil {

    private ZipOutputStream zipOutputStream;
    private ByteArrayOutputStream output;
    private boolean isClosed;
    
    public ZipUtil() {
        super();
        this.output = new ByteArrayOutputStream();
        this.zipOutputStream = new ZipOutputStream(output); 
        isClosed = false;
    }
    
    /**
     * 增加文件
     * @author A4YL9ZZ 
     * @param data
     * @param name
     * @throws IOException
     */
    public void addFile(byte[] data,String name) throws IOException{
        addFile(data,name,"");
    }
    
    /**
     * 增加文件
     * @author A4YL9ZZ 
     * @param data
     * @param name
     * @param parentDir
     * @throws IOException
     */
    public void addFile(byte[] data,String name,String parentDir) throws IOException{
        if(isClosed){
            throw new IOException("文件已关闭");
        }
        if(data!=null && name!=null && parentDir!=null && !name.isEmpty()){
            if(!parentDir.isEmpty()){
                this.zipOutputStream.putNextEntry(new ZipEntry(parentDir+"/"+name));
            }else{
                this.zipOutputStream.putNextEntry(new ZipEntry(name));
            }
            
            this.zipOutputStream.write(data);
            this.zipOutputStream.closeEntry();
        }
    }
    
    /**
     * 输出
     * @author A4YL9ZZ 
     * @return
     * @throws IOException
     */
    public byte[] out() throws IOException{
        if(!isClosed){
            this.close();
        }
        return this.output.toByteArray();
    }
    
    private void close() throws IOException{
        this.zipOutputStream.close();
        this.output.close();
        isClosed=true;
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
        if(!isClosed){
            this.close();
        }
    }
    
    
    


}
