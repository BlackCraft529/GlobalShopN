package com.bc.Utils.load;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.bc.gsn;
import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 15:32
 */
public class LoadCfg {
    public static final String mailGlobalInfoTitle="§4系统邮件详情",mailGlobalInfoBt="§a已阅§9(§b领取§9)",mailGlobalSendMenuTitle="§6管理员: 全局邮件发送";

    public static Config cfg;
    public static Boolean usePoint=true;
    public static String mailsAutoLook="";
    public static String menuTitle="",menuText="",menuBtOpen="",menuBtMail="",menuBtBank="",menuBtSell="",menuBtSendMail ="";
    public static String bankTitle="",bankText="",bankBtDeposit="", bankBtWithdraw ="",bankOpDeposit="",bankOpWithdraw="", bankDsIpMoney ="", bankDsIpPoint ="",
                    bankDsLbMoney="",bankDsLbPoint="",bankWdIpMoney="",bankWdIpPoint="",bankWdLbMoney="",bankWdLbPoint="";
    public static String shopTitle="",shopText="",shopBtText="";
    public static String buyTitle="",buySlider="",buyBtText="",buySureTitle="";
    public static String sellTitle="",sellText="",sellLbItemName="",sellLbItemCount="",sellIpMoney="",sellIpPoint="",sellSlider="";
    public static String mailTitle="",mailText="",mailInfoTitle="",mailInfoBtGet="",mailSendTitle="",mailSendMsg="",mailSendItem="",mailSendCount="",mailSendReceiver="";
    public static String globalMailTitle="",globalMailText="",globalMailBtPersonalText="",globalMailBtGlobalText="",globalMailListMenuTitle="";
    public static float chargeSellMoney=0f,chargeSellPoint=0f,chargeBuyMoney=0f,chargeBuyPoint=0f,aBankMoney=0f,aBankPoint=0f;
    public static int keepDay=7,maxSell=3,mailAutoLookTick=15000,watchDog=15*1000*60;
    public static void loadCfg(){
        if(!new File(gsn.getPlugin().getDataFolder(), "config.yml").exists()) {
            gsn.getPlugin().getLogger().info(TextFormat.BLUE+"未找到config.yml，正在创建...");
            gsn.getPlugin().saveDefaultConfig();
            cfg=new Config(new File(gsn.getPlugin().getDataFolder(), "config.yml"), Config.YAML);
            gsn.getPlugin().getLogger().info(TextFormat.GREEN+"config.yml创建完成!");
        }else {
            cfg = new Config(new File(gsn.getPlugin().getDataFolder(), "config.yml"), Config.YAML);
            gsn.getPlugin().getLogger().info(TextFormat.YELLOW+"config.yml已找到!");
        }
        reLoadCfg();
        gsn.getPlugin().getLogger().info(TextFormat.GREEN+"config.yml加载完成!");
    }
    public static void reLoadCfg(){
        usePoint=cfg.getBoolean("UsePoint");
        keepDay=cfg.getInt("Shop.KeepDay");
        maxSell=cfg.getInt("Shop.MaxSell");
        if(cfg.get("WatchDog")!=null) {
            watchDog = cfg.getInt("WatchDog") * 60 * 1000;
        }
        aBankMoney=(float) cfg.getDouble("Bank.Accrual.Money");
        aBankPoint=(float) cfg.getDouble("Bank.Accrual.Point");

        mailsAutoLook=cfg.getString("Mails.AutoLook").replaceAll("&","§");
        mailAutoLookTick=cfg.getInt("Mail.Wait")*1000;

        menuTitle=cfg.getString("Gui.Menu.Title").replaceAll("&","§");
        menuText=cfg.getString("Gui.Menu.Text").replaceAll("&","§");
        bankTitle=cfg.getString("Gui.Bank.Title").replaceAll("&","§");
        bankText=cfg.getString("Gui.Bank.Text").replaceAll("&","§");

        buyTitle=cfg.getString("Gui.Buy.Title").replaceAll("&","§");
        buyBtText=cfg.getString("Gui.Buy.Button.Text").replaceAll("&","§");
        buySlider=cfg.getString("Gui.Buy.Sure.Slider").replaceAll("&","§");
        buySureTitle=cfg.getString("Gui.Buy.Sure.Title").replaceAll("&","§");

        menuBtOpen=cfg.getString("Gui.Menu.Open.Text").replaceAll("&","§");
        menuBtMail=cfg.getString("Gui.Menu.Mail.Text").replaceAll("&","§");
        menuBtBank=cfg.getString("Gui.Menu.Bank.Text").replaceAll("&","§");
        menuBtSell=cfg.getString("Gui.Menu.Sell.Text").replaceAll("&","§");

        bankBtDeposit=cfg.getString("Gui.Bank.Deposit.Text").replaceAll("&","§");
        bankBtWithdraw =cfg.getString("Gui.Bank.Withdraw.Text").replaceAll("&","§");
        bankOpDeposit=cfg.getString("Gui.Bank.OperateMenu.Deposit.Title").replaceAll("&","§");
        bankOpWithdraw=cfg.getString("Gui.Bank.OperateMenu.Withdraw.Title").replaceAll("&","§");
        bankDsIpMoney =cfg.getString("Gui.Bank.OperateMenu.Deposit.Input.Money").replaceAll("&","§");
        bankDsIpPoint =cfg.getString("Gui.Bank.OperateMenu.Deposit.Input.Point").replaceAll("&","§");
        bankDsLbMoney=cfg.getString("Gui.Bank.OperateMenu.Deposit.Label.Money").replaceAll("&","§");
        bankDsLbPoint=cfg.getString("Gui.Bank.OperateMenu.Deposit.Label.Point").replaceAll("&","§");
        bankWdIpMoney=cfg.getString("Gui.Bank.OperateMenu.Withdraw.Input.Money").replaceAll("&","§");
        bankWdIpPoint=cfg.getString("Gui.Bank.OperateMenu.Withdraw.Input.Point").replaceAll("&","§");
        bankWdLbMoney=cfg.getString("Gui.Bank.OperateMenu.Withdraw.Label.Money").replaceAll("&","§");
        bankWdLbPoint=cfg.getString("Gui.Bank.OperateMenu.Withdraw.Label.Point").replaceAll("&","§");

        shopTitle=cfg.getString("Gui.Shop.Title").replaceAll("&","§");
        shopText=cfg.getString("Gui.Shop.Text").replaceAll("&","§");
        shopBtText=cfg.getString("Gui.Shop.ButtonText").replaceAll("&","§");

        sellTitle=cfg.getString("Gui.Sell.Title").replaceAll("&","§");
        sellText=cfg.getString("Gui.Sell.Text").replaceAll("&","§");
        sellLbItemName=cfg.getString("Gui.Sell.Label.Name").replaceAll("&","§");
        sellLbItemCount=cfg.getString("Gui.Sell.Label.Count").replaceAll("&","§");
        sellIpMoney=cfg.getString("Gui.Sell.Input.Money").replaceAll("&","§");
        sellIpPoint=cfg.getString("Gui.Sell.Input.Point").replaceAll("&","§");
        sellSlider=cfg.getString("Gui.Sell.Slider").replaceAll("&","§");

        chargeBuyMoney=(float)cfg.getDouble("Shop.Charge.Money.Buy")/100;
        chargeBuyPoint=(float)cfg.getDouble("Shop.Charge.Point.Buy")/100;
        chargeSellMoney=(float)cfg.getDouble("Shop.Charge.Money.Sell")/100;
        chargeSellPoint=(float)cfg.getDouble("Shop.Charge.Point.Sell")/100;

        mailTitle=cfg.getString("Gui.Mail.Title").replaceAll("&","§");
        mailText=cfg.getString("Gui.Mail.Text").replaceAll("&","§");
        mailInfoTitle=cfg.getString("Gui.Mail.Info.Title").replaceAll("&","§");
        mailInfoBtGet=cfg.getString("Gui.Mail.Info.Button.Text").replaceAll("&","§");
        mailSendTitle=cfg.getString("Gui.Mail.Send.Title").replaceAll("&","§");
        mailSendMsg=cfg.getString("Gui.Mail.Send.Input").replaceAll("&","§");
        mailSendItem=cfg.getString("Gui.Mail.Send.SendItem").replaceAll("&","§");
        mailSendCount=cfg.getString("Gui.Mail.Send.Count").replaceAll("&","§");
        mailSendReceiver=cfg.getString("Gui.Mail.Send.Receiver").replaceAll("&","§");

        globalMailTitle=cfg.getString("Gui.GlobalMail.Title").replaceAll("&","§");
        globalMailText=cfg.getString("Gui.GlobalMail.Text").replaceAll("&","§");
        globalMailBtPersonalText=cfg.getString("Gui.GlobalMail.Personal.Text").replaceAll("&","§");
        globalMailBtGlobalText=cfg.getString("Gui.GlobalMail.Global.Text").replaceAll("&","§");
        globalMailListMenuTitle=cfg.getString("Gui.GlobalMail.Mails.Title").replaceAll("&","§");

        menuBtSendMail =cfg.getString("Gui.Menu.SendMail.Text").replaceAll("&","§");
    }
}
