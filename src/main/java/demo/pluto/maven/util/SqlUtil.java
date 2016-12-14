package demo.pluto.maven.util;

import java.util.ArrayList;
import java.util.List;

public class SqlUtil {
	

	
	
	public static void generateInsertSql(String str){
		String[] fileds = str.split("[,]");
		List<String> filedList = new ArrayList<String>(fileds.length);
		//输出
		System.out.println("		(");
		System.out.println("		  <dynamic prepend=\" \">");
		for(int i =0;i<fileds.length;i++){
			String[] filed = fileds[i].trim().split("[ ]");
			filed[1]= filed[1].replace("\"", "");
			filedList.add(filed[1]);
			System.out.println("		     <isNotEmpty prepend=\", \" property=\""+filed[1].trim()+"\">"+filed[0].trim()+"</isNotEmpty>");
		}
		System.out.println("		  </dynamic>");
		System.out.println("		) VALUES (");
		System.out.println("		  <dynamic prepend=\" \">");
		for(String val :filedList){
			System.out.println("		     <isNotEmpty prepend=\", \" property=\""+val.trim()+"\">#"+val.trim()+"#</isNotEmpty>");
		}
		System.out.println("		  </dynamic>");
		System.out.println("		) ");
		System.out.println("        <selectKey resultClass=\"java.lang.Integer\" keyProperty=\"id\" > ");
		System.out.println("		   SELECT @@IDENTITY AS id ");
		System.out.println("		</selectKey>  ");
	}
	

	public static void main(String[] args){
		// TODO Auto-generated method stub
//		String fileName = "C:\\work\\GDC\\Source\\workspace\\Demo\\resources\\data.txt";
		String fileName = "data.txt";
		generateInsertSql(FileUtil.readFile2String(fileName,"UTF-8"));

	}

}
