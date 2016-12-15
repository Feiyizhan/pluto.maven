package demo.pluto.maven.util.pdf.old;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;















import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import demo.pluto.maven.util.FileUtil;
import demo.pluto.maven.util.pdf.old.field.FieldProperty;
import demo.pluto.maven.util.pdf.old.field.TableValue;

/**
 * @author A4YL9ZZ pxu3@mmm.com
 * <br/>基于iText 2.1.*版本
 */
public class GeneratePDFOld {
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


        //获取模版中的自定义字段的属性列表（包括字段的坐标信息）
        AcroFields fields = stamp.getAcroFields();
        if(dataList!=null){
            //设置值
            for(FieldProperty data:dataList){
                FieldPosition spot = FindFieldPosition(fields,data.getName());
                if(spot==null){  //字段不存在则忽略
                    continue;
                }
                int page=(int)spot.getPage();
                float x=spot.getX();
                float y = spot.getY();
                
                BaseFont font = data.getFont()!=null?data.getFont():DEFAULT_FONT;
                float fontSize = data.getFontSize()!=null?data.getFontSize().floatValue():DEFAULT_FONTSIZE;
                
                PdfContentByte over =stamp.getOverContent(page); 
                over.beginText();
                //设置内容的坐标。
                over.setTextMatrix(x,y);
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

                            for(int j=0;j<values[i].length;j++){
                                if(fpList.get(j)!=null){
                                    x = fpList.get(j).getX();
                                    y= fpList.get(j).getY()-i*cellHeight;
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
        //多页的处理
//        Document doc = new Document();
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        PdfCopy pdfCopy = new PdfCopy(doc, output);
//        doc.open();
//        int pageCount = reader.getNumberOfPages();
//        for (int j = 1; j <= pageCount; j++) {
//            PdfImportedPage impPage = pdfCopy.getImportedPage(new PdfReader(baos.toByteArray()), j);
//            pdfCopy.addPage(impPage);
//        }
//        doc.close();
//        baos.close();
//        byte[] result = output.toByteArray();
//        output.close();
        byte[] result=baos.toByteArray();
        baos.close();
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
        float[] spots = fields.getFieldPositions(name);
        if(spots==null || spots.length==0){  //字段不存在则忽略
            return null;
        }
        FieldPosition fp = new FieldPosition(spots[0],spots[1],spots[2],spots[3],spots[4]);
        return fp;
    }
    
    /**
     * 根据单元格的信息，调整图片的缩放比例和相对位置。
     * @author A4YL9ZZ pxu3@mmm.com
     * @param img
     * @param spot
     * @return
     */
    public static void handlePerImage(Image img, final FieldPosition spot) {
        //获取图片的高和宽
        float width = img.getWidth();
        float height = img.getHeight();
        //获取单元格的高和宽（保留12像素的边距）
        float maxHeight = spot.getHeight()-spot.getY() - 12;
        float maxWidth = spot.getWidth()-spot.getX() - 12;
        
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
        float x = spot.getX() + (maxWidth - abstractW) / 2;
        //y 坐标一样
        float y = spot.getY() + (maxHeight - abstractH) / 2;
        //设置图像的相对坐标
        img.setAbsolutePosition(x, y);
        
        //计算图片的X轴最大坐标。（用于判断是否超过单元格）
//        float positionimg = spot.position.getLeft() + (maxWidth - abstractW) / 2 + abstractW
//                / 2;

        
    }
    

}


class FieldPosition{
    private float height;
    private float width;
    private float x;
    private float y;
    private float page;
    
    public FieldPosition(float page,float x, float y,float width,float height) {
        super();
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        this.page = page;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public float getWidth() {
        return width;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getPage() {
        return page;
    }
    public void setPage(float page) {
        this.page = page;
    }
    
    
    
}