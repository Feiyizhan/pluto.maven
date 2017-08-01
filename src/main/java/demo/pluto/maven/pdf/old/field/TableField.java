package demo.pluto.maven.pdf.old.field;

/**
 * 表格数据字段
 * @author A4YL9ZZ 
 *
 */
public class TableField extends AbstractField{
    
    private TableValue tableValue;

    public TableField(String name, TableValue tableValue) {
        super(name);
        this.tableValue = tableValue;
    }

    public TableValue getTableValue() {
        return tableValue;
    }

    public void setTableValue(TableValue tableValue) {
        this.tableValue = tableValue;
    }
    
    public FieldType getType() {
        // TODO Auto-generated method stub
        return FieldType.TABLE;
    }

}
