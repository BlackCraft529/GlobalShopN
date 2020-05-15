package com.bc.globalshopN.math;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import com.bc.globalshopN.Utils.Image;
import com.bc.globalshopN.Utils.ItemNBT;
import com.bc.globalshopN.gsn;
import com.bc.globalshopN.load.LoadCfg;
import com.bc.globalshopN.load.LoadLang;
import java.io.File;
import java.util.List;

/**
 * @author Luckily_Baby
 * @date 2020/5/6 17:22
 */
public class MailWindowMath {
    /**
     * 玩家发送一封邮件
     * @param p 发送者
     * @return 界面
     */
    public static FormWindowCustom getSendMailMenu(Player p){
        Item item=p.getInventory().getItemInHand();
        int maxCount=item.equals(new Item(0))?0:item.getCount();
        FormWindowCustom wd=new FormWindowCustom(LoadCfg.mailSendTitle);
        ElementInput msgInput=new ElementInput(LoadCfg.mailSendMsg);
        ElementToggle sendItem=new ElementToggle(LoadCfg.mailSendItem);
        ElementSlider itemCount=new ElementSlider(LoadCfg.mailSendCount,1,maxCount,1,1);
        ElementInput receiver=new ElementInput(LoadCfg.mailSendReceiver);

        wd.addElement(msgInput);
        wd.addElement(sendItem);
        wd.addElement(itemCount);
        wd.addElement(receiver);

        return wd;
    }
    /**
     * 获取玩家的邮件界面 - 异步调用
     * @param p 玩家
     * @return 界面
     */
    public static FormWindowSimple getMailMenu(Player p){
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        if(!pfi.exists()) {
            p.sendMessage(LoadLang.title+"§4插件内部错误:302");
            return null;
        }
        Config pf=new Config(pfi,Config.YAML);
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.mailTitle,LoadCfg.mailText);
        for(String key:pf.getKeys(false)){
            String sender=pf.getString(key+".Sender");
            String date=pf.getString(key+".Date");
            wd.addButton(new ElementButton(sender+"\n"+date, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Mail.Image"))));
        }
        return wd;
    }

    /**
     * 获取用户的邮件信息
     * @param p 玩家
     * @param key 邮件ID
     * @return 信息
     */
    public static FormWindowSimple getMailInfo(Player p,String key){
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        if(!pfi.exists()) {
            p.sendMessage(LoadLang.title+"§4插件内部错误:302");
            return null;
        }
        Config pf=new Config(pfi,Config.YAML);
        String info="§4无";
        if(pf.get(key)!=null){
            info="";
            String sender=pf.getString(key+".Sender");
            String date=pf.getString(key+".Date");
            String msg=pf.getString(key+".Msg");
            List<String> lore=LoadCfg.cfg.getStringList("Gui.Mail.Info.Desc");
            Item item=ItemNBT.getItemFromMail(p,key);
            String itemName= item==null?"§a无":(item.hasCustomName()?item.getCustomName():item.getName());
            if(item!=null){
                itemName+=" §ax "+item.getCount();
            }
            for(String s:lore){
                info+=s.replaceAll("<sender>",sender)
                        .replaceAll("<date>",date)
                        .replaceAll("<msg>",msg)
                        .replaceAll("<item>",itemName)
                        .replaceAll("&","§")+"\n";
            }
        }
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.mailInfoTitle+" §8§9§8@"+key,info);
        wd.addButton(new ElementButton(LoadCfg.mailInfoBtGet,Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Mail.Info.Button.Image"))));
        return wd;
    }
}
