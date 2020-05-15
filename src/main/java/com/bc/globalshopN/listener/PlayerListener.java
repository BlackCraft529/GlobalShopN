package com.bc.globalshopN.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.bc.globalshopN.Utils.PlayerFile;
import com.bc.globalshopN.gsn;
import com.bc.globalshopN.load.LoadCfg;
import com.bc.globalshopN.load.LoadLang;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 16:44
 */
public class PlayerListener implements Listener {
    public SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat dfs = new DecimalFormat("#.00");
    @EventHandler
    public void onPlayerJoins(PlayerJoinEvent e){
        Player p = e.getPlayer();
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        if(!pf.exists()) {
            File fileParents = pf.getParentFile();
            //新建文件夹
            if (!fileParents.exists()) {
                fileParents.mkdirs();
            }
            try {
                pf.createNewFile();
            } catch (IOException es) {
                es.printStackTrace();
                gsn.getPlugin().getLogger().info(TextFormat.RED + "[GlobalShopN] 插件内部错误: ERROR-301");
            }
        }else{
            Config pfi=new Config(pf,Config.YAML);
            int count=pfi.getKeys(false).size();
            if(count>0){
                p.sendMessage(LoadLang.title+LoadLang.mailHas.replaceAll("<count>",count+""));
            }
        }
    }
    /**
     * 为新玩家创建数据文件&发放日常利息
     * @param e 玩家登录
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players",p.getName()+".yml");
        //用户数据不存在-创建
        if(!pf.exists()) {
            File fileParents = pf.getParentFile();
            //新建文件夹
            if (!fileParents.exists()) { fileParents.mkdirs(); }
            try {
                pf.createNewFile();
                Config pfi = new Config(pf, Config.YAML);
                pfi.set("VipLevel",0);
                pfi.set("Bank.Money",0);
                pfi.set("Bank.Point",0);
                pfi.set("Bank.AccrualDate",df.format(new Date()));
                pfi.set("Sell.Count",0);
                pfi.set("Sell.Money",0);
                pfi.set("Sell.Point",0);
                pfi.set("Buy.Count",0);
                pfi.set("Buy.Money",0);
                pfi.set("Buy.Point",0);
                pfi.save();
            } catch (IOException ex) {
                ex.printStackTrace();
                gsn.getPlugin().getLogger().info(TextFormat.RED + "[GlobalShopN] 插件内部错误: ERROR-101");
            }
        }else{
            SimpleDateFormat dfi = new SimpleDateFormat("yyyy-MM-dd");
            //发放利息
            if(!PlayerFile.getPlayerLastLoginDate(p).equalsIgnoreCase(dfi.format(new Date()))) {
                if (LoadCfg.usePoint) {
                    double ap = PlayerFile.getPlayerBankPoint(p) * (LoadCfg.aBankPoint / 100.0);
                    PlayerFile.setPlayerBanlPoint(p, PlayerFile.getPlayerBankPoint(p) + ap);
                    if(ap<1){
                        p.sendMessage(LoadLang.title + LoadLang.bankAPoint.replaceAll("<point>", "0"));
                    }else {
                        p.sendMessage(LoadLang.title + LoadLang.bankAPoint.replaceAll("<point>", dfs.format(ap)));
                    }
                }
                double am = PlayerFile.getPlayerBankMoney(p) * (LoadCfg.aBankMoney / 100.0);
                PlayerFile.setPlayerBanlMoney(p, PlayerFile.getPlayerBankMoney(p) + am);
                if(am<1){
                    p.sendMessage(LoadLang.title + LoadLang.bankAMoney.replaceAll("<money>", "0"));
                }else {
                    p.sendMessage(LoadLang.title + LoadLang.bankAMoney.replaceAll("<money>", dfs.format(am)));
                }
                PlayerFile.setPlayerLastLoginDate(p);
            }
        }
    }
}
