package demo.pluto.maven.util.pdf.old;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;






















import com.lowagie.text.BadElementException;
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
import demo.pluto.maven.util.pdf.old.field.FontProperty;
import demo.pluto.maven.util.pdf.old.field.ImageField;
import demo.pluto.maven.util.pdf.old.field.FieldProperty;
import demo.pluto.maven.util.pdf.old.field.SimpleField;
import demo.pluto.maven.util.pdf.old.field.TableField;
import demo.pluto.maven.util.pdf.old.field.TableValue;

/**
 * @author A4YL9ZZ pxu3@mmm.com
 * <br/>基于iText 2.1.*版本
 */
public class GeneratePDFOld {

        

    
    
    public static final String FONT_NAME_SONGTI = "template/pdf/fonts/simsun.ttc,1";
    public static final String FONT_NAME_TIMES = "template/pdf/fonts/times.ttf";
    public static final FontProperty DEFAULT_FONTSIZE =FontProperty.PT9;
    public static  BaseFont DEFAULT_FONT=null ;
    static{
        try {
            DEFAULT_FONT =BaseFont.createFont(FONT_NAME_SONGTI, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, false, null, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 根据PDFform的数据填充指定的模版，并返回填充好的结果。
     * @author A4YL9ZZ pxu3@mmm.com
     * @param template
     * @param pdfForm
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws DocumentException
     */
    public static byte[] generatePDF(String template,PDFForm pdfForm) throws FileNotFoundException, IOException, DocumentException{
        //读取PDF模版
        PdfReader reader = new PdfReader(FileUtil.getFileInput(template));
        //设置填充模版内容的输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamp = new PdfStamper(reader,baos);
        
        //多页的处理
        Document doc = new Document();
        //设置最终结果的输出流
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfCopy pdfCopy = new PdfCopy(doc, output);
        doc.open();
        
        if(pdfForm!=null){
            fullSimpleForm(pdfForm,stamp);
            fullImageForm(pdfForm,stamp);

            //关闭PDF写入，并将全部数据刷新到输出流
            stamp.close();
            //缓存下非动态数据
            byte[] simplePDF = baos.toByteArray();

            //生成动态数据
            TableField tableField = pdfForm.getTableField();
            if(tableField!=null && tableField.getTableValue()!=null){
                TableValue tableValue = tableField.getTableValue();
                //计算需要生成的页数
                int pageSize = tableValue.getPageSize();
                int pageCount = (int) Math.ceil(tableValue.getValues().length*1.0/pageSize) ;
                
                for(int pageNumber=1;pageNumber<=pageCount;pageNumber ++){
                    //清空本页的输出流
                    baos.reset();
                    //根据缓存的内容重新生成PDFReader
                    PdfReader tableReader = new PdfReader(simplePDF);
                    //根据PDFReader和输出流创建PDF内容编辑对象
                    PdfStamper tableStamp = new PdfStamper(tableReader,baos);
                    //填充表格数据
                    fullTableForm(pdfForm,tableStamp,pageNumber);
                    //设置PDF表单不可编辑。
                    tableStamp.setFormFlattening(true);
                    //关闭PDF编辑，并将全部数据刷新到输出流
                    tableStamp.close();
                    //根据当前页的输出流重新创建一个PDFReader对象，通过该对象生成PDF Page。
                    PdfImportedPage page = pdfCopy.getImportedPage(new PdfReader(baos.toByteArray()), 1);
                    //将页面增加到最终的的结果PDF
                    pdfCopy.addPage(page);
                }
            }
        }

        //关闭输出流和文档。
        baos.close();
        doc.close();
        output.close();
        byte[] result = output.toByteArray();
  
        return result;
        
        
    }
    private static FontProperty calculateFont(FontProperty fp,float width,float height,String value){
        //计算内容的宽度，判断宽度是否超过单元格的宽度.
        float oneChar = fp.getMm();
        float size = value.length() * oneChar;
        int countLine =(int) Math.ceil(size *1.0 / width);
        //计算字体高度是否超过单元格高度，超过则缩小字体.
        if(oneChar*0.5f*countLine > height){
            //判断是否是最小号的字体了，如果是，则不再递归
            if(fp.getType() == FontProperty.getMin().getType()){
                return fp;
            }else{
               return calculateFont(FontProperty.getLess(fp),width,height,value);
            }
        }
        return fp;
    }
    
    private static void fullSimpleCell(BaseFont font,FontProperty fp,float x,float y,String value,PdfContentByte over,float width,float height){
        FontProperty fpT = calculateFont(fp,width,height,value);
        float oneChar = fpT.getMm();
        float size = value.length() * oneChar;
        int lineChars = (int)Math.floor(width * 1.0 / oneChar);
        int countLine =(int) Math.ceil(size *1.0 / width);
        //设置字体和大小
        over.setFontAndSize(font,fpT.getType());
        y = y+(oneChar*0.5f*(countLine-1))  ;
        for(int i=0;i<countLine;i++){
          //设置内容的坐标。
          over.setTextMatrix(x,y);
          //设置值
          if((i+1)*lineChars > value.length()){
              over.showText(value.substring(i*lineChars)); 
          }else{
              over.showText(value.substring(i*lineChars,(i+1)*lineChars)); 
          }
          
          y-=(oneChar*0.5f);
        }
      

    }
    
    /**
     * 填充简单数据
     * @author A4YL9ZZ pxu3@mmm.com
     * @param pdfForm
     * @param stamp
     * @throws MalformedURLException
     * @throws IOException
     * @throws DocumentException
     */
    private static void fullSimpleForm(PDFForm pdfForm,PdfStamper stamp) throws MalformedURLException, IOException, DocumentException{
        if(pdfForm!=null){
            //获取模版中的自定义字段的属性列表（包括字段的坐标信息）
            AcroFields fields = stamp.getAcroFields();
            //设置普通的值
            List<SimpleField> simpleFields= pdfForm.getSimpleFields();
            if(simpleFields!=null){
                for(SimpleField field:simpleFields){
                    FieldPosition spot = FindFieldPosition(fields,field.getName()); 
                    if(spot==null){  //字段不存在则忽略
                        continue;
                    }
                    int page=(int)spot.getPage();
                    float x=spot.getX();
                    float y = spot.getY();
                    FieldProperty fieldProperty = field.getFieldProperty();
                   
                    BaseFont font = fieldProperty!=null && fieldProperty.getFont()!=null?fieldProperty.getFont():DEFAULT_FONT;
                    FontProperty fp = fieldProperty!=null?fieldProperty.getFontProperty():DEFAULT_FONTSIZE;
                    float fontSize =fp.getType();
                    
                    PdfContentByte over =stamp.getOverContent(page); 
                    over.beginText();
                    //设置内容的坐标。
                    over.setTextMatrix(x,y);
                    //设置字体和大小
                    over.setFontAndSize(font,fontSize);
                    //设置值
                    over.showText(field.getSimpleValue());
                    over.endText();
                    
                }
            }
        }
    }
    
    
    
    /**
     * 填充图片数据
     * @author A4YL9ZZ pxu3@mmm.com
     * @param pdfForm
     * @param stamp
     * @throws MalformedURLException
     * @throws IOException
     * @throws DocumentException
     */
    private static void fullImageForm(PDFForm pdfForm,PdfStamper stamp) throws MalformedURLException, IOException, DocumentException{
        if(pdfForm!=null){
            //获取模版中的自定义字段的属性列表（包括字段的坐标信息）
            AcroFields fields = stamp.getAcroFields();
            //设置二进制的值
            List<ImageField> binaryFields= pdfForm.getBinaryFields();
            if(binaryFields!=null){
                for(ImageField field:binaryFields){
                    FieldPosition spot = FindFieldPosition(fields,field.getName()); 
                    if(spot==null){  //字段不存在则忽略
                        continue;
                    }
                    int page=(int)spot.getPage();
                    float x=spot.getX();
                    float y = spot.getY();
                    FieldProperty fieldProperty = field.getFieldProperty();
                    BaseFont font = fieldProperty!=null && fieldProperty.getFont()!=null?fieldProperty.getFont():DEFAULT_FONT;
                    FontProperty fp = fieldProperty!=null?fieldProperty.getFontProperty():DEFAULT_FONTSIZE;
                    float fontSize =fp.getType();
                    
                    
                    PdfContentByte over =stamp.getOverContent(page); 
                    over.beginText();
                    //设置内容的坐标。
                    over.setTextMatrix(x,y);
                    //设置字体和大小
                    over.setFontAndSize(font,fontSize);
                    
                    Image img = Image.getInstance(field.getBinaryValue());
                    //图片缩放
                    handlePerImage(img, spot);
                    //设置值
                    over.addImage(img);
                    over.endText();
                    
                }
            }
        }
    }
    
    /**
     * 填充表格
     * @author A4YL9ZZ pxu3@mmm.com
     * @param pdfForm
     * @param stamp
     * @param pageNumber
     */
    private static void fullTableForm(PDFForm pdfForm,PdfStamper stamp,int pageNumber){
        TableField tableField = pdfForm.getTableField();
        TableValue tableValue = tableField.getTableValue();
        //获取模版中的自定义字段的属性列表（包括字段的坐标信息）
        AcroFields fields = stamp.getAcroFields();
        FieldPosition spot = FindFieldPosition(fields,tableField.getName());
        if(spot==null){  //字段不存在则忽略
            return;
        }
        int page=(int)spot.getPage();
        float x=spot.getX();
        float y = spot.getY();
        
        FieldProperty fieldProperty = tableField.getFieldProperty();
        BaseFont font = fieldProperty!=null && fieldProperty.getFont()!=null?fieldProperty.getFont():DEFAULT_FONT;
        FontProperty fp = fieldProperty!=null?fieldProperty.getFontProperty():DEFAULT_FONTSIZE;
        float fontSize =fp.getType();
        
        String[][] values = tableValue.getValues();
        String[] title = tableValue.getTitle();
        float cellHeight =tableValue.getCellHeight();
        List<FieldPosition> fpList = new ArrayList<FieldPosition>();
        //获取标题行的位置
        for(int cel=0;cel<title.length;cel++){
            fpList.add(FindFieldPosition(fields,title[cel])); 
        }
        
        int pageSize = tableValue.getPageSize();
        int offset = (pageNumber -1) * pageSize;
        int max =Math.min(values.length, offset+pageSize);
        int count = max -offset;
        
        PdfContentByte over =stamp.getOverContent(page); 
        over.beginText();
        //设置字体和大小
        over.setFontAndSize(font,fontSize);
        
        for(int i=0;i<count;i++){

            for(int j=0;j<values[i].length;j++){
                if(fpList.get(j)!=null){
                    x = fpList.get(j).getX();
                    y= fpList.get(j).getY()-i*cellHeight;
                    fullSimpleCell(font,fp,x,y,values[i+offset][j],over,spot.getWidth(),cellHeight);
//                    //设置内容的坐标。
//                    over.setTextMatrix(x,y);
//                    over.showText(values[i+offset][j]);
                }

            }
        }
        
        over.endText();
        
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