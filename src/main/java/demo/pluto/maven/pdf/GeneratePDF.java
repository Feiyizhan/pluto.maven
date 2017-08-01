package demo.pluto.maven.pdf;

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
import com.itextpdf.text.Image;
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

import demo.pluto.maven.pdf.field.FieldProperty;
import demo.pluto.maven.pdf.field.TableValue;
import demo.pluto.maven.util.FileUtil;



/**
 * @author A4YL9ZZ 
 * <br/>基于iText 5.5.10
 */
public class GeneratePDF {
    
    public static final String FONT_NAME_SONGTI = "template/pdf/fonts/simsun.ttc";
    public static final String FONT_NAME_TIMES = "template/pdf/fonts/times.ttf";
    public static final float DEFAULT_FONTSIZE =9f;
    static  BaseFont DEFAULT_FONT=null ;
    static{
        try {
            DEFAULT_FONT =BaseFont.createFont(FONT_NAME_SONGTI, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, false, null, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static byte[] generatePDF(String template,List<FieldProperty> dataList) throws FileNotFoundException, IOException, DocumentException{
        //读取PDF模版
        PdfReader reader = new PdfReader(FileUtil.getFileInput(template));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamp = new PdfStamper(reader,baos);

        //PRAcroForm form = reader.getAcroForm();

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
                //设置值
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
                        
                        for(int i=0;i<values.length;i++){
                            float x = 0;
                            float y = 0;
                            for(int j=0;j<values[i].length;j++){
                                if(fpList.get(j)!=null){
                                    x = fpList.get(j).position.getLeft();
                                    y= fpList.get(j).position.getBottom()-i*cellHeight;
                                    over.setTextMatrix(x,y);
                                    over.showText(values[i][j]);
                                }

                            }
                        }
                        break;
                    case BINARY:
                        Image img = Image.getInstance(data.getBinaryValue());
                        handlePerImage(img, spot);
                        over.addImage(img);
                        
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
        baos.close();
        byte[] result = output.toByteArray();
        output.close();
        return result;
        
        
    }
    
    /**
     * 获取字段的Position
     * @author A4YL9ZZ 
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
    
    /**
     * 根据单元格的信息，调整图片的缩放比例和相对位置。
     * @author A4YL9ZZ 
     * @param img
     * @param spot
     * @return
     */
    public static void handlePerImage(Image img, final FieldPosition spot) {
        //获取图片的高和宽
        float width = img.getWidth();
        float height = img.getHeight();
        //获取单元格的高和宽（保留12像素的边距）
        float maxHeight = spot.position.getHeight() - 12;
        float maxWidth = spot.position.getWidth() - 12;
        
        //计算高和宽需要的缩放比例
        float rateh = maxHeight / height;
        float ratew = maxWidth / width;
        float rate = 1;
        if ((rateh < 1) || (ratew < 1)) {
            rate = rateh > ratew ? ratew : rateh;
        }
        //得到缩放后的宽和高
        float abstractH = height * rate;
        float abstractW = width * rate;
        //缩放图像.
        img.scaleAbsolute(abstractW, abstractH);
        
        //x 坐标等于单元格最大宽度-图片的最大宽度的一半，也就是在单元格的中心位置。
        float x = spot.position.getLeft() + (maxWidth - abstractW) / 2;
        //y 坐标一样
        float y = spot.position.getBottom() + (maxHeight - abstractH) / 2;
        //设置图像的相对坐标
        img.setAbsolutePosition(x, y);
        
        //计算图片的X轴最大坐标。（用于判断是否超过单元格）
//        float positionimg = spot.position.getLeft() + (maxWidth - abstractW) / 2 + abstractW
//                / 2;

        
    }
    
}
