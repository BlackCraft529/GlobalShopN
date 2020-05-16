package com.bc.Utils;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 16:36
 */
public class RandomId {
    /**
     * 获取随机商品ID
     * @return 随机商品ID
     */
    public static String getRandomGoodId(){
        String id="";
        id+=Math.round((Math.random()+1) * 1000)+"-"
                +Math.round((Math.random()+1) * 1000);
        return id;
    }

    /**
     * 获取随机邮件ID
     * @return 随机邮件ID
     */
    public static String getRandomMailId(){
        String id="";
        id+=Math.round((Math.random()+1) * 1000)+"~"
                +Math.round((Math.random()+1) * 1000);
        return id;
    }
}
