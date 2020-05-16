package com.bc.mailsN.listener;

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
import cn.nukkit.utils.Config;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.load.LoadMails;
import com.bc.gsn;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadLang;
import com.bc.mailsN.math.MailMath;
import com.bc.mailsN.math.MailWindowMath;
import me.onebone.economyapi.EconomyAPI;
import net.player.api.Point;

import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * @author Luckily_Baby
 * @date 2020/5/6 17:47
 */
public class MailListener implements Listener {
    /**
     * 发送邮件
     * @param e 界面点击相应事件
     */
    @EventHandler
    public void onClickSendMailMenu(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return;}
        if (e.getResponse() == null){ return;}
        if(!(e.getWindow() instanceof FormWindowCustom)||e.wasClosed()){ return;}
        FormWindowCustom window = (FormWindowCustom) e.getWindow();
        if(!window.getTitle().equalsIgnoreCase(LoadCfg.mailSendTitle)){ return;}
        Player p=e.getPlayer();
        String msg=window.getResponse().getInputResponse(0);
        Player receiver= null;
        //System.out.println("触发");
        if(gsn.getPlugin().getServer().getPlayer(window.getResponse().getInputResponse(3))!=null){
            receiver=gsn.getPlugin().getServer().getPlayer(window.getResponse().getInputResponse(3));
        }
        if(receiver==null){
            p.sendMessage(LoadLang.title+LoadLang.errorPlayerNotExists);
            p.showFormWindow(MailWindowMath.getSendMailMenu(p));
            return;
        }
        boolean isSendItem=window.getResponse().getToggleResponse(1);
        int count=0;
        if(isSendItem){
            count=(int)window.getResponse().getSliderResponse(2);
        }
        if(p.getInventory().getItemInHand().getCount()>=count&&isSendItem&&count>0){
            Item item=p.getInventory().getItemInHand();
            //收取物品
            if(count==p.getInventory().getItemInHand().getCount()){
                p.getInventory().setItemInHand(new Item(0));
            }else if(count<p.getInventory().getItemInHand().getCount()){
                item.setCount(item.getCount()-count);
                p.getInventory().setItemInHand(item);
            }
            item.setCount(count);
            if(MailMath.sendMail(p,receiver,item,msg)){
                p.sendMessage(LoadLang.title + LoadLang.mailSend);
            }else{
                p.sendMessage(LoadLang.title+LoadLang.errorPlayerNotExists);
            }
            return;
        }else if(p.getInventory().getItemInHand().getCount()<count&&isSendItem&&count>0){
            p.sendMessage(LoadLang.title+LoadLang.errorItemNotEnough);
            p.showFormWindow(MailWindowMath.getSendMailMenu(p));
            return;
        }
        if(MailMath.sendMail(p,receiver,null,msg)) {
            p.sendMessage(LoadLang.title + LoadLang.mailSend);
        }else{
            p.sendMessage(LoadLang.title+LoadLang.errorPlayerNotExists);
        }
    }
    /**
     * 信件信息界面
     * @param e 用户点击领取
     */
    @EventHandler
    public void onClickGet(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().contains(LoadCfg.mailInfoTitle)){return;}
        Player p=e.getPlayer();
        String mailId=((FormWindowSimple) window).getTitle().split("§8§9§8@")[1];
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        if(!pfi.exists()) {
            p.sendMessage(LoadLang.title+LoadLang.mailNotExists);
            return ;
        }
        Config pf=new Config(pfi,Config.YAML);
        if(pf.get(mailId)!=null){
            Item item= ItemNBT.getItemFromMail(p,mailId);
            pf.remove(mailId);
            pf.save();
            if(item!=null) {
                p.getInventory().addItem(item);
            }
            p.sendMessage(LoadLang.title+LoadLang.mailGetSucc);
            return;
        }else{
            p.sendMessage(LoadLang.title+LoadLang.mailNotExists);
            return ;
        }
    }

    /**
     * 打开邮件详情
     * @param e 点击邮件
     */
    @EventHandler
    public void onClickMailMenu(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().contains(LoadCfg.mailTitle)){return;}
        Player p=e.getPlayer();
        int slot=0;
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        if(!pfi.exists()) {
            p.sendMessage(LoadLang.title+LoadLang.mailNotExists);
            return ;
        }
        Config pf=new Config(pfi,Config.YAML);
        String mailId=((FormResponseSimple)e.getResponse()).getClickedButton().getText().split("\n")[0].split("@§8")[1];
        if(pf.get(mailId)==null){
            p.sendMessage(LoadLang.title+LoadLang.mailNotExists);
            return;
        }
        p.showFormWindow(MailWindowMath.getMailInfo(p,mailId));
    }

    /**
     * 玩家点击全局信箱
     * @param e 信箱相应事件
     */
    @EventHandler
    public void onClickGlobalMailWindow(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().equalsIgnoreCase(LoadCfg.globalMailListMenuTitle)){return;}
        Player p=e.getPlayer();
        String clickKey=((FormResponseSimple)e.getResponse()).getClickedButton().getText().split("\n")[0].split("@§8")[1];
        //获取文件中对应的ID
        for(String key: LoadMails.globalMails.getKeys(false)){
            if(key.equalsIgnoreCase(clickKey)){
                p.showFormWindow(MailWindowMath.getGlobalMailInfoWindow(key));
                return;
            }
        }
    }

    /**
     * 用户点击全局邮件详细信息界面
     * @param e 事件相应事件
     */
    @EventHandler
    public void onClickGlobalMailInfoWindow(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().contains(LoadCfg.mailGlobalInfoTitle)){return;}
        Player p=e.getPlayer();
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        if(!pfi.exists()) {
            p.sendMessage(LoadLang.title+LoadLang.mailNotExists);
            return ;
        }
        Config pf=new Config(pfi,Config.YAML);
        String key=((FormWindowSimple) window).getTitle().split("@§8")[1];
        Item item=ItemNBT.getItemFromGlobalMail(key);
        double money=LoadMails.globalMails.getDouble(key+".Money");
        EconomyAPI.getInstance().setMoney(p,EconomyAPI.getInstance().myMoney(p)+money);
        double point=0;
        if(LoadCfg.usePoint){
            point=LoadMails.globalMails.getDouble(key+".Point");
            Point.setPoint(p,Point.myPoint(p)+point);
        }
        if(item!=null){
            p.getInventory().addItem(item);
        }
        p.sendMessage(LoadLang.title+"§a已领取 §e金币:"+money+" §b点券:"+point);
        List<String> globalMails=pf.getStringList("Global");
        globalMails.add(key);
        pf.set("Global",globalMails);
        pf.save();
        p.sendMessage(LoadLang.title+LoadLang.mailGetSucc);
    }

    /**
     * 用户点击邮件主页
     * @param e 窗体相应事件
     */
    @EventHandler
    public void onClickMailsMenu(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().equals(LoadCfg.globalMailTitle)){return;}
        Player p=e.getPlayer();
        //个人邮箱
        if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.globalMailBtPersonalText)) {
            p.showFormWindow(MailWindowMath.getMailMenu(p));
        //打开全局邮箱↓
        }else if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.globalMailBtGlobalText)){
            p.showFormWindow(MailWindowMath.getGlobalMailListWindow(p));
        }
    }

    /**
     * 用户发送全局邮件界面
     * @param e 窗体相应事件
     */
    @EventHandler
    public void onClickSendGlobalMailWindow(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return;}
        if (e.getResponse() == null){ return;}
        if(!(e.getWindow() instanceof FormWindowCustom)||e.wasClosed()){ return;}
        FormWindowCustom window = (FormWindowCustom) e.getWindow();
        if(!window.getTitle().equalsIgnoreCase(LoadCfg.mailGlobalSendMenuTitle)){ return;}
        Player p=e.getPlayer();
        try {
            String msg=window.getResponse().getInputResponse(0);
            boolean isSendItem=window.getResponse().getToggleResponse(1);
            if(isSendItem&&p.getInventory().getItemInHand()==null){
                p.sendMessage(LoadLang.title+LoadLang.errorIsAir);
                p.showFormWindow(MailWindowMath.getGlobalSendMailWindow(p));
                return;
            }
            Item item=p.getInventory().getItemInHand();
            int count=0;
            if(isSendItem) {
                count = (int) window.getResponse().getSliderResponse(3);
            }
            boolean isAutoLook=window.getResponse().getToggleResponse(2);
            double money = Double.parseDouble(window.getResponse().getInputResponse(4));
            double point=0;
            if(LoadCfg.usePoint){
                point=Double.parseDouble(window.getResponse().getInputResponse(5));
            }
            item.setCount(count);
            if(MailMath.sendGlobalMail(p,item,msg,isAutoLook,money,point)){
                p.sendMessage(LoadLang.title+LoadLang.mailSend);
            }else{
                p.sendMessage(LoadLang.title+"§4插件内部错误: 501");
            }
        }catch (Exception ex){
            p.sendMessage(LoadLang.title+"§6请输入正确的数字！");
            p.showFormWindow(MailWindowMath.getGlobalSendMailWindow(p));
            System.out.println(ex.getMessage());
        }
    }
}
