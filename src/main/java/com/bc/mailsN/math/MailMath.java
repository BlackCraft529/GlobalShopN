package com.bc.mailsN.math;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.RandomId;
import com.bc.Utils.load.LoadMails;
import com.bc.gsn;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Luckily_Baby
 * @date 2020/5/6 18:01
 */
public class MailMath {
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 发送一封邮件
     * @param sender 发送者
     * @param receiver 接受者
     * @param item 物品
     * @param msg 寄语
     * @return 是否成功
     */
    public static boolean sendMail(Player sender, Player receiver, Item item,String msg){
        String randomId= RandomId.getRandomMailId();
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",receiver.getName()+".yml");
        if(!pfi.exists()) {
            return false;
        }
        Config pf=new Config(pfi,Config.YAML);
        while(pf.get(randomId)!=null){
            randomId=RandomId.getRandomMailId();
        }
        pf.set(randomId+".Sender",sender.getName());
        pf.set(randomId+".Date",df.format(new Date()));
        if(item!=null) {
            pf.set(randomId+".Item", ItemNBT.getItemNbt(item));
        }else{
            pf.set(randomId+".Item","null");
        }
        pf.set(randomId+".Msg",msg);
        pf.save();
        return true;
    }
    /**
     * 发送一封邮件
     * @param sender 发送者-控制台
     * @param receiver 接受者
     * @param item 物品
     * @param msg 寄语
     * @return 是否成功
     */
    public static boolean sendMail(CommandSender sender, String receiver, Item item, String msg){
        String randomId= RandomId.getRandomMailId();
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",receiver+".yml");
        if(!pfi.exists()) {
            return false;
        }
        Config pf=new Config(pfi,Config.YAML);
        while(pf.get(randomId)!=null){
            randomId=RandomId.getRandomMailId();
        }
        pf.set(randomId+".Sender",sender.getName());
        pf.set(randomId+".Date",df.format(new Date()));
        if(item!=null) {
            pf.set(randomId+".Item", ItemNBT.getItemNbt(item));
        }else{
            pf.set(randomId+".Item","null");
        }
        pf.set(randomId+".Msg",msg);
        pf.save();
        return true;
    }

    /**
     * 发送一封全服邮件
     * @param sender 发送者
     * @param item 包含的物品
     * @param msg 信息
     * @param autoLook 是否自动弹出
     * @return 是否成功发送
     */
    public static boolean sendGlobalMail(Player sender,Item item,String msg,boolean autoLook,double money,double point,String cmd){
        String randomId= RandomId.getRandomMailId();
        while(LoadMails.globalMails.get(randomId)!=null){
            randomId= RandomId.getRandomMailId();
        }
        LoadMails.globalMails.set(randomId+".Sender",sender.getName());
        LoadMails.globalMails.set(randomId+".Item",ItemNBT.getItemNbt(item));
        LoadMails.globalMails.set(randomId+".Msg",msg);
        LoadMails.globalMails.set(randomId+".Money",money);
        LoadMails.globalMails.set(randomId+".Point",point);
        LoadMails.globalMails.set(randomId+".AutoLook",autoLook);
        LoadMails.globalMails.set(randomId+".Date",df.format(new Date()));
        LoadMails.globalMails.set(randomId+".Cmd",cmd);
        LoadMails.globalMails.save();
        return true;
    }
}
