package com.bc.auctionN.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.RandomId;
import com.bc.Utils.load.LoadAuction;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadLang;
import com.bc.auctionN.math.AuctionWindowMath;
import com.bc.gsn;
import me.onebone.economyapi.EconomyAPI;
import net.player.api.Point;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author Luckily_Baby
 * @date 2020/5/21 16:13
 */
public class AuctionListener implements Listener {
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat dfs = new DecimalFormat("#.00");
    /**
     * 拍卖菜单操作界面
     * @param e 事件
     */
    @EventHandler
    public void onClickMenu(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().equals(LoadCfg.auctionTitle)){return;}
        Player p=e.getPlayer();
        if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.auctionBtAuction)) {
            p.showFormWindow(AuctionWindowMath.getAuctionGoodsWindow(p));
        }else if (((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.auctionBtShop)){
            p.showFormWindow(AuctionWindowMath.getAuctionShopWindow());
        }
    }

    /**
     * 点击商品列表
     * @param e 事件
     */
    @EventHandler
    public void onClickGoodsListWindow(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().equals(LoadCfg.auctionShopTitle)){return;}
        Player p=e.getPlayer();
        String key=((FormResponseSimple)e.getResponse()).getClickedButton().getText().split("§8%")[1];
        if(LoadAuction.autionData.get(key)==null){
            p.sendMessage(LoadLang.title+LoadLang.errorGoodNotExists);
            return;
        }
        p.showFormWindow(AuctionWindowMath.getAuctionInfoWindow(key));
    }

    /**
     * 点击商品详细信息界面
     * @param e 窗体相应事件
     */
    @EventHandler
    public void onClickInfoWindow(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().contains(LoadCfg.auctionInfoTitle)){return;}
        Player p=e.getPlayer();
        String key=((FormWindowSimple) window).getTitle().split("§8%")[1];
        if(LoadAuction.autionData.get(key)==null){
            p.sendMessage(LoadLang.title+LoadLang.errorGoodNotExists);
            return;
        }
        p.showFormWindow(AuctionWindowMath.getAuctionAddPriceWindow(key));
    }

    /**
     * 点击加价界面
     * @param e 事件
     */
    @EventHandler
    public void onClickAddPriceWindow(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return;}
        if (e.getResponse() == null){ return;}
        if(!(e.getWindow() instanceof FormWindowCustom)||e.wasClosed()){ return;}
        FormWindowCustom window = (FormWindowCustom) e.getWindow();
        if(!window.getTitle().contains(LoadCfg.auctionAddPriceTitle)){ return;}
        Player p=e.getPlayer();
        String key=window.getTitle().split("§8%")[1];
        if(LoadAuction.autionData.get(key)==null){
            p.sendMessage(LoadLang.title+LoadLang.errorGoodNotExists);
            return;
        }
        if(window.getResponse().getInputResponse(0)==null){
            p.showFormWindow(AuctionWindowMath.getAuctionAddPriceWindow(key));
            return;
        }
        try {
            double addPrice = Double.parseDouble(window.getResponse().getInputResponse(0));
            double nowPrice=LoadAuction.autionData.getDouble(key+".Auction.MaxPrice")+addPrice;
            if(LoadAuction.autionData.getBoolean(key+".IsPoint")){
                if(Point.myPoint(p)<nowPrice){
                    p.sendMessage(LoadLang.title+LoadLang.errorNotEnough);
                    return;
                }
            }else{
                if(EconomyAPI.getInstance().myMoney(p)<nowPrice){
                    p.sendMessage(LoadLang.title+LoadLang.errorNotEnough);
                    return;
                }
            }
            //价格判断
            if(addPrice<LoadAuction.autionData.getDouble(key+".Auction.AddPrice")){
                p.sendMessage(LoadLang.title+LoadLang.errorAuctionAddPrice);
                p.showFormWindow(AuctionWindowMath.getAuctionAddPriceWindow(key));
                return;
            }
            //收取现在拍卖者的价格
            if(LoadAuction.autionData.getBoolean(key+".IsPoint")){
                Point.setPoint(p,Point.myPoint(p)-nowPrice);
            }else{
                EconomyAPI.getInstance().setMoney(p,EconomyAPI.getInstance().myMoney(p)-nowPrice);
            }
            //返还上一位的拍卖金额
            if(!"NULL".equalsIgnoreCase(LoadAuction.autionData.getString(key+".Auction.Player"))) {
                String lastAuctionPlayer = LoadAuction.autionData.getString(key + ".Auction.Player");
                if (LoadAuction.autionData.getBoolean(key + ".IsPoint")) {
                    Point.setPoint(lastAuctionPlayer, Point.myPoint(lastAuctionPlayer) + LoadAuction.autionData.getDouble(key + ".Auction.MaxPrice"));
                } else {
                    EconomyAPI.getInstance().setMoney(lastAuctionPlayer, EconomyAPI.getInstance().myMoney(lastAuctionPlayer) + LoadAuction.autionData.getDouble(key + ".Auction.MaxPrice"));
                }
            }
            //保存文件
            LoadAuction.autionData.set(key+".Auction.MaxPrice",nowPrice);
            LoadAuction.autionData.set(key+".Auction.Player",p.getName());
            LoadAuction.autionData.set(key+".Auction.Count",LoadAuction.autionData.getInt(key+".Auction.Count")+1);
            LoadAuction.autionData.save();
            p.sendMessage(LoadLang.title+LoadLang.auctionAddPrice.replaceAll("<price>",nowPrice+""));
        }catch (Exception ex){
            p.sendMessage(LoadLang.title+LoadLang.errorAuctionPrice);
        }
    }

    /**
     * 上架拍卖品
     * @param e 事件
     */
    @EventHandler
    public void onClickAuctionGoodsWindow(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return;}
        if (e.getResponse() == null){ return;}
        if(!(e.getWindow() instanceof FormWindowCustom)||e.wasClosed()){ return;}
        FormWindowCustom window = (FormWindowCustom) e.getWindow();
        if(!window.getTitle().equalsIgnoreCase(LoadCfg.auctionGoodsTitle)){ return;}
        Player p=e.getPlayer();int hasAuction=0;
        for(String key:LoadAuction.autionData.getKeys(false)){
            if(LoadAuction.autionData.getString(key+".Player").equalsIgnoreCase(p.getName())){
                hasAuction++;
            }
        }
        if(hasAuction>=LoadCfg.auctionMax){
            p.sendMessage(LoadLang.title+LoadLang.errorAuctionMax);
            return;
        }
        try{
            double minPrice=Double.parseDouble(window.getResponse().getInputResponse(0));
            double addPrice=Double.parseDouble(window.getResponse().getInputResponse(1));
            boolean isPoint=false;
            if(LoadCfg.usePoint){
                isPoint=window.getResponse().getToggleResponse(2);
            }
            int count=(int)window.getResponse().getSliderResponse(3);
            String randomId= RandomId.getRandomAuctionId();
            if(LoadAuction.autionData.get(randomId)!=null){
                randomId=RandomId.getRandomAuctionId();
            }
            if(count>p.getInventory().getItemInHand().getCount()){
                p.sendMessage(LoadLang.title+LoadLang.errorGoodNotEnough);
                return;
            }
            Item item=p.getInventory().getItemInHand();
            //收取物品
            if(count==item.getCount()){
                p.getInventory().setItemInHand(new Item(0));
            }else{
                item.setCount(item.getCount()-count);
                p.getInventory().setItemInHand(item);
            }
            item.setCount(count);
            //文件保存
            LoadAuction.autionData.set(randomId+".Player",p.getName());
            LoadAuction.autionData.set(randomId+".MinPrice",minPrice);
            LoadAuction.autionData.set(randomId+".Date",df.format(new Date()));
            LoadAuction.autionData.set(randomId+".Item", ItemNBT.getItemNbt(item));
            LoadAuction.autionData.set(randomId+".IsPoint",isPoint);
            LoadAuction.autionData.set(randomId+".Auction.AddPrice",addPrice);
            LoadAuction.autionData.set(randomId+".Auction.Player","NULL");
            LoadAuction.autionData.set(randomId+".Auction.MaxPrice",minPrice);
            LoadAuction.autionData.set(randomId+".Auction.Count",0);
            LoadAuction.autionData.save();
            for(Map.Entry<UUID, Player> ps:gsn.getPlugin().getServer().getOnlinePlayers().entrySet()){
                ps.getValue().sendMessage(LoadLang.title+LoadLang.auctionBcMsg.replaceAll("<player>",p.getName()));
            }
        }catch (Exception ex){
            p.sendMessage(LoadLang.title+LoadLang.errorAuctionPrice);
        }
    }
}
