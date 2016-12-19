package demo.pluto.maven.pdf.old.field;

/**
 * @author A4YL9ZZ pxu3@mmm.com
 * <br/> table类型的值。
 */
public class TableValue{
    /**
     * 标题(用于和表头定位和单元格定位）
     */
    String[] title;
    /**
     * 表格值
     */
    String[][] values;
    /**
     * 单元格高度
     */
    float cellHeight;
    
    /**
     * 页面记录数
     */
    int pageSize;
    
    public String[] getTitle() {
        return title;
    }
    public void setTitle(String[] title) {
        this.title = title;
    }
    public String[][] getValues() {
        return values;
    }
    public void setValues(String[][] values) {
        this.values = values;
    }
    public float getCellHeight() {
        return cellHeight;
    }
    public void setCellHeight(float cellHeight) {
        this.cellHeight = cellHeight;
    }
    
    
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public TableValue(String[] title, String[][] values, float cellHeight,int pageSize) {
        super();
        this.title = title;
        this.values = values;
        this.cellHeight = cellHeight;
        this.pageSize=pageSize;
    }
    
    
    
}
