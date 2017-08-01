

/* This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure, use or reproduction without the written
 * authorization of 3M is prohibited. If publication occurs, the following
 * notice applies:
 *      Copyright © 2009, 3M. All rights reserved.
 */
package demo.pluto.maven.pdf.old.field;

import com.lowagie.text.pdf.BaseFont;


/**
 * @author A4YL9ZZ 
 *
 */
public class FieldProperty {
	
	/**
	 * 字体大小
	 */
	private FontProperty fontProperty;
	
	/**
	 * 字体
	 */
	private BaseFont font;

    public FontProperty getFontProperty() {
        return fontProperty;
    }

    public void setFontProperty(FontProperty fontProperty) {
        this.fontProperty = fontProperty;
    }

    public BaseFont getFont() {
        return font;
    }



    public void setFont(BaseFont font) {
        this.font = font;
    }


}
