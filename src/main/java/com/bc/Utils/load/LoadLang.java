package com.bc.Utils.load;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.bc.gsn;

import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 15:33
 */
public class LoadLang {
    public static Config lang;
    public static String title="";
    public static String errorSell="",errorCharge="",errorWithdraw="",errorDeposit="",errorNotEnough="",
            errorGoodNotExists="",errorGoodNotEnough="",errorIsBlack="",errorIsMaxSell="",errorIsAir="",errorPlayerNotExists="",errorItemNotEnough="";
    public static String noticeCharge="",noticeSellSucc="",withdrawMoney="",withdrawPoint="",depositMoney="",depositPoint="",noticeBuySucc="";
    public static String bankAMoney="",bankAPoint="";
    public static String mailNotExists="",mailGetSucc="",mailBack="",mailSend="",mailHas="";
    public static void loadLang(){
        if(!new File(gsn.getPlugin().getDataFolder(), "lang.yml").exists()) {
            gsn.getPlugin().getLogger().info(TextFormat.BLUE+"未找到lang.yml，正在创建...");
            gsn.getPlugin().saveResource("lang.yml");
            lang=new Config(new File(gsn.getPlugin().getDataFolder(), "lang.yml"), Config.YAML);
            gsn.getPlugin().getLogger().info(TextFormat.GREEN+"lang.yml创建完成!");
        }else {
            lang = new Config(new File(gsn.getPlugin().getDataFolder(), "lang.yml"), Config.YAML);
            gsn.getPlugin().getLogger().info(TextFormat.YELLOW+"lang.yml已找到!");
        }
        reLoadLang();
        gsn.getPlugin().getLogger().info(TextFormat.GREEN+"lang.yml加载完成!");
    }
    public static void reLoadLang() {
        title=lang.getString("Title").replaceAll("&","§");;
        errorSell=lang.getString("Error.Sell").replaceAll("&","§");
        errorCharge=lang.getString("Error.Charge").replaceAll("&","§");
        errorWithdraw=lang.getString("Error.Withdraw").replaceAll("&","§");
        errorDeposit=lang.getString("Error.Deposit").replaceAll("&","§");
        errorNotEnough=lang.getString("Error.NotEnough").replaceAll("&","§");
        errorGoodNotExists=lang.getString("Error.GoodNotExists").replaceAll("&","§");
        errorGoodNotEnough=lang.getString("Error.GoodNotEnough").replaceAll("&","§");
        errorIsBlack=lang.getString("Error.IsBlack").replaceAll("&","§");
        errorIsMaxSell=lang.getString("Error.IsMaxSell").replaceAll("&","§");
        errorIsAir=lang.getString("Error.IsAir").replaceAll("&","§");
        errorPlayerNotExists=lang.getString("Error.PlayerNotExists").replaceAll("&","§");
        errorItemNotEnough=lang.getString("Error.ItemNotEnough").replaceAll("&","§");
        mailNotExists=lang.getString("Error.MailNotExists").replaceAll("&","§");

        noticeCharge=lang.getString("Notice.Charge").replaceAll("&","§");
        noticeSellSucc=lang.getString("Notice.SellSucc").replaceAll("&","§");
        noticeBuySucc=lang.getString("Notice.Buy.Succ").replaceAll("&","§");

        withdrawMoney=lang.getString("Notice.Withdraw.Money").replaceAll("&","§");
        withdrawPoint=lang.getString("Notice.Withdraw.Point").replaceAll("&","§");
        depositMoney=lang.getString("Notice.Deposit.Money").replaceAll("&","§");
        depositPoint=lang.getString("Notice.Deposit.Point").replaceAll("&","§");

        bankAMoney=lang.getString("Notice.Bank.Accrual.Money").replaceAll("&","§");
        bankAPoint=lang.getString("Notice.Bank.Accrual.Point").replaceAll("&","§");

        mailGetSucc=lang.getString("Notice.Mail.Get").replaceAll("&","§");
        mailBack=lang.getString("Notice.Mail.Back").replaceAll("&","§");
        mailSend=lang.getString("Notice.Mail.Send").replaceAll("&","§");
        mailHas=lang.getString("Notice.Mail.Has").replaceAll("&","§");
    }
}
