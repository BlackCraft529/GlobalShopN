package com.bc.globalshopN.Utils;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import com.bc.globalshopN.load.LoadCfg;
import com.bc.globalshopN.load.LoadData;
import java.util.List;

/**
 * @author Luckily_Baby
 * @date 2020/5/6 15:24
 */
public class Match {
    /**
     * 是否到达了最大上架数量
     * @param p 上架玩家
     * @return boolean
     */
    public static boolean isMaxSell(Player p){
        int count=0;
        for(String key: LoadData.shopData.getKeys(false)){
            if(LoadData.shopData.getString(key+".Player").equalsIgnoreCase(p.getName())){
                count++;
                if(count>=LoadCfg.maxSell) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断是否在黑名单
     * @param item 要判断的物品
     * @return 是否在黑名单
     */
    public static boolean isOnBlackList(Item item){
        if(!isBlackName(item)&&!isBlackLore(item)&&!isBlackId(item)) {
            return false;
        }
        return true;
    }

    /**
     * 是否被屏蔽ID
     * @param item 要判断的物品
     * @return true:被屏蔽  false:未被屏蔽
     */
    public static boolean isBlackId(Item item){
        List<String> itemIdList= LoadCfg.cfg.getStringList("Shop.Black.Id");
        String itemId=item.getId()+":"+item.getDamage();
        return itemIdList.contains(itemId);
    }
    /**
     * 是否被屏蔽物品名字
     * @param item 要判断的物品
     * @return true:被屏蔽  false:未被屏蔽
     */
    public static boolean isBlackName(Item item){
        List<String> itemNameList=LoadCfg.cfg.getStringList("Shop.Black.Name");
        String itemName=item.hasCustomName()?item.getCustomName():item.getName();
        for(String s:itemNameList){
            if(itemName.contains(s)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isBlackLore(Item item){
        if(item.getLore()==null||item.getLore().length<=0) {
            return false;
        }
        List<String> itemLoreList=LoadCfg.cfg.getStringList("Shop.Black.Lore");
        String[] lore=item.getLore();
        for(String s:lore){
            for(String k:itemLoreList){
                if(s.contains(k)){
                    return true;
                }
            }
        }
        return false;
    }
}
