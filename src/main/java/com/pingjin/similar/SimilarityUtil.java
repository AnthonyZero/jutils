package com.pingjin.similar;

/**
 * Levenshtein Distance编辑距离算法 计算字符串相似度
 * 编辑距离(删除，添加，替换 得到相等字符串所需次数)算法 
 * @author pingjin create 2017年12月1日
 *
 */
public class SimilarityUtil {
    
    public static float levenshtein(String str1,String str2) {  
        //计算两个字符串的长度。  
        int len1 = str1.length();  
        int len2 = str2.length();  
        //建立上面说的数组，比字符长度大一个空间  
        int[][] dif = new int[len1 + 1][len2 + 1];  
        //赋初值，步骤B。  
        for (int a = 0; a <= len1; a++) {  
            dif[a][0] = a;  
        }  
        for (int a = 0; a <= len2; a++) {  
            dif[0][a] = a;  
        }  
        //计算两个字符是否一样，计算左上的值  
        int temp;  
        for (int i = 1; i <= len1; i++) {  
            for (int j = 1; j <= len2; j++) {  
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {  
                    temp = 0;  
                } else {  
                    temp = 1;  
                }  
                //取三个值中最小的  
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,  
                        dif[i - 1][j] + 1);  
            }  
        }  
        //取数组右下角的值，同样不同位置代表不同字符串的比较  
        /*System.out.println("差异步骤："+dif[len1][len2]);*/  
        //计算相似度  
        float similarity = 1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());  
        return similarity; 
    }  
  
    //得到最小值  
    private static int min(int... is) {  
        int min = Integer.MAX_VALUE;  
        for (int i : is) {  
            if (min > i) {  
                min = i;  
            }  
        }  
        return min;  
    }  
    
    public static void main(String[] args) {  
    	long start=System.currentTimeMillis();
        //要比较的两个字符串  
        String str1 = "重庆市渝中区石油路174号龙湖·时代天街C馆5楼10号";  
        String str2 = "重庆渝中石油路174号龙湖时代天街C馆5-10";  
        float sim = levenshtein(str1,str2);
        System.out.println(sim);
        /*for (int i= 0; i< 20000; i++) {
        	for(int j = 0; j < 20000; j++) {
        		levenshtein(str1,str2);
        	}
        }*/
        long time = System.currentTimeMillis() - start;
        System.out.println("运行耗时= "+time/60/1000+" 毫秒");
    } 
}
