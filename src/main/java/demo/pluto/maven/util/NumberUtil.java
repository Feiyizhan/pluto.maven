package demo.pluto.maven.util;

public class NumberUtil {

	
	/**
	 * 将数字转为指定长度字符串,不足位，前补0
	 * @param val
	 * @param len
	 */
	public static String int2String(int val,int len){
		if(len<0){
			return String.valueOf(val);
		}
		char[] chars = new char[len];
		String str = String.valueOf(val);
		char[] valChars=str.toCharArray();
		int diff= len-valChars.length;
		if(diff<=0){
			return String.valueOf(valChars);
		}else{
			for(int i=0;i<len;i++){
				if(i<diff){
					chars[i]='0';
				}else{
					chars[i]= valChars[i-diff];
				}
			}
			return String.valueOf(chars);
		}

	}

	
	/**
	 * 格式化double数字，保留指定长度小数位（末尾不进位）
	 * @param val
	 * @param pLen  保留的小数位长度
	 * @return
	 */
	public static double formatDouble(double val,int pLen){
		double len = Math.pow(10.0, (double)pLen);
		long l = (long)(val * len);
		return (double)(l/len);
	}
	
	
	public static void main(String[] args){
		System.out.println(int2String(123,10));
		System.out.println(String.format("%010d", 123));
		System.out.println(formatDouble(1.0123456789, 6));
		int newseq = 99998;
		int fixedLength =5;
		newseq +=1;
		if(newseq>(Math.pow(10, fixedLength)-1)){
			newseq=1;
		}
		System.out.println(newseq);
	}
}
