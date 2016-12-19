package demo.pluto.maven.pdf.old.field;

/**
 * 字段接口的抽象实现
 * @author A4YL9ZZ pxu3@mmm.com
 *
 */
public abstract class AbstractField implements Field {

    
    public  AbstractField(String name){
        this.name =name;
    }
    
    /**
     * 字段名，用于定位
     */
    private String name;
    
    /**
     * 字段属性（包括字体，字体大小）
     */
    private FieldProperty fieldProperty;
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldProperty getFieldProperty() {
        return fieldProperty;
    }

    public void setFieldProperty(FieldProperty fieldProperty) {
        this.fieldProperty = fieldProperty;
    }


}
