package com.bc.Utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.bc.gsn;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 19:50
 */
public class PlayerFile {
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static String getPlayerLastLoginDate(Player p){
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players",p.getName()+".yml");
        if(!pf.exists()){
            return null;
        }
        Config pfi = new Config(pf, Config.YAML);
        return pfi.getString("Bank.AccrualDate");
    }
    /**
     * 保存用户登录的最后日期
     * @param p 用户
     * @return 是否保存完成
     */
    public static boolean setPlayerLastLoginDate(Player p){
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players",p.getName()+".yml");
        if(!pf.exists()){
            return false;
        }
        Config pfi = new Config(pf, Config.YAML);
        pfi.set("Bank.AccrualDate",df.format(new Date()));
        pfi.save();
        return true;
    }
    /**
     * 获取玩家商家等级
     * @param p 玩家
     * @return 等级
     */
    public static int getPlayerVipLevel(Player p){
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players",p.getName()+".yml");
        if(!pf.exists()){
            return 0;
        }
        Config pfi = new Config(pf, Config.YAML);
        return pfi.getInt("VipLevel");
    }

    /**
     * 获取用户银行剩余金币
     * @param p 用户
     * @return 余额
     */
    public static double getPlayerBankMoney(Player p){
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players",p.getName()+".yml");
        if(!pf.exists()){
            return -1;
        }
        Config pfi = new Config(pf, Config.YAML);
        return pfi.getInt("Bank.Money");
    }

    /**
     * 设置用户银行余额
     * @param p 用户
     * @param Money 金额
     * @return 是否成功
     */
    public static boolean setPlayerBanlMoney(Player p,double Money){
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players",p.getName()+".yml");
        if(!pf.exists()){
            return false;
        }
        Config pfi = new Config(pf, Config.YAML);
        pfi.set("Bank.Money",Money);
        pfi.save();
        return true;
    }
    /**
     * 获取用户银行剩余金币
     * @param p 用户
     * @return 余额
     */
    public static double getPlayerBankPoint(Player p){
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players",p.getName()+".yml");
        if(!pf.exists()){
            return -1;
        }
        Config pfi = new Config(pf, Config.YAML);
        return pfi.getInt("Bank.Point");
    }

    /**
     * 设置用户银行余额
     * @param p 用户
     * @param Point 金额
     * @return 是否成功
     */
    public static boolean setPlayerBanlPoint(Player p,double Point){
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players",p.getName()+".yml");
        if(!pf.exists()){
            return false;
        }
        Config pfi = new Config(pf, Config.YAML);
        pfi.set("Bank.Point",Point);
        pfi.save();
        return true;
    }
}
