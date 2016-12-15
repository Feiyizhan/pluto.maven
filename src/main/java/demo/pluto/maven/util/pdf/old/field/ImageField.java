package demo.pluto.maven.util.pdf.old.field;

/**
 * 图片数据数据字段
 * @author A4YL9ZZ pxu3@mmm.com
 *
 */
public class ImageField extends AbstractField {


    private byte[] binaryValue;

    public ImageField(String name, byte[] binaryValue) {
        super(name);
        this.binaryValue = binaryValue;
    }


    public byte[] getBinaryValue() {
        return binaryValue;
    }

    public void setBinaryValue(byte[] binaryValue) {
        this.binaryValue = binaryValue;
    }

    public FieldType getType() {
        // TODO Auto-generated method stub
        return FieldType.IMAGE;
    }
    
    
}
