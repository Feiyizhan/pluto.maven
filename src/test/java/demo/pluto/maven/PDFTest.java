package demo.pluto.maven;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.DocumentException;

import demo.pluto.maven.email.EmailDemo;
import demo.pluto.maven.util.DateUtil;
import demo.pluto.maven.util.FileUtil;
import demo.pluto.maven.util.pdf.GeneratePDF;
import demo.pluto.maven.util.pdf.field.FieldProperty;
import demo.pluto.maven.util.pdf.field.FieldProperty.ValueType;
import demo.pluto.maven.util.pdf.field.TableValue;
import junit.framework.TestCase;

public class PDFTest extends TestCase {
    
    public void testGeneratPDF(){
        try {
            List<FieldProperty> dataList = new ArrayList<FieldProperty>();
            FieldProperty contractNumber = new FieldProperty("CA-BB","contractNumber");
            FieldProperty projectName = new FieldProperty("BBBB","projectName");
            FieldProperty customerName = new FieldProperty("CCCC","CustomerName");
            FieldProperty application = new FieldProperty("DDDD","application");
            FieldProperty installaddress = new FieldProperty("施工地址","installaddress");
            FieldProperty issueDate = new FieldProperty(DateUtil.date2String(new Date(), null),"issueDate");
            FieldProperty installDate = new FieldProperty(DateUtil.date2String(new Date(), null),"installDate");
            
            
            
            String[] title = new String[3];
            title[0]= "productName";
            title[1]= "countAndUnit";
            title[2]= "warrantyInfo";
            String[][] table = new String[7][3];

            for(int row=0;row <7;row ++){
                table[row][0]= "产品名称";
                table[row][1]= "1234.45";
                table[row][2]= "10年质保";
            }
            FieldProperty productName = new FieldProperty(new TableValue(title,table,29),"productName");

            byte[] result = FileUtil.readFile("C:\\github\\CHK_EWCS_TSSD\\workspace\\EWCSTSSD\\WebContent\\resources\\left.jpg");
            
            FieldProperty picture = new FieldProperty(result,"picture");
            
            dataList.add(contractNumber);
            dataList.add(projectName);
            dataList.add(customerName);
            dataList.add(application);
            dataList.add(installaddress);
            dataList.add(issueDate);
            dataList.add(installDate);
            dataList.add(productName);
            dataList.add(picture);
            
            
            
            byte[] b = GeneratePDF.generatePDF("template/pdf/2017 TSSD长期质保合同Word模板.pdf",dataList);
            FileOutputStream fos = new FileOutputStream("C:\\github\\Generate PDF\\test.pdf");
            fos.write(b);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
