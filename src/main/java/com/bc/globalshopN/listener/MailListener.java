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
import cn.nukkit.utils.Config;
import com.bc.globalshopN.Utils.ItemNBT;
import com.bc.globalshopN.gsn;
import com.bc.globalshopN.load.LoadCfg;
import com.bc.globalshopN.load.LoadLang;
import com.bc.globalshopN.math.MailMath;
import com.bc.globalshopN.math.MailWindowMath;

import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/6 17:47
 */
public class MailListener implements Listener {
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
        for(ElementButton bt:((FormWindowSimple) window).getButtons()){
            if(bt.equals(((FormResponseSimple)e.getResponse()).getClickedButton())){
                break;
            }
            slot++;
        }
        String mailId="---1---";
        for(String key: pf.getKeys(false)){
            if(slot<=0){
                mailId=key;
                break;
            }
            slot-=1;
        }
        if(pf.get(mailId)==null){
            p.sendMessage(LoadLang.title+LoadLang.mailNotExists);
            return;
        }
        p.showFormWindow(MailWindowMath.getMailInfo(p,mailId));
    }
}
