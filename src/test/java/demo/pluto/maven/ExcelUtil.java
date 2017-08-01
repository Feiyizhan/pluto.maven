package demo.pluto.maven;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author A4YL9ZZ 
 *
 * <br/> Excel 相关工具类
 */
public class ExcelUtil {

    
    
    /**
     * 读取Excel文件，返回一个Workbook
     * @author A4YL9ZZ 
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Workbook readExcel(String fileName) throws IOException{
        // 获取InputStream
        InputStream inputStream = ExcelUtil.class.getResourceAsStream(fileName);
        // 读取EXCEL
        return new XSSFWorkbook(inputStream);
    }
    
    /**
     * 写入数据到Excel
     * @author A4YL9ZZ 
     * @param workbook
     * @param sheetName
     * @param valueList
     * @param startRow
     * @return
     */
    public static Workbook writeExcel(Workbook workbook,String sheetName,List<List<String>> valueList,int startRow){
        if(workbook==null || valueList ==null || sheetName==null){
            return workbook;
        }
        
        Sheet sheet = workbook.getSheet(sheetName);
        if(sheet==null){
            return null ;
        }
        if(startRow <0) startRow =0;
        
        for(int r=0;r<valueList.size();r++){
            List<String> list = valueList.get(r);
            Row row = sheet.createRow(r + startRow);
            for(int c=0;c<list.size();c++){
                String val = list.get(c);
                if(val !=null){
                    row.createCell(c).setCellValue(list.get(c));
                }
                
            }
        }
        
        return workbook;
    }
}
