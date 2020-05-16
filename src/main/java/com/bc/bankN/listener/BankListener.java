package com.bc.bankN.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import com.bc.Utils.PlayerFile;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadLang;
import com.bc.bankN.math.BankWindowMath;
import me.onebone.economyapi.EconomyAPI;
import net.player.api.Point;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 17:15
 */
public class BankListener implements Listener {
    /**
     * 点击存钱按钮-或者点击取钱按钮
     * @param e 用户点击窗体事件
     */
    @EventHandler
    public void onClickDeposit(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().equals(LoadCfg.bankTitle)){return;}
        Player p=e.getPlayer();
        if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.bankBtDeposit)) {
            p.showFormWindow(BankWindowMath.getDepositWindow(p));
            return;
        }else if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.bankBtWithdraw)){
            p.showFormWindow(BankWindowMath.getWithdrawWindow(p));
            return;
        }
    }
    /**
     * 玩家存入金额
     * @param e 事件
     */
    @EventHandler
    public void onDeposit(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return;}
        if (e.getResponse() == null){ return;}
        if(!(e.getWindow() instanceof FormWindowCustom)||e.wasClosed()){ return;}
        FormWindowCustom window = (FormWindowCustom) e.getWindow();
        if(!window.getTitle().equalsIgnoreCase(LoadCfg.bankOpDeposit)){ return;}
        Player p=e.getPlayer();
        try{
            //存入金币
            double depositMoney=Double.parseDouble(window.getResponse().getInputResponse(1));
            if(EconomyAPI.getInstance().myMoney(p)<depositMoney){
                p.sendMessage(LoadLang.title+LoadLang.errorNotEnough);
                return;
            }
            EconomyAPI.getInstance().setMoney(p,EconomyAPI.getInstance().myMoney(p)-depositMoney);
            if(PlayerFile.setPlayerBanlMoney(p,PlayerFile.getPlayerBankMoney(p)+depositMoney)){
                p.sendMessage(LoadLang.title+LoadLang.depositMoney.replaceAll("<money>",depositMoney+""));
            }else{
                p.sendMessage(LoadLang.title+"§4插件内部错误:104");
                return;
            }
            if(LoadCfg.usePoint){
                double depositPoint=Double.parseDouble(window.getResponse().getInputResponse(3));
                if(Point.myPoint(p)<depositPoint){
                    p.sendMessage(LoadLang.title+LoadLang.errorNotEnough);
                    return;
                }
                Point.setPoint(p,Point.myPoint(p)-depositPoint);
                if(PlayerFile.setPlayerBanlPoint(p,PlayerFile.getPlayerBankPoint(p)+depositPoint)){
                    p.sendMessage(LoadLang.title+LoadLang.depositPoint.replaceAll("<point>",depositPoint+""));
                }else{
                    p.sendMessage(LoadLang.title+"§4插件内部错误:105");
                    return;
                }
            }
        }catch (Exception ex){
            p.sendMessage(LoadLang.title+LoadLang.errorDeposit);
            p.showFormWindow(BankWindowMath.getDepositWindow(p));
            return;
        }
    }
    /**
     * 玩家取出金额
     * @param e 事件
     */
    @EventHandler
    public void onWithdraw(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return;}
        if (e.getResponse() == null){ return;}
        if(!(e.getWindow() instanceof FormWindowCustom)||e.wasClosed()){ return;}
        FormWindowCustom window = (FormWindowCustom) e.getWindow();
        if(!window.getTitle().equalsIgnoreCase(LoadCfg.bankOpWithdraw)){ return;}
        Player p=e.getPlayer();
        try {
            //取出金币
            double withdrawMoney = Double.parseDouble(window.getResponse().getInputResponse(1));
            double bankMoney= PlayerFile.getPlayerBankMoney(p);
            if(withdrawMoney>bankMoney){
                p.sendMessage(LoadLang.title+LoadLang.errorNotEnough);
                return;
            }
            if(PlayerFile.setPlayerBanlMoney(p,bankMoney-withdrawMoney)){
                EconomyAPI.getInstance().addMoney(p,withdrawMoney);
                p.sendMessage(LoadLang.title+LoadLang.withdrawMoney.replaceAll("<money>",withdrawMoney+""));
            }else{
                p.sendMessage(LoadLang.title+"§4插件内部错误:102");
                return;
            }
            //取出点券
            if(LoadCfg.usePoint){
                double withdrawPoint=Double.parseDouble(window.getResponse().getInputResponse(3));
                double bankPoint=PlayerFile.getPlayerBankPoint(p);
                if(bankMoney<withdrawMoney){
                    p.sendMessage(LoadLang.title+LoadLang.errorNotEnough);
                    return;
                }
                if(PlayerFile.setPlayerBanlPoint(p,bankPoint-withdrawPoint)){
                    Point.setPoint(p,Point.myPoint(p)+withdrawPoint);
                    p.sendMessage(LoadLang.title+LoadLang.withdrawPoint.replaceAll("<point>",withdrawPoint+""));
                }else{
                    p.sendMessage(LoadLang.title+"§4插件内部错误:103");
                    return;
                }
            }
        }catch (Exception ex){
            p.sendMessage(LoadLang.title+LoadLang.errorWithdraw);
            p.showFormWindow(BankWindowMath.getWithdrawWindow(p));
            return;
        }
    }
}
