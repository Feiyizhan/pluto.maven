package demo.pluto.maven.util.pdf.old.field;


public class SimpleField extends AbstractField{


    private String simpleValue;
   
    public SimpleField(String name, String simpleValue) {
        super(name);
        this.simpleValue= simpleValue;
    }
    public String getSimpleValue() {
        return simpleValue;
    }
    public void setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
    }
    
    public FieldType getType() {
        // TODO Auto-generated method stub
        return FieldType.SIMPLE;
    }

}
