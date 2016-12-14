

/* This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure, use or reproduction without the written
 * authorization of 3M is prohibited. If publication occurs, the following
 * notice applies:
 *      Copyright © 2009, 3M. All rights reserved.
 */
package demo.pluto.maven.util.pdf.field;

import java.awt.Color;
import java.util.List;

import com.itextpdf.text.pdf.BaseFont;


/**
 * PDF Template field property.
 * 
 * @author cnv00845
 */
public class FieldProperty {

//	public static final String FIELD_FLAG = "setfflags";
//
//	public static final String FIELD_BGCOLOR = "bgcolor";
//
//	public static final String FIELD_FONT = "textfont";
//
//	public static final String FIELD_TEXT_SIZE = "textsize";
//	
//	public static final String GENERATION_RES_PREFIX = "template/pdf/font/";
//	
//	public static final String fontName = "times.ttf";

//	private BaseFont font;

	
    /**
     * @author A4YL9ZZ pxu3@mmm.com
     * <br/> 值类型。
     */
    public enum ValueType { STRING,LIST,TABLE,BINARY } 
    
	private String stringValue;
	
	private List<String> listValue;
	
	private TableValue tableValue;
	
	private byte[] binaryValue;
	
	private ValueType valueType;
	
	/**
	 * 字段名，用于定位
	 */
	private String name;
	
	/**
	 * 字体大小
	 */
	private Float fontSize;
	
	/**
	 * 字体
	 */
	private BaseFont font;
	
	
    /**
     * 创建String值类型的字段。
     * @author A4YL9ZZ pxu3@mmm.com
     * @param stringValue
     * @param name
     */
    public FieldProperty(String stringValue, String name) {
        super();
        this.stringValue = stringValue;
        this.valueType = ValueType.STRING;
        this.name = name;
    }
    
    

    /**
     * 创建指定类型的字段对象。
     * @author A4YL9ZZ pxu3@mmm.com
     * @param valueType
     * @param name
     */
    public FieldProperty(ValueType valueType, String name) {
        super();
        this.valueType = valueType;
        this.name = name;
    }



    /**
     * 创建二进制值字段对象
     * @author A4YL9ZZ pxu3@mmm.com
     * @param binaryValue
     * @param name
     */
    public FieldProperty(byte[] binaryValue, String name) {
        super();
        this.binaryValue = binaryValue;
        this.valueType = ValueType.BINARY;
        this.name = name;
    }



    /**
     * 创建列表类型的字段对象
     * @author A4YL9ZZ pxu3@mmm.com
     * @param listValue
     * @param name
     */
    public FieldProperty(List<String> listValue, String name) {
        super();
        this.listValue = listValue;
        this.valueType = ValueType.LIST;
        this.name = name;
    }



    /**
     * 创建table类型的字段
     * @author A4YL9ZZ pxu3@mmm.com
     * @param tableValue
     * @param name
     */
    public FieldProperty(TableValue tableValue, String name) {
        super();
        this.tableValue = tableValue;
        this.valueType = ValueType.TABLE;
        this.name = name;
    }



    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public List<String> getListValue() {
        return listValue;
    }

    public void setListValue(List<String> listValue) {
        this.listValue = listValue;
    }



    public TableValue getTableValue() {
        return tableValue;
    }



    public void setTableValue(TableValue tableValue) {
        this.tableValue = tableValue;
    }



    public byte[] getBinaryValue() {
        return binaryValue;
    }

    public void setBinaryValue(byte[] binaryValue) {
        this.binaryValue = binaryValue;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Float getFontSize() {
        return fontSize;
    }



    public void setFontSize(Float fontSize) {
        this.fontSize = fontSize;
    }



    public BaseFont getFont() {
        return font;
    }



    public void setFont(BaseFont font) {
        this.font = font;
    }


}
