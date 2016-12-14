package demo.pluto.maven.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	/**
	 * 根据起始和终止生成int值列表。
	 * @param start
	 * @param end (包含end）
	 * @return
	 */
	public static List<Integer> generateIntList(int start,int end){
		List<Integer> list = new ArrayList<Integer>();
		for(int i=start;i<=end;i++){
			list.add(i);
		}
		return list;
	}
	
}
