package com.bc;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.bc.Utils.load.*;
import com.bc.auctionN.listener.AuctionListener;
import com.bc.bankN.listener.BankListener;
import com.bc.globalshopN.listener.*;
import com.bc.globalshopN.math.MenuWindowMath;
import com.bc.mailsN.listener.MailListener;
import com.bc.mailsN.math.MailWindowMath;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 14:37
 */
public class gsn extends PluginBase {
    private static gsn plugin;
    public static String ver="0.2.34";
    public static gsn getPlugin(){return plugin;}

    /**
     * 关服
     */
    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.GREEN + "开始卸载全球商店[GlobalShopN]...");

        com.bc.globalshopN.clock.WatchDog.sigleThreadPool.shutdown();
        com.bc.auctionN.clock.WatchDog.sigleThreadPool.shutdown();
        MenuListener.sigleThreadPool.shutdown();
        this.getLogger().info(TextFormat.GREEN+"线程池关闭...");
    }

    /**
     * 开服
     */
    @Override
    public void onEnable() {
        plugin = this;
        this.getLogger().info(TextFormat.YELLOW + "[GlobalShopN]:");
        this.getLogger().info(TextFormat.GREEN + "尝试加载全球插件!");
        //加载监听器
        this.getServer().getPluginManager().registerEvents(new com.bc.globalshopN.listener.PlayerListener(),this);
        this.getServer().getPluginManager().registerEvents(new com.bc.mailsN.listener.PlayerListener(),this);
        this.getServer().getPluginManager().registerEvents(new AuctionListener(),this);
        this.getServer().getPluginManager().registerEvents(new MenuListener(),this);
        this.getServer().getPluginManager().registerEvents(new BankListener(),this);
        this.getServer().getPluginManager().registerEvents(new SellListener(),this);
        this.getServer().getPluginManager().registerEvents(new ShopListener(),this);
        this.getServer().getPluginManager().registerEvents(new MailListener(),this);
        this.getLogger().info(TextFormat.GREEN+"监听器注册完毕!");

        //加载配置项
        LoadCfg.loadCfg();
        LoadLang.loadLang();
        LoadData.loadData();
        LoadMails.loadMails();
        LoadAuction.loadAuctionData();

        //启动线程
        com.bc.globalshopN.clock.WatchDog.onWatch();
        com.bc.auctionN.clock.WatchDog.onWatch();
        this.getLogger().info(TextFormat.GREEN+"检查线程已启动!");
        this.getLogger().info(TextFormat.GREEN+"Version: "+TextFormat.RED+ver);
        this.getLogger().info(TextFormat.GOLD+"欢迎使用N系列11:"+TextFormat.RED+"["+TextFormat.BLUE+"GlobalShopN"+TextFormat.RED+"]");
        this.getLogger().info(TextFormat.GREEN + "BUG反馈请联系: QQ 1990588801.");
        this.getLogger().info(TextFormat.GREEN + "帮助您更快的修复错误~");
    }

    /**
     * 执行指令
     * @param sender 执行者
     * @param command 指令头
     * @param label null
     * @param args 指令集
     * @return 是否成功执行
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("gsn")) {
            return true;
        }
        //获取帮助
        if (args.length == 0) {
            for(String s: LoadHelp.getHelp(sender.isOp())){
                sender.sendMessage(s);
            }
            return true;
        }
        //open sell mail
        if(args.length==1){
            //管理员全局邮箱
            if(args[0].equalsIgnoreCase("adminMail")&&sender.hasPermission("gsn.adminMail")){
                if(sender instanceof Player){
                    ((Player)sender).showFormWindow(MailWindowMath.getGlobalSendMailWindow((Player)sender));
                    return true;
                }else {
                    sender.sendMessage("控制台禁止使用该指令...");
                    return true;
                }
            }
            //开启主页面
            if(args[0].equalsIgnoreCase("open")&&sender.hasPermission("gsn.use")){
                if(sender instanceof Player){
                    ((Player)sender).showFormWindow(MenuWindowMath.getMenu());
                    return true;
                }else{
                    sender.sendMessage("控制台禁止使用该指令...");
                    return true;
                }
            }
            //重载插件
            if(args[0].equalsIgnoreCase("reload")&&sender.hasPermission("gsn.admin")){
                //加载配置项
                LoadCfg.loadCfg();
                LoadLang.loadLang();
                LoadData.loadData();
                LoadMails.loadMails();
                LoadAuction.loadAuctionData();
                sender.sendMessage(LoadLang.title+"§a重载完成！");
                return true;
            }
        }
        return true;
    }
}
