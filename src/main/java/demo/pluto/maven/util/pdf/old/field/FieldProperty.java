

/* This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure, use or reproduction without the written
 * authorization of 3M is prohibited. If publication occurs, the following
 * notice applies:
 *      Copyright © 2009, 3M. All rights reserved.
 */
package demo.pluto.maven.util.pdf.old.field;

import com.lowagie.text.pdf.BaseFont;

/**
 * PDF Template field property.
 * 
 * @author cnv00845
 */
public class FieldProperty {
	
	/**
	 * 字体大小
	 */
	private Float fontSize;
	
	/**
	 * 字体
	 */
	private BaseFont font;
	


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
