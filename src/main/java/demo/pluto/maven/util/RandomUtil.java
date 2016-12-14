package demo.pluto.maven.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {

	public static <T> T getRandomItem(List<T> list){
		return list.get(getRandomRange(0,list.size()));
	}
	
	/**
	 * 返回指定范围的随机数（不包含end）
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandomRange(int start,int end){
		Random ran=new Random();
		return ran.nextInt(end-start)+start;
		
	}
	
	
	/**
	 * 返回指定范围的随机小数（不包含end）
	 * @param start
	 * @param end
	 * @param plen 小数位位数
	 * @return
	 */
	public static double getRandomRange(double start,double end,int plen){
		return NumberUtil.formatDouble(new Random().nextDouble()* (end-start)+start, plen);
		
	}
	
	/**
	 * 随机获取list中的元素,并从list中移除已选中的
	 * @param list
	 * @return
	 */
	public static <T> T getRandomItemAndRemoveIt(List<T> list){
		T t =list.get(getRandomRange(0,list.size()));
		list.remove(t);
		return t;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		List<Integer> list = new ArrayList<Integer>();
//		for(int i=0;i<3;i++){
//			list.add(i+1);
//		}
//		
//		for(int i=0;i<100;i++){
//			System.out.println(getRandomItem(list));
//		}
		for(int i=0;i<100;i++){
			System.out.println(getRandomRange(10.10,10.50,2));
		}
	}

}
