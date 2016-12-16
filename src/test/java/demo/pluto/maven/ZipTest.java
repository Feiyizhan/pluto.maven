package demo.pluto.maven;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import demo.pluto.maven.util.FileUtil;
import demo.pluto.maven.util.ZipUtil;
import junit.framework.TestCase;

public class ZipTest extends TestCase {

    
//    public void testCreateZip1(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            
//            //CloseEntry 之前输出
//            byte[] result1=baos.toByteArray();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\beforeCloseEntry.zip");
//            fos.write(result1);
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果: zip文件可以正常打开，但添加的文件是0字节的空文件。
//             */
//            out.closeEntry();
//            
//            //CloseEntry 之后输出
//            byte[] result2=baos.toByteArray();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\afterCloseEntry.zip");
//            fos.write(result2);
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开，添加的文件也能正常打开。
//             */
//            out.close();
//            //zipClose 之后输出
//            byte[] result3=baos.toByteArray();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\afterZipClose1.zip");
//            fos.write(result3);
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开，添加的文件也能正常打开。
//             * 文件末尾增加了一些压缩文件的索引信息。
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    }
//    
//    public void testCreateZip2(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            
//            out.closeEntry();
//            
//            out.finish();
//            
//            //finish 之后输出
//            byte[] result2=baos.toByteArray();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\afterFinish1.zip");
//            fos.write(result2);
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开，添加的文件也能正常打开。
//             * 文件末尾增加了一些压缩文件的索引信息。
//             * 和正常的ZipClose结果一样
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    }
//    
//    public void testCreateZip3(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//
//            out.finish();
//            //finish 之后输出
//            byte[] result2=baos.toByteArray();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\afterFinish2.zip");
//            fos.write(result2);
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开，添加的文件也能正常打开。
//             * 文件末尾增加了一些压缩文件的索引信息。
//             * 和正常的ZipClose结果一样）
//             * 和Close了Entry结果一样
//             * 
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    }
//    
//    public void testCreateZip4(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            
//            out.closeEntry();
//            out.flush();
//            //flush 之后输出
//            byte[] result2=baos.toByteArray();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\afterFlush1.zip");
//            fos.write(result2);
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开，添加的文件也能正常打开。
//             * 和afterCloseEntry结果一样
//             */
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    }
//    
//    public void testCreateZip5(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            
//            out.flush();
//            //flush 之后输出
//            byte[] result2=baos.toByteArray();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\afterFlush2.zip");
//            fos.write(result2);
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开，但添加的文件是0字节的空文件。
//             * 和beforCloseEntry一样
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
//    
//    public void testAppendZip1(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            out.putNextEntry(new ZipEntry("2.txt"));
//            out.write(result);
//            out.closeEntry();
//    
//            //CloseEntry 之后输出
//            byte[] result2=baos.toByteArray();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\afterCloseEntry(2Entry).zip");
//            fos.write(result2);
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开.添加的文件也能正常打开，但缺少索引信息和zip的结束信息。
//             * 
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
//    
//    public void testAppendZip2(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            out.putNextEntry(new ZipEntry("2.txt"));
//            out.write(result);
//            out.closeEntry();
//    
//            //CloseEntry 之后创建一个新的zip文件，并追加新增entry
////            byte[] result2=baos.toByteArray();
////            baos.reset();
////            baos.write(result2);  //注释掉的这三行代码和没注释结果一样。
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("3.txt"));
//            out.write(result);
//            out.closeEntry();
//            out.close();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry1.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件不能正常打开。
//             * 
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
//    
//    
//    public void testAppendZip3(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            out.putNextEntry(new ZipEntry("2.txt"));
//            out.write(result);
//            out.closeEntry();
//    
//            //CloseEntry 之后创建一个新的zip文件，并追加新增entry
//            byte[] result2=baos.toByteArray();
//            baos.reset();
//            baos.write(result2);
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("3.txt"));
//            out.write(result);
//            out.closeEntry();
//            
//            //不执行zip Close操作，直接输出。
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry2.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开。但缺少索引。
//             * 
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
    
//    public void testAppendZip4(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            out.putNextEntry(new ZipEntry("2.txt"));
//            out.write(result);
//            out.closeEntry();
//    
//            //CloseEntry 之后创建一个新的zip文件，并追加新增entry
//            byte[] result2=baos.toByteArray();
//            baos.reset();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("3.txt"));
//            out.write(result2);  //将第一个zip的内容作为新的entry的值写入。
//            out.closeEntry();
//            
//            //不执行zip Close操作，直接输出。
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry3.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开。但缺少索引。
//             * 追加的文件是识别为一个zip文件，内容可以打开。
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
//   
//    
//    public void testAppendZip5(){
//        //测试在不同阶段输出zip文件。
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            out.putNextEntry(new ZipEntry("2.txt"));
//            out.write(result);
//            out.closeEntry();
//    
//            //CloseEntry 之后创建一个新的zip文件，并追加新增entry
//            byte[] result2=baos.toByteArray();
//            baos.reset();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("3.txt"));
//            out.write(result2);  //将第一个zip的内容作为新的entry的值写入。
//            out.closeEntry();
//            
//            out.close(); //执行Close，正常输出新的zip文件。
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry4.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：zip文件可以正常打开。新的zip文件是一个正常的zip文件。
//             * 追加的文件是识别为一个zip文件，内容可以打开。
//             */
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
    
//    public void testAppendZip6(){
//        //测试正常的三个Entry的zip文件的结构
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            out.putNextEntry(new ZipEntry("2.txt"));
//            out.write(result);
//            out.closeEntry();
//            
//            byte[] result2 = baos.toByteArray();
//            out.putNextEntry(new ZipEntry("3.txt"));
//            out.write(result);  
//            out.closeEntry();
//            
//            out.close(); //执行Close，正常输出新的zip文件。
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry5.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            /*
//             * 测试结果：这是一个正常的zip文件。
//             */
//            
//            //新增一个zip文件，将第一个zip文件的结果作为初始数据，并直接输出。
////            out = new ZipOutputStream(baos);
////            out.close(); //执行Close，正常输出新的zip文件。( //报错 ZIP file must have at least one entry
////            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry6.zip");
////            fos.write(baos.toByteArray());
////            fos.flush();
////            fos.close();
//            
//            //新增一个zip文件，将一个zip文件追到了两个Entry的内容作为初始数据,并增加三个新的Entry，但只有最后一个Entry正常输入数据.
//            baos.reset();
//            baos.write(result2);
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.putNextEntry(new ZipEntry("2.txt"));
//            out.closeEntry();
//            out.putNextEntry(new ZipEntry("3.txt"));
//            out.write(result);
//            out.closeEntry();
//            
//            //多个Entry
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry7.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            out.close();
//            
//            //文件无法正常打开
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry8.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
    
//    public void testAppendZip7(){
//        //测试正常的三个Entry的zip文件的结构
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            baos.write(result);
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//
//           
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry9.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//            out.close();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry10.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
    
    
//    public void testAppendZip8(){
//        //测试正常的三个Entry的zip文件的结构
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            baos.write(result);
//            out.closeEntry();
//
//           
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry11.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//            out.close();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry12.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
//    
//    
//    public void testAppendZip9(){
//        //测试正常的三个Entry的zip文件的结构
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            baos.write(result);
//           
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry13.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//            out.close();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry14.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
//    
//    public void testAppendZip10(){
//        //测试正常的三个Entry的zip文件的结构
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            
//            out = new ZipOutputStream(baos);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            out.close();
//            baos.write(result);
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry15.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
//    
    
//    public void testAppendZip11(){
//        //测试正常的三个Entry的zip文件的结构
//        try {
//            FileOutputStream fos;
//            ByteArrayOutputStream baos;
//            ZipOutputStream out;
//            byte[] result = new byte[4];
//            result[0]=1;
//            result[1]=1;
//            result[2]=1;
//            result[3]=1;
//            baos = new ByteArrayOutputStream();
//            
//            out = new ZipOutputStream(baos);
//            baos.write(result);
//            out.putNextEntry(new ZipEntry("1.txt"));
//            out.write(result);
//            out.closeEntry();
//            
//            
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry16.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//            
//            out.close();
//            fos = new FileOutputStream("C:\\github\\Generate PDF\\zip\\appendNewZipEntry17.zip");
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    } 
    
    
    public void testZipUtil(){
        
        ZipUtil zip = new ZipUtil();
        try {
            byte[] result = FileUtil.readFile("C:\\github\\CHK_EWCS_TSSD\\workspace\\EWCSTSSD\\WebContent\\resources\\left.jpg");
            zip.addFile(result, "1.jpg");
            zip.addFile(result, "a/2.jpg");
            zip.addFile(result, "b/3.jpg");
            zip.addFile(result, "4.jpg","c");
            FileOutputStream fos = new FileOutputStream("C:\\github\\Generate PDF\\zipUtil.zip");
            fos.write(zip.out());
            fos.flush();
            fos.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
}
