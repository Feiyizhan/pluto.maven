package demo.pluto.maven;

import java.util.Scanner;

public class GameTest {
    
    public static void main(String[] args){
        int maxCount = 0;
        int maxValue = 128;
        int minValue = 0;
        int data =0;
        int maxG = 0;
        int minG = 0;
        int gap =0;
        int checkReult = 99; //检查结果，0匹配，1偏大，-1 偏小，99 初始值
        double gapRate = 0.05;
        
        maxCount = (int) Math.ceil(Math.log(maxValue-minValue)/(Math.log(2)))-1 ;
        //System.out.println(maxCount);
        
        System.out.println("游戏开始");
        for(int i=1 ;i<=maxCount;i++){
            Scanner sc = new Scanner(System.in);
            System.out.println("数值范围(含):"+maxValue+" - "+minValue);
            System.out.println("当前为第"+i+"次，剩余"+(maxCount-i)+"次机会，请输入数字：");
            data = sc.nextInt();
            gap = (int) ((maxValue-minValue)*gapRate);
            //System.out.println("偏差:"+gap);
            if(data <= maxValue && data >=minValue){ //输入的值必须在有效范围内
                //判断输入的值是在偏大还是偏小区域，偏大区域就提示输入值偏小，偏小区域就提示偏大,相等提示偏小

                if(i==maxCount){ //最后一次，随机选择一个范围内数字，判断当前输入的数字是否匹配
                    if(((int) (Math.random()*(maxValue-minValue+1))+minValue) == data){
                        System.out.println("恭喜，猜中了，用了"+i+"次。");
                        checkReult = 0;
                        break;
                    }
                }
                maxG= maxValue-data ; //偏大区域数量
                minG= data-minValue;  //偏小区域数量
                if(Math.abs(maxG-minG)<=gap){  //偏大和偏小区域的数量差在范围内，则随机选择偏大还是偏小
                    
                    if( maxG==0 && maxG==minG){
                        System.out.println("恭喜，猜中了，用了"+i+"次。");
                        checkReult = 0;
                        break;
                    }
                    if(Math.random() > 0.5){ //偏大
                        checkReult = 1;
                        maxValue = data-1;
                    }else{ //偏小
                        checkReult = -1;
                        minValue = data+1;
                    }
                }else if(maxG > minG){ //偏大区域大于偏小区域就提示输入值偏小，将正确范围移动到偏大范围
                    checkReult = -1;
                    minValue = data+1;
                }else{ //偏大区域小于偏小区域就提示输入值偏大，将正确范围移动到偏小范围
                    checkReult = 0;
                    maxValue = data-1;
                }
                switch (checkReult) {//显示匹配结果 
                    case -1:
                        System.out.println("偏小");
                        break;
                    case 1:
                        System.out.println("偏大");
                        break;
                    default:
                        break;
                }
                

            }else{
                System.out.println("你浪费了一次机会.");
            }
        }
        if(checkReult!=0){//没猜中，在可能的值列表，随机抽一个
            int randomValue = 0;
            switch (checkReult) {
                case -1: //偏小
                    randomValue= (int) (Math.random()*(maxG)); //在偏大区间随机取一个数
                    randomValue = randomValue + data + 1;  //随机数返回是0开始的数字，因此需要+1跳过当前输入的值
                    break;
                case 1: //偏大
                    randomValue= (int) (Math.random()*(minG));  //在偏小区间随机取一个数
                    randomValue = data -randomValue-1 ; //随机数返回是0开始的数字，因此需要-1跳过当前输入的值
                    break;
                case 99: //初始范围
                    randomValue= (int) (Math.random()*(maxValue-minValue+1)); 
                    randomValue = randomValue + minValue;
                    break;
                default:
                    break;
            }
            

            System.out.println("失败！正确的数字是："+randomValue);
        }
        
    }

}
