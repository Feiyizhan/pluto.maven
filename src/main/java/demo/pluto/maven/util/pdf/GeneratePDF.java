package demo.pluto.maven.util.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.FieldPosition;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PRAcroForm.FieldInformation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import demo.pluto.maven.util.FileUtil;
import demo.pluto.maven.util.pdf.field.FieldProperty;
import demo.pluto.maven.util.pdf.field.TableValue;



public class GeneratePDF {
    
    public static final String FONT_NAME = "template/pdf/fonts/simfang.ttf";
    public static final float DEFAULT_FONTSIZE =8f;
    static  BaseFont DEFAULT_FONT=null ;
    static{
        try {
            DEFAULT_FONT =BaseFont.createFont(FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, false, null, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static byte[] generatePDF(String template,List<FieldProperty> dataList) throws FileNotFoundException, IOException, DocumentException{
        //读取PDF模版
        PdfReader reader = new PdfReader(FileUtil.getFileInput(template));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamp = new PdfStamper(reader,baos);
        

        
       
        PRAcroForm form = reader.getAcroForm();
//        //获取模版中的自定义的字段名列表
//        List<FieldInformation> formFields = form.getFields();
        //获取模版中的自定义字段的属性列表（包括字段的坐标信息）
        AcroFields fields = stamp.getAcroFields();
        if(dataList!=null){
            //设置值
            for(FieldProperty data:dataList){
                FieldPosition spot = FindFieldPosition(fields,data.getName());
                if(spot==null){  //字段不存在则忽略
                    continue;
                }
                
                BaseFont font = data.getFont()!=null?data.getFont():DEFAULT_FONT;
                float fontSize = data.getFontSize()!=null?data.getFontSize().floatValue():DEFAULT_FONTSIZE;
                
                Rectangle rectangle = spot.position;
                
                
                PdfContentByte over =stamp.getOverContent(spot.page); 
                over.beginText();
                //设置内容的坐标。
                over.setTextMatrix(rectangle.getLeft(),rectangle.getBottom());
                //设置字体和大小
                over.setFontAndSize(font,fontSize);
                switch (data.getValueType()) {
                    case STRING:
                        over.showText(data.getStringValue());
                        break;
                    case LIST:
                        break;
                    case TABLE:
                        TableValue tableValue = data.getTableValue();
                        String[][] values = tableValue.getValues();
                        String[] title = tableValue.getTitle();
                        float cellHeight =tableValue.getCellHeight();
                        List<FieldPosition> fpList = new ArrayList<FieldPosition>();
                        for(int cel=0;cel<title.length;cel++){
                            fpList.add(FindFieldPosition(fields,title[cel])); 
                        }
                        
                        for(int i=1;i<values.length;i++){
                            float x = 0;
                            float y = 0;
                            for(int j=0;j<values[i].length;j++){
                                if(fpList.get(j)!=null){
                                    x = fpList.get(j).position.getLeft();
                                    y= fpList.get(j).position.getBottom()-(i-1)*cellHeight;
                                    over.setTextMatrix(x,y);
                                    over.showText(values[i][j]);
                                }

                            }
                        }
                        break;
                    case BINARY:
                        
                        break;
                    default:
                        break;
                }
                
                over.endText();
            }
        }

        
        
        //取消字段的背景
        stamp.setFormFlattening(true);
        stamp.close();
        Document doc = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfCopy pdfCopy = new PdfCopy(doc, output);
        doc.open();
        int pageCount = reader.getNumberOfPages();
        for (int j = 1; j <= pageCount; j++) {
            PdfImportedPage impPage = pdfCopy.getImportedPage(new PdfReader(baos.toByteArray()), j);
            pdfCopy.addPage(impPage);
        }
        doc.close();
        byte[] result = output.toByteArray();
        output.close();
        return result;
        
        
    }
    
    /**
     * 获取字段的Position
     * @author A4YL9ZZ pxu3@mmm.com
     * @param fields
     * @param name
     * @return
     */
    public static FieldPosition FindFieldPosition(AcroFields fields,String name){
        List<FieldPosition> spots = fields.getFieldPositions(name);
        if(spots==null || spots.isEmpty()){  //字段不存在则忽略
            return null;
        }

        return spots.get(0);
    }
}
