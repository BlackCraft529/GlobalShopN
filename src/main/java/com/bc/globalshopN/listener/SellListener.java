package com.bc.globalshopN.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.item.Item;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.Match;
import com.bc.Utils.PlayerFile;
import com.bc.Utils.RandomId;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadData;
import com.bc.Utils.load.LoadLang;
import com.bc.globalshopN.math.SellWindowMath;
import me.onebone.economyapi.EconomyAPI;
import net.player.api.Point;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 18:22
 */
public class SellListener implements Listener {
    public SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat dfs = new DecimalFormat("#.00");
    /**
     * 出售商品
     * @param e 点击出售界面按钮
     */
    @EventHandler
    public void sellItem(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return;}
        if (e.getResponse() == null){ return;}
        if(!(e.getWindow() instanceof FormWindowCustom)||e.wasClosed()){ return;}
        FormWindowCustom window = (FormWindowCustom) e.getWindow();
        if(!window.getTitle().equalsIgnoreCase(LoadCfg.sellTitle)){ return;}
        Player p=e.getPlayer();
        Item item=p.getInventory().getItemInHand();
        //是否在黑名单
        if(Match.isOnBlackList(item)){
            p.sendMessage(LoadLang.title+LoadLang.errorIsBlack);
            return;
        }
        //是否到达了最大上架数量
        if(Match.isMaxSell(p)){
            p.sendMessage(LoadLang.title+LoadLang.errorIsMaxSell);
            return;
        }
        try{
            //获取基础数据
            double sellMoney=Double.parseDouble(window.getResponse().getInputResponse(3));
            if(sellMoney<0){
                p.sendMessage(LoadLang.title+LoadLang.errorSell);
                return;
            }
            double chargeMoney=sellMoney*LoadCfg.chargeSellMoney;
            double sellPoint=0,chargePoint;
            //上架数量-手续费调整
            int count=LoadCfg.usePoint?(int)window.getResponse().getSliderResponse(5):(int)window.getResponse().getSliderResponse(4);
            chargeMoney*=count;
            //扣除手续费
            if(EconomyAPI.getInstance().myMoney(p)>=chargeMoney){
                EconomyAPI.getInstance().setMoney(p,EconomyAPI.getInstance().myMoney(p)-chargeMoney);
                //去除小数位
                long money= (int)(EconomyAPI.getInstance().myMoney(p)*100);
                double nowMoney=money/100.0;
                EconomyAPI.getInstance().setMoney(p,nowMoney);
                String chargeMoneyString=dfs.format(chargeMoney);
                chargeMoneyString=chargeMoneyString.equalsIgnoreCase(".00")?"0":chargeMoneyString;
                p.sendMessage(LoadLang.title+LoadLang.noticeCharge.replaceAll("<charge>",chargeMoneyString));
            }else{
                p.sendMessage(LoadLang.title+LoadLang.errorCharge);
                return;
            }
            if(LoadCfg.usePoint){
                sellPoint=Double.parseDouble(window.getResponse().getInputResponse(4));
                if(sellPoint<0){
                    p.sendMessage(LoadLang.title+LoadLang.errorSell);
                    return;
                }
                chargePoint=sellPoint*LoadCfg.chargeSellPoint;
                chargePoint*=count;
                if(Point.myPoint(p)>=chargePoint) {
                    Point.setPoint(p, Point.myPoint(p) - chargePoint);
                    //去除小数位
                    long point= (int)(Point.myPoint(p)*100);
                    double nowPoint=point/100.0;
                    Point.setPoint(p,nowPoint);
                    String chargePointString=dfs.format(chargePoint);
                    chargePointString=chargePointString.equalsIgnoreCase(".00")?"0":chargePointString;
                    p.sendMessage(LoadLang.title+LoadLang.noticeCharge.replaceAll("<charge>",chargePointString));
                }else{
                    p.sendMessage(LoadLang.title+LoadLang.errorCharge);
                    return;
                }
            }
            Item sItem=p.getInventory().getItemInHand();
            //数量错误
            if(sItem.getCount()<count){
                p.sendMessage(LoadLang.title+LoadLang.errorSell);
                return;
            }
            //取走物品
            if(sItem.getCount()>count) {
                sItem.setCount(sItem.getCount() - count);
                p.getInventory().setItemInHand(sItem);
            }else{
                p.getInventory().setItemInHand(new Item(0));
            }
            //上架-文件保存
            sItem.setCount(count);
            String itemNbt= ItemNBT.getItemNbt(sItem);
            //获取ID
            String id= RandomId.getRandomGoodId();
            while(LoadData.shopData.get(id)!=null){
                id=RandomId.getRandomGoodId();
            }
            //保存文件
            LoadData.shopData.set(id+".Player",p.getName());
            LoadData.shopData.set(id+".Item",itemNbt);
            LoadData.shopData.set(id+".Date",df.format(new Date()));
            LoadData.shopData.set(id+".VipLevel", PlayerFile.getPlayerVipLevel(p));
            LoadData.shopData.set(id+".Price.Money",sellMoney);
            LoadData.shopData.set(id+".Price.Point",sellPoint);
            LoadData.shopData.save();
            p.sendMessage(LoadLang.title+LoadLang.noticeSellSucc);
        }catch (Exception ex){
            p.sendMessage(LoadLang.title+LoadLang.errorSell);
            p.showFormWindow(SellWindowMath.getSellWindow(p));
            System.out.println(ex);
        }
    }
}
