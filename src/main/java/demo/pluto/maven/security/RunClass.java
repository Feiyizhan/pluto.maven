package demo.pluto.maven.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class RunClass {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        encryptStringWithAES();
//        encryptStringWithDES();
//        encryptStringWithDESede();
        encryptStringWithDES2();
    }
    
    public static void encryptStringWithAES(){
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            
            kgen.init(128, new SecureRandom("123456".getBytes("UTF-8")));  //使用用户提供的password初始化此密钥生成器，使其具有确定的密钥大小128字节长。
            SecretKey secretKey = kgen.generateKey();  //生成一个密钥。      
            byte[] enCodeFormat = secretKey.getEncoded();  //返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  //根据给定的enCodeFormat字节数组构造一个用AES算法加密的密钥。
            
            System.out.println("加密前密钥的内容："+HexBin.encode("123456".getBytes("UTF-8"))); 
            System.out.println("编码后的密钥的内容："+HexBin.encode(enCodeFormat)); 
            //2.对生成的密钥key进行编码保存  
            String keyencode= HexBin.encode(key.getEncoded());  
            System.out.println("加密后的密钥的内容："+keyencode); 
            //写入文件保存  
            File file=new File("keyencode.txt");  
            OutputStream outputStream=new FileOutputStream(file);  
            outputStream.write(keyencode.getBytes());  
            outputStream.close();  
            System.out.println(keyencode+" -----> key保存成功");  
            
            //3.进行加密  
            Cipher cipher=Cipher.getInstance("AES");   // 创建密码器  
            cipher.init(Cipher.ENCRYPT_MODE, key);   // 以加密的方式用密钥初始化此 Cipher。
            byte[] data = "pxu3@mmm.com".getBytes("utf-8");
            byte[] result= cipher.doFinal(data);   //加密内容
            
            System.out.println("加密前的内容："+HexBin.encode(data));   
            System.out.println("加密后的内容："+HexBin.encode(result));  
            
            
            //4.获取解密密钥 
            kgen.init(128, new SecureRandom("123456".getBytes("UTF-8")));
            SecretKey deSecretKey = kgen.generateKey();  //生成一个密钥。      
            byte[] deCodeFormat = deSecretKey.getEncoded();  //返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            SecretKeySpec dekey = new SecretKeySpec(deCodeFormat, "AES"); 
              
            //5.进行解密  
            Cipher deCipher=Cipher.getInstance("AES");  
            deCipher.init(Cipher.DECRYPT_MODE, dekey);  
            result= deCipher.doFinal(result);  
            System.out.println("接收方进行解密："+new String(result));  
            
            
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    
    public static void encryptStringWithDES(){
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("DES");
            
            kgen.init(new SecureRandom("123456".getBytes("UTF-8")));  //使用用户提供的password初始化此密钥生成器，使其具有确定的密钥大小128字节长。
//            kgen.init(new SecureRandom());
            SecretKey secretKey = kgen.generateKey();  //生成一个密钥。      
            byte[] enCodeFormat = secretKey.getEncoded();  //返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "DES");  //根据给定的enCodeFormat字节数组构造一个用AES算法加密的密钥。
            
            System.out.println("加密前密钥的内容："+HexBin.encode("123456".getBytes("UTF-8"))); 
            System.out.println("编码后的密钥的内容："+HexBin.encode(enCodeFormat)); 
            //2.对生成的密钥key进行编码保存  
            String keyencode= HexBin.encode(key.getEncoded());  
            System.out.println("加密后的密钥的内容："+keyencode); 
            //写入文件保存  
            File file=new File("keyencode.txt");  
            OutputStream outputStream=new FileOutputStream(file);  
            outputStream.write(keyencode.getBytes());  
            outputStream.close();  
            System.out.println(keyencode+" -----> key保存成功");  
            
            //3.进行加密  
            Cipher cipher=Cipher.getInstance("DES");   // 创建密码器  
            cipher.init(Cipher.ENCRYPT_MODE, key);   // 以加密的方式用密钥初始化此 Cipher。
            byte[] data = "pxu3@mmm.com".getBytes("utf-8");
            byte[] result= cipher.doFinal(data);   //加密内容
            
            System.out.println("加密前的内容："+HexBin.encode(data));   
            System.out.println("加密后的内容："+HexBin.encode(result));  
            
            
            //4.获取解密密钥 
            kgen.init(new SecureRandom("123456".getBytes("UTF-8")));
            SecretKey deSecretKey = kgen.generateKey();  //生成一个密钥。      
            byte[] deCodeFormat = deSecretKey.getEncoded();  //返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            SecretKeySpec dekey = new SecretKeySpec(deCodeFormat, "DES"); 
              
            //5.进行解密  
            Cipher deCipher=Cipher.getInstance("DES");  
            deCipher.init(Cipher.DECRYPT_MODE, dekey);  
            result= deCipher.doFinal(result);  
            System.out.println("接收方进行解密："+new String(result));  
            
            
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    
    public static void encryptStringWithDES2(){
        
        try {
            byte[] password = "12345678".getBytes("UTF-8");
            System.out.println(password.length);
            System.out.println("原始数据:"+HexBin.encode(password));
            KeyGenerator kgen = KeyGenerator.getInstance("DES");
            SecretKey secretKey = kgen.generateKey();  //生成一个密钥。 
            System.out.println("生成的密钥数据数据:"+HexBin.encode(secretKey.getEncoded()));
            SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), "DES");
            System.out.println("SecretKeySpec处理之后的数据:"+HexBin.encode(key.getEncoded()));
            
            DESKeySpec desKeySpec=new DESKeySpec(password);  
            System.out.println("通过DESKeySpec直接创建的Key:"+HexBin.encode(desKeySpec.getKey()));
            
            SecretKeyFactory secretKeyFactory=SecretKeyFactory.getInstance("DES"); 
            Key secretKey2 = secretKeyFactory.generateSecret(desKeySpec) ;//获取密封的密钥
            System.out.println("密封后的Key:"+HexBin.encode(secretKey2.getEncoded()));
            
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
//        DESKeySpec key1 = new DESKeySpec("")
        
        
    }

}
