package demo.pluto.maven.nio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import demo.pluto.maven.util.FileUtil;

public class CaseChannel {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        caseReadFile();
//        method3();
//        System.out.println("=============");
//        method4();
        caseTransferFrom();
        System.out.println("=============");
        caseTransferTo();
    }
    

    
    public static void caseReadFile(){
        RandomAccessFile aFile =null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        try {
            File file = FileUtil.getFile("data/nio-data.txt");
            aFile = new RandomAccessFile(file, "rw");
            System.out.println(aFile.getFD());
            FileChannel inChannel = aFile.getChannel();

            ByteBuffer buf = ByteBuffer.allocate(48);
            
            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {

                //System.out.println("Read " + bytesRead);
                buf.flip();
                
                while (buf.hasRemaining()) {
                    bos.write(buf.get());
                   
                    
                }
                
                
                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            System.out.print(bos.toString("utf-8"));
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                if(aFile!=null){
                    aFile.close();
                    bos.close();
                }                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    
    public static void method4(){
        RandomAccessFile aFile = null;
        FileChannel fc = null;
        try{
            aFile = new RandomAccessFile(FileUtil.getFile("data/nio-data.txt"),"rw");
            fc = aFile.getChannel();
   
            long timeBegin = System.currentTimeMillis();
            ByteBuffer buff = ByteBuffer.allocate((int) aFile.length());
            buff.clear();
            fc.read(buff);
            //System.out.println((char)buff.get((int)(aFile.length()/2-1)));
            //System.out.println((char)buff.get((int)(aFile.length()/2)));
            //System.out.println((char)buff.get((int)(aFile.length()/2)+1));
            long timeEnd = System.currentTimeMillis();
            System.out.println("Read time: "+(timeEnd-timeBegin)+"ms");
   
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(aFile!=null){
                    aFile.close();
                }
                if(fc!=null){
                    fc.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
   
    public static void method3(){
        RandomAccessFile aFile = null;
        FileChannel fc = null;
        try{
            aFile = new RandomAccessFile(FileUtil.getFile("data/nio-data.txt"),"rw");
            fc = aFile.getChannel();
            long timeBegin = System.currentTimeMillis();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, aFile.length());
            // System.out.println((char)mbb.get((int)(aFile.length()/2-1)));
            // System.out.println((char)mbb.get((int)(aFile.length()/2)));
            //System.out.println((char)mbb.get((int)(aFile.length()/2)+1));
            long timeEnd = System.currentTimeMillis();
            System.out.println("Read time: "+(timeEnd-timeBegin)+"ms");
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(aFile!=null){
                    aFile.close();
                }
                if(fc!=null){
                    fc.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    public static void caseTransferFrom(){
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;
        try
        {
            fromFile = new RandomAccessFile(FileUtil.getFile("data/nio-data.txt"),"rw");
            FileChannel fromChannel = fromFile.getChannel();
            toFile = new RandomAccessFile(FileUtil.getFile("data/nio-data2.txt"),"rw");
            FileChannel toChannel = toFile.getChannel();
 
            long position = 0;
            long count = fromChannel.size();
            System.out.println(fromChannel.size());
            System.out.println(toChannel.size());
            System.out.println(toChannel.transferFrom(fromChannel, position, count));
            System.out.println(fromChannel.size());
            System.out.println(toChannel.size());
 
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally{
            try{
                if(fromFile != null){
                    fromFile.close();
                }
                if(toFile != null){
                    toFile.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    public static void caseTransferTo()
    {
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;
        try
        {
            fromFile = new RandomAccessFile(FileUtil.getFile("data/nio-data.txt"),"rw");
            FileChannel fromChannel = fromFile.getChannel();
            toFile = new RandomAccessFile(FileUtil.getFile("data/nio-data2.txt"),"rw");
            FileChannel toChannel = toFile.getChannel();
  
            long position = 0;
            long count = fromChannel.size();
            toChannel.position(toChannel.size()); //设置目标Channel的起始位置为当前的Size（之后如果向该Channel写入数据，将会增加Channel的Size，如果读取则直接返回末尾）
            System.out.println(fromChannel.size());
            System.out.println(toChannel.size());
            System.out.println(fromChannel.transferTo(position, count,toChannel));
            System.out.println(fromChannel.size());
            System.out.println(toChannel.size());
            //toChannel.force(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally{
            try{
                if(fromFile != null){
                    fromFile.close();
                }
                if(toFile != null){
                    toFile.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
