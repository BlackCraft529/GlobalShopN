package com.bc.globalshopN.load;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 15:35
 */
public class LoadHelp {
    public static List<String> getHelp(boolean isOp){
        List<String> help=new ArrayList<String>();
        help.add("§aGlobalShopN 帮助:");
        help.add("§a1./gsn open  §9打开主页面");
        if(isOp){
            help.add("§b/gsn reload  §8重载插件");
        }
        return help;
    }
}
