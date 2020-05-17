package com.bc.mailsN.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadLang;
import com.bc.Utils.load.LoadMails;
import com.bc.gsn;
import com.bc.mailsN.math.MailWindowMath;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Luckily_Baby
 * @date 2020/5/16 14:44
 */
public class PlayerListener implements Listener {
    private static ThreadFactory threadFactory=new ThreadFactoryBuilder().setNameFormat("my-pool-%d").build();
    public static ExecutorService sigleThreadPool = new ThreadPoolExecutor(1,1,0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024),threadFactory,new ThreadPoolExecutor.AbortPolicy());
    /**
     * 创建用户邮箱文件
     * @param e 玩家加入事件
     */
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
                Config pfs=new Config(pf,Config.YAML);
                pfs.set("Global",new ArrayList<String>());
                pfs.save();
            } catch (IOException es) {
                es.printStackTrace();
                gsn.getPlugin().getLogger().info(TextFormat.RED + "[GlobalShopN] 插件内部错误: ERROR-301");
            }
        }else{
            Config pfi=new Config(pf,Config.YAML);
            if("newPlayer".equals(pfi.get("Global"))){
                pfi.set("Global",new ArrayList<String>());
                pfi.save();
            }
            int mailCount=pfi.getKeys(false).size();
            int globalCount=LoadMails.globalMails.getKeys(false).size()-pfi.getStringList("Global").size();
            int count=(mailCount-1)+globalCount;
            if(count>0){
                p.sendMessage(LoadLang.title+LoadLang.mailHas.replaceAll("<count>",(count)+""));
            }
        }
    }

    /**
     * 检查玩家是否有未读邮件，并弹出
     * @param e
     */
    @EventHandler
    public void onPlayerJoinGameEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        File pf=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        if(!pf.exists()) {
            return;
        }
        Config pfs=new Config(pf,Config.YAML);
        List<String> hasLookedMails=pfs.getStringList("Global");
        for(String key: LoadMails.globalMails.getKeys(false)){
            if(!hasLookedMails.contains(key)){
                if(LoadMails.globalMails.getBoolean(key+".AutoLook")){
                    //休眠 xxS 后弹出界面
                    sigleThreadPool.execute(() -> {
                        try {
                            Thread.sleep(LoadCfg.mailAutoLookTick);
                            p.showFormWindow(MailWindowMath.getGlobalMailListWindow(p));
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    });
                    break;
                }
            }
        }
    }
}
