package demo.pluto.maven;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lowagie.text.DocumentException;

import demo.pluto.maven.pdf.old.GeneratePDFOld;
import demo.pluto.maven.pdf.old.PDFForm;
import demo.pluto.maven.pdf.old.field.FieldProperty;
import demo.pluto.maven.pdf.old.field.ImageField;
import demo.pluto.maven.pdf.old.field.SimpleField;
import demo.pluto.maven.pdf.old.field.TableField;
import demo.pluto.maven.pdf.old.field.TableValue;
import demo.pluto.maven.util.DateUtil;
import demo.pluto.maven.util.FileUtil;
import junit.framework.TestCase;



public class PDFOldTest extends TestCase {

    
//    public void testGeneratePDFOld(){
//        try {
//            PDFForm form = new PDFForm();
//            List<SimpleField> simpleFields = new ArrayList<SimpleField>();
//            
//            List<ImageField> binaryFields =new ArrayList<ImageField>();
//          
//            
//            simpleFields.add(new SimpleField("contractNumber","CA-BB"));
//            simpleFields.add(new SimpleField("projectName","BBBB"));
//            simpleFields.add(new SimpleField("customerName","CCCC"));
//            simpleFields.add(new SimpleField("application","DDDD"));
//            simpleFields.add(new SimpleField("installaddress","施工地址"));
//            simpleFields.add(new SimpleField("issueDate",DateUtil.date2String(new Date(), null)));
//            simpleFields.add(new SimpleField("installDate",DateUtil.date2String(new Date(), null)));
//            
//            
//            
//            String[] title = new String[3];
//            title[0]= "productName";
//            title[1]= "countAndUnit";
//            title[2]= "warrantyInfo";
//            String[][] table = new String[25][3];
//            int pageSize = 7;
//            for(int row=0;row <25;row ++){
//                table[row][0]= "一二三四五六七八九零一二三四五六七八九零一二三四五六七八九零一二三四五六七八九零一二三四五六七八九零"+row;
//                table[row][1]= "1234.45";
//                table[row][2]= "10年质保";
//            }
//            
//            TableField tableField  = new TableField("productName",new TableValue(title,table,29,pageSize));
//
//            byte[] result = FileUtil.readFile("C:\\github\\CHK_EWCS_TSSD\\workspace\\EWCSTSSD\\WebContent\\resources\\left.jpg");
//            
//            binaryFields.add(new ImageField("picture",result));
//            
//
//            form.setBinaryFields(binaryFields);
//            form.setSimpleFields(simpleFields);
//            form.setTableField(tableField);
//            
//            byte[] b = GeneratePDFOld.generatePDF("template/pdf/2017 TSSD长期质保合同Word模板.pdf",form);
//            FileOutputStream fos = new FileOutputStream("C:\\github\\Generate PDF\\test.pdf");
//            fos.write(b);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        
//    }
    
    
    public void testMergePDF(){
        try {
            byte[] mainPage = FileUtil.readFile("C:\\github\\Generate PDF\\test_result.pdf");
            PDFForm form = new PDFForm();
            List<SimpleField> simpleFields = new ArrayList<SimpleField>();
            List<ImageField> binaryFields =new ArrayList<ImageField>();
            simpleFields.add(new SimpleField("contractNumber","CA-BB"));
            simpleFields.add(new SimpleField("projectName","BBBB"));
            simpleFields.add(new SimpleField("customerName","CCCC"));
            simpleFields.add(new SimpleField("application","DDDD"));
            simpleFields.add(new SimpleField("installaddress","施工地址"));
            simpleFields.add(new SimpleField("issueDate",DateUtil.date2String(new Date(), null)));
            simpleFields.add(new SimpleField("installDate",DateUtil.date2String(new Date(), null)));
            

            byte[] image = FileUtil.readFile("C:\\github\\CHK_EWCS_TSSD\\workspace\\EWCSTSSD\\WebContent\\resources\\left.jpg");
            binaryFields.add(new ImageField("picture",image));
            

            form.setBinaryFields(binaryFields);
            form.setSimpleFields(simpleFields);
            
            
           
            List<byte[]> pageList = new ArrayList<byte[]>();
            for(int i=0;i<10;i++){
                String[] title = new String[3];
                title[0]= "productName";
                title[1]= "countAndUnit";
                title[2]= "warrantyInfo";
                String[][] table = new String[7][3];
                int pageSize = 7;
                for(int row=0;row <pageSize;row ++){
                    table[row][0]= "测试产品"+(int)(Math.random()*100)+"-"+row;
                    table[row][1]= "1234.45";
                    table[row][2]= "10年质保";
                }
                
                TableField tableField  = new TableField("productName",new TableValue(title,table,29,pageSize));
                form.setTableField(tableField);
                byte[] b = GeneratePDFOld.generatePDF("template/pdf/2017 TSSD长期质保合同Word模板.pdf",form);
                pageList.add(b);
            }
            
            byte[] result = GeneratePDFOld.mergePDF(mainPage, pageList);
            FileOutputStream fos = new FileOutputStream("C:\\github\\Generate PDF\\test2.pdf");
            fos.write(result);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
