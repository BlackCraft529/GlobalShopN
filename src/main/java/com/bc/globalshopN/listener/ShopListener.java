package com.bc.globalshopN.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadData;
import com.bc.Utils.load.LoadLang;
import com.bc.globalshopN.math.GoodWindowMath;
import me.onebone.economyapi.EconomyAPI;
import net.player.api.Point;
import java.text.ParseException;

/**
 * @author wrx-18090248
 * @date 2020/5/5 21:35
 */
public class ShopListener implements Listener {
    /**
     * 点击商品详情页 - 进入确认界面
     * @param e 点击商品详情界面
     */
    @EventHandler
    public void onClickGoodsInfo(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().contains(LoadCfg.buyTitle+" §8$")){return;}
        Player p=e.getPlayer();
        if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.buyBtText)) {
            String goodId = ((FormWindowSimple) window).getTitle().split(" §8\\$")[1];
            if(LoadData.shopData.get(goodId)==null){
                p.sendMessage(LoadLang.title+LoadLang.errorGoodNotExists);
                return;
            }
            p.showFormWindow(GoodWindowMath.getMakeSureBuyWindow(goodId,p));
        }
    }

    /**
     * 点击确认界面 - 完成购买操作
     * @param e
     */
    @EventHandler
    public void onClickMakeSure(PlayerFormRespondedEvent e) throws ParseException {
        if(e.getPlayer()==null){ return;}
        if (e.getResponse() == null){ return;}
        if(!(e.getWindow() instanceof FormWindowCustom)||e.wasClosed()){ return;}
        FormWindowCustom window = (FormWindowCustom) e.getWindow();
        if(!window.getTitle().contains(LoadCfg.buySureTitle+" §8$")){ return;}
        Player p=e.getPlayer();
        String id= window.getTitle().split(" §8\\$")[1];
        String seller=LoadData.shopData.getString(id+".Player");
        int count=(int)window.getResponse().getSliderResponse(0);
        double needMoney=LoadData.shopData.getDouble(id+".Price.Money")*count;
        double needPoint=LoadData.shopData.getDouble(id+".Price.Point")*count;
        if(EconomyAPI.getInstance().myMoney(p)>=needMoney){
            Item item= ItemNBT.getItemByKey(id);
            int maxCount=item.getCount();
            //商品不足
            if(maxCount-count<0){
                p.sendMessage(LoadLang.title+LoadLang.errorGoodNotEnough);
                p.showFormWindow(GoodWindowMath.getGoodWindow(id,p));
                return;
            }
            if(LoadCfg.usePoint&&needPoint>0){
                if(Point.myPoint(p)>=needPoint&&EconomyAPI.getInstance().myMoney(p)>=needMoney){
                    Point.setPoint(p,Point.myPoint(p)-needPoint);
                    Point.setPoint(seller,Point.myPoint(seller)+needPoint);
                    EconomyAPI.getInstance().setMoney(p,EconomyAPI.getInstance().myMoney(p)-needMoney);
                    EconomyAPI.getInstance().setMoney(seller,EconomyAPI.getInstance().myMoney(seller)+needMoney);
                    //给予物品
                    item.setCount(count);
                    p.getInventory().addItem(item);
                    item.setCount(maxCount- count);
                    if(ItemNBT.saveItemToYml(item,id)){
                        p.sendMessage(LoadLang.title+LoadLang.noticeBuySucc);
                        return;
                    }else{
                        p.sendMessage(LoadLang.title+"§4插件内部错误:202");
                        return;
                    }
                }else{
                    p.sendMessage(LoadLang.title+LoadLang.errorNotEnough);
                    return;
                }
            }else{
                EconomyAPI.getInstance().setMoney(p,EconomyAPI.getInstance().myMoney(p)-needMoney);
                EconomyAPI.getInstance().setMoney(seller,EconomyAPI.getInstance().myMoney(seller)+needMoney);
                //给予物品
                item.setCount(count);
                p.getInventory().addItem(item);
                item.setCount(maxCount-count);
                if(ItemNBT.saveItemToYml(item,id)){
                    p.sendMessage(LoadLang.title+LoadLang.noticeBuySucc);
                    return;
                }else{
                    p.sendMessage(LoadLang.title+"§4插件内部错误:201");
                    return;
                }
            }
        }else{
            p.sendMessage(LoadLang.title+LoadLang.errorNotEnough);
            return;
        }
    }
    /**
     * 点击某件商品 获取一个商品的详情页
     * @param e 事件
     */
    @EventHandler
    public void onClickGoodShop(PlayerFormRespondedEvent e) throws ParseException {
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().equals(LoadCfg.shopTitle)){return;}
        Player p=e.getPlayer();
        int slot=0;
        for(ElementButton bt:((FormWindowSimple) window).getButtons()){
            if(bt.equals(((FormResponseSimple)e.getResponse()).getClickedButton())){
                break;
            }
            slot++;
        }
        String goodId="---1---";
        for(String key:LoadData.shopData.getKeys(false)){
            if(slot<=0){
                goodId=key;
                break;
            }
            slot-=1;
        }
        if(LoadData.shopData.get(goodId)==null){
            p.sendMessage(LoadLang.title+LoadLang.errorGoodNotExists);
            return;
        }
        p.showFormWindow(GoodWindowMath.getGoodWindow(goodId,p));
    }
}
