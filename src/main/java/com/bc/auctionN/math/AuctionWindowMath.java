package com.bc.auctionN.math;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import com.bc.Utils.Image;
import com.bc.Utils.ItemIDSunName;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.load.LoadAuction;
import com.bc.Utils.load.LoadCfg;

import java.util.List;

/**
 * @author Luckily_Baby
 * @date 2020/5/21 13:44
 */
public class AuctionWindowMath {
    /**
     * 获取拍卖市场菜单
     * @return 拍卖菜单
     */
    public static FormWindowSimple getAuctionMenu(){
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.auctionTitle,LoadCfg.auctionText);
        wd.addButton(new ElementButton(LoadCfg.auctionBtShop, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Auction.Button.Shop.Image"))));
        wd.addButton(new ElementButton(LoadCfg.auctionBtAuction,Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Auction.Button.Auction.Image"))));
        return wd;
    }

    /**
     * 获取拍卖市场列表
     * @return 市场列表
     */
    public static FormWindowSimple getAuctionShopWindow(){
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.auctionShopTitle,"");
        for(String  key: LoadAuction.autionData.getKeys(false)){
            String seller=LoadAuction.autionData.getString(key+".Player");
            Item item= ItemNBT.getItemByNbtString(LoadAuction.autionData.getString(key+".Item"));
            String itemName=item.hasCustomName()?item.getCustomName():item.getName();
            String type=LoadAuction.autionData.getBoolean(key+".IsPoint")?"§9[§b点券§9]":"§9[§e金币§9]";
            wd.addButton(new ElementButton(type+itemName+"\n§a"+seller+"§8%"+key, Image.getImageByCfg(ItemIDSunName.getIDByPath(item))));
        }
        return wd;
    }

    /**
     * 拍卖物品菜单
     * @return 拍卖物品
     */
    public static FormWindowCustom getAuctionGoodsWindow(Player p){
        FormWindowCustom wd=new FormWindowCustom(LoadCfg.auctionGoodsTitle);
        ElementInput minPrice=new ElementInput("§a起拍价格:");
        ElementInput addPrice=new ElementInput("§e最低加价:");
        String point=LoadCfg.usePoint?"§d点券拍卖":"§4未启用点券,勾选无效";
        ElementToggle isPoint=new ElementToggle(point);
        Item item=p.getInventory().getItemInHand();
        ElementSlider itemCount=new ElementSlider("§b拍卖数量",1,item.getCount(),1,1);

        wd.addElement(minPrice);
        wd.addElement(addPrice);
        wd.addElement(isPoint);
        wd.addElement(itemCount);

        return wd;
    }

    /**
     * 获取拍卖信息界面
     * @param key 商品ID
     * @return 拍卖界面
     */
    public static FormWindowSimple getAuctionInfoWindow(String key){
        List<String> desc=LoadCfg.cfg.getStringList("Gui.Auction.Info.Desc");
        String info="";
        String seller=LoadAuction.autionData.getString(key+".Player");
        String minPrice=LoadAuction.autionData.getString(key+".MinPrice");
        String addPrice=LoadAuction.autionData.getString(key+".Auction.AddPrice");
        String nowPrice=LoadAuction.autionData.getString(key+".Auction.MaxPrice");
        String count=LoadAuction.autionData.getString(key+".Auction.Count");
        String auctionPlayer=LoadAuction.autionData.getString(key+".Auction.Player");
        Item item=ItemNBT.getItemByNbtString(LoadAuction.autionData.getString(key+".Item"));
        String type=LoadAuction.autionData.getBoolean(key+".IsPoint")?"§b点券":"§e金币";
        String itemInfo=item.hasCustomName()?item.getCustomName():item.getName();
        for(String s:item.getLore()){
            itemInfo+=s+"\n";
        }
        for(String s:desc){
            info+=s.replaceAll("<seller>",seller)
                    .replaceAll("<minPrice>",minPrice)
                    .replaceAll("<addPrice>",addPrice)
                    .replaceAll("<nowPrice>",nowPrice)
                    .replaceAll("<count>",count)
                    .replaceAll("<auctionPlayer>",auctionPlayer)
                    .replaceAll("<item>",itemInfo)
                    .replaceAll("<type>",type)
                    .replaceAll("&","§")+"\n";
        }
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.auctionInfoTitle+"§8%"+key,info);
        wd.addButton(new ElementButton(LoadCfg.auctionBtGetAuction));
        return wd;
    }

    /**
     * 获取加价界面
     * @param key 商品ID
     * @return 加价界面
     */
    public static FormWindowCustom getAuctionAddPriceWindow(String key){
        FormWindowCustom wd=new FormWindowCustom(LoadCfg.auctionAddPriceTitle+"§8%"+key);
        String minAdd=LoadAuction.autionData.getString(key+".Auction.AddPrice");
        String nowPrice=LoadAuction.autionData.getString(key+".Auction.MaxPrice");
        ElementInput addPrice=new ElementInput("§a请输入你的加价: (最低加价:"+minAdd+" 当前价格: "+nowPrice+")");
        wd.addElement(addPrice);
        return wd;
    }

}
