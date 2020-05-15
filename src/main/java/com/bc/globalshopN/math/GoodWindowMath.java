package com.bc.globalshopN.math;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import com.bc.globalshopN.Utils.Image;
import com.bc.globalshopN.Utils.ItemNBT;
import com.bc.globalshopN.Utils.Time;
import com.bc.globalshopN.load.LoadCfg;
import com.bc.globalshopN.load.LoadData;
import com.bc.globalshopN.load.LoadLang;
import java.text.ParseException;
import java.util.List;

/**
 * @author wrx-18090248
 * @date 2020/5/5 21:37
 */
public class GoodWindowMath {
    /**
     * 打开商品详情页
     * @param goodId 商品ID
     * @param p 请求者
     * @return 界面
     */
    public static FormWindowSimple getGoodWindow(String goodId, Player p) throws ParseException {
        if(LoadData.shopData.get(goodId)==null){
            p.sendMessage(LoadLang.title+LoadLang.errorGoodNotExists);
            return null;
        }
        String player=LoadData.shopData.getString(goodId+".Player");
        double sellMoney=LoadData.shopData.getDouble(goodId+".Price.Money");
        double sellPoint=LoadData.shopData.getDouble(goodId+".Price.Point");
        Item item= ItemNBT.getItemByKey(goodId);
        int count=item.getCount();
        String lore="";
        for(String s:item.getLore()){
            lore+=s+"\n";
        }
        List<String> desc= LoadCfg.cfg.getStringList("Gui.Buy.Desc");
        String itemName=item.hasCustomName()?item.getCustomName():item.getName();
        String info="";
        for(String s:desc){
            info+=s.replaceAll("<player>",player)
                    .replaceAll("<money>",sellMoney+"")
                    .replaceAll("<point>",sellPoint+"")
                    .replaceAll("<item>",itemName)
                    .replaceAll("<count>",count+"")
                    .replaceAll("<lore>" , lore)
                    .replaceAll("<time>", Time.getSurplusDay(goodId)+"")
                    .replaceAll("&","§")+"\n";
        }
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.buyTitle+" §8$"+goodId,info);
        wd.addButton(new ElementButton(LoadCfg.buyBtText, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Buy.Button.Image"))));
        return wd;
    }

    /**
     * 获取确认购买界面-输入数量等操作
     * @param goodId 商品ID
     * @param p 用户-购买者
     * @return 购买界面
     */
    public static FormWindowCustom getMakeSureBuyWindow(String goodId, Player p){
        FormWindowCustom wd=new FormWindowCustom(LoadCfg.buySureTitle+" §8$"+goodId);
        int count=Integer.parseInt(LoadData.shopData.getString(goodId+".Item").split(":")[2]);
        ElementSlider countSlider=new ElementSlider(LoadCfg.buySlider,1,count,1,1);
        wd.addElement(countSlider);
        return wd;
    }
}
