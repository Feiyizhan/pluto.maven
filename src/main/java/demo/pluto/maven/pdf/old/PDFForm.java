package demo.pluto.maven.pdf.old;

import java.util.List;

import demo.pluto.maven.pdf.old.field.ImageField;
import demo.pluto.maven.pdf.old.field.SimpleField;
import demo.pluto.maven.pdf.old.field.TableField;

public class PDFForm {
    
    private List<SimpleField> simpleFields;
    private TableField tableField;
    private List<ImageField> binaryFields;
    public List<SimpleField> getSimpleFields() {
        return simpleFields;
    }
    public void setSimpleFields(List<SimpleField> simpleFields) {
        this.simpleFields = simpleFields;
    }
    public TableField getTableField() {
        return tableField;
    }
    public void setTableField(TableField tableField) {
        this.tableField = tableField;
    }
    public List<ImageField> getBinaryFields() {
        return binaryFields;
    }
    public void setBinaryFields(List<ImageField> binaryFields) {
        this.binaryFields = binaryFields;
    }
    
   
}
