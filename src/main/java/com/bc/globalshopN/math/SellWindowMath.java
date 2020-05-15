package com.bc.globalshopN.math;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.item.Item;
import com.bc.globalshopN.load.LoadCfg;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 18:09
 */
public class SellWindowMath {
    /**
     * 获取出售界面
     * @param p 获取者
     * @return 出售界面
     */
    public static FormWindowCustom getSellWindow(Player p){
        if(p.getInventory().getItemInHand()==null){
            return null;
        }
        Item item=p.getInventory().getItemInHand();
        String itemNameString=item.hasCustomName()?item.getCustomName():item.getName();
        FormWindowCustom wd=new FormWindowCustom(LoadCfg.sellTitle);
        ElementLabel msg=new ElementLabel(LoadCfg.sellText);
        ElementLabel itemName=new ElementLabel(LoadCfg.sellLbItemName.replaceAll("<item>",itemNameString));
        ElementLabel itemCount=new ElementLabel(LoadCfg.sellLbItemCount.replaceAll("<count>",item.getCount()+""));
        ElementInput sellMoney=new ElementInput(LoadCfg.sellIpMoney);
        ElementSlider countSlider=new ElementSlider(LoadCfg.sellSlider,1,item.getCount(),1,1);

        wd.addElement(msg);
        wd.addElement(itemName);
        wd.addElement(itemCount);
        wd.addElement(sellMoney);
        //点券
        if(LoadCfg.usePoint){
            ElementInput sellPoint=new ElementInput(LoadCfg.sellIpPoint);
            wd.addElement(sellPoint);
        }
        wd.addElement(countSlider);
        return wd;
    }
}
