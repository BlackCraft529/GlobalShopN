package com.bc.mailsN.math;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import com.bc.Utils.Image;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.load.LoadMails;
import com.bc.gsn;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadLang;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luckily_Baby
 * @date 2020/5/6 17:22
 */
public class MailWindowMath {
    /**
     * 玩家发送一封邮件 - 个人邮件
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
     * 获取玩家的邮件界面 - 异步调用 - 个人邮件
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
            if(key.equalsIgnoreCase("Global")){
                continue;
            }
            String sender=pf.getString(key+".Sender");
            String date=pf.getString(key+".Date");
            wd.addButton(new ElementButton("§a"+sender+" @§8"+key+"\n§e"+date, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Mail.Image"))));
        }
        return wd;
    }

    /**
     * 获取用户的邮件信息 - 个人邮件
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

    /**
     * 获取邮箱菜单
     * @return 邮箱菜单
     */
    public static FormWindowSimple getMailMenu(){
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.globalMailTitle,LoadCfg.globalMailText);
        wd.addButton(new ElementButton(LoadCfg.globalMailBtPersonalText,Image.getImageByCfg(LoadCfg.cfg.getString("Gui.GlobalMail.Personal.Image"))));
        wd.addButton(new ElementButton(LoadCfg.globalMailBtGlobalText,Image.getImageByCfg(LoadCfg.cfg.getString("Gui.GlobalMail.Global.Image"))));
        return wd;
    }

    /**
     * 获取全局邮件信箱 -异步调用
     * @param p 玩家
     * @return 全局信箱界面
     */
    public static FormWindowSimple getGlobalMailListWindow(Player p){
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        if(!pfi.exists()) {
            p.sendMessage(LoadLang.title+"§4插件内部错误:302");
            return null;
        }
        Config pf=new Config(pfi,Config.YAML);
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.globalMailListMenuTitle,"");
        List<String> hasLookMails=pf.getStringList("Global");
        for(String key:LoadMails.globalMails.getKeys(false)){
            if(!hasLookMails.contains(key)){
                String sender=LoadMails.globalMails.getString(key+".Sender");
                sender=((LoadMails.globalMails.getBoolean(key+".AutoLook")?LoadCfg.mailsAutoLook:"")+sender);
                sender+=" @§8"+key;
                String date=LoadMails.globalMails.getString(key+".Date");
                wd.addButton(new ElementButton("§a"+sender+"\n§e"+date,Image.getImageByCfg(LoadCfg.cfg.getString("Gui.GlobalMail.Mails.Image"))));
            }
        }
        return wd;
    }

    /**
     * 获取全局邮箱详细信息
     * @param key 邮件ID
     * @return 邮件信息
     */
    public static FormWindowSimple getGlobalMailInfoWindow(String key){
        List<String> desc=new ArrayList<>();
        desc.add("§a发件人: §e"+LoadMails.globalMails.getString(key+".Sender"));
        desc.add("§a发件日期: §e"+LoadMails.globalMails.getString(key+".Date"));
        desc.add("§a信件寄语: ");
        String[] lores=LoadMails.globalMails.getString(key+".Msg").split("\\\\n");
        for(String s:lores){
            desc.add(s.replaceAll("&","§"));
        }
        String info="";
        for(String s:desc){
            info+=s+"\n";
        }
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.mailGlobalInfoTitle+"@§8"+key,info);
        wd.addButton(new ElementButton(LoadCfg.mailGlobalInfoBt));
        return wd;
    }

    /**
     * 获取全局邮件发送界面
     * @param p 请求用户
     * @return 界面
     */
    public static FormWindowCustom getGlobalSendMailWindow(Player p){
        FormWindowCustom wd=new FormWindowCustom(LoadCfg.mailGlobalSendMenuTitle);
        Item item=p.getInventory().getItemInHand();
        int maxCount=item.equals(new Item(0))?0:64;
        ElementInput msgInput=new ElementInput("§a寄语 ");
        ElementToggle sendItem=new ElementToggle("§a是否发送手中物品（全服）");
        ElementSlider itemCount=new ElementSlider("§a请选择数量",1,maxCount,1,1);
        ElementToggle autoLook=new ElementToggle("§b是否必读");
        ElementInput moneyInput=new ElementInput("§a金币");
        ElementInput pointInput=new ElementInput("§b点券");
        ElementInput cmdInput=new ElementInput("§6指令 玩家变量:<player>");

        wd.addElement(msgInput);
        wd.addElement(sendItem);
        wd.addElement(autoLook);
        wd.addElement(itemCount);
        wd.addElement(moneyInput);
        wd.addElement(pointInput);
        wd.addElement(cmdInput);

        return wd;
    }
}
