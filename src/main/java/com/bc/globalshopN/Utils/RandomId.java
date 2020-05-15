package com.bc.globalshopN.Utils;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 16:36
 */
public class RandomId {
    public static String getRandomGoodId(){
        String id="";
        id+=Math.round((Math.random()+1) * 1000)+"-"
                +Math.round((Math.random()+1) * 1000);
        return id;
    }
}
