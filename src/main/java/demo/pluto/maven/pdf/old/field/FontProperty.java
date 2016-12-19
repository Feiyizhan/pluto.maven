package demo.pluto.maven.pdf.old.field;


public enum FontProperty{
    PT5(5f,17.6f),PT5_5(5.5f,19.4f),PT6_5(6.5f,22.9f),PT7_5(7.5f,25.6f),PT9(9f,31.8f),PT10_5(10.5f,37.0f),PT12(12.0f,42.3f),PT14(14.0f,49.4f),PT15(15.0f,52.9f);
    private float type;
    private float mm;
    private FontProperty(float type, float mm) {
        this.type = type;
        this.mm = mm;
    }

    public float getType() {
        return type;
    }

    public void setType(float type) {
        this.type = type;
    }

    public float getMm() {
        return mm;
    }
    public void setMm(float mm) {
        this.mm = mm;
    }
    
    public static FontProperty findFontSize(float fontPt){
        for(FontProperty fs:FontProperty.values()){
            if(fs.type==fontPt) return fs;
        }
        return null;
    }
    /**
     * 获取小一号的字体
     * @author A4YL9ZZ pxu3@mmm.com
     * @param fp
     * @return
     */
    public static FontProperty getLess(FontProperty fp){
        FontProperty[] fps= FontProperty.values();
        for(int i=0;i<fps.length;i++){
            if(fp.type==fps[i].type){
                return i!=0? fps[i-1]:fps[0];
            }
        }
        return fps[0];
    }
    
    public static FontProperty getMin(){
        return FontProperty.values()[0];
    }
}