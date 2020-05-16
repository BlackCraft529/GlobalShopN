package com.bc.Utils.load;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.bc.gsn;
import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/16 15:00
 */
public class LoadMails {
    public static Config globalMails;
    public static void loadMails(){
        if(!new File(gsn.getPlugin().getDataFolder(), "mails.yml").exists()) {
            gsn.getPlugin().getLogger().info(TextFormat.BLUE+"未找到mails.yml，正在创建...");
            gsn.getPlugin().saveResource("mails.yml");
            globalMails =new Config(new File(gsn.getPlugin().getDataFolder(), "mails.yml"), Config.YAML);
            gsn.getPlugin().getLogger().info(TextFormat.GREEN+"mails.yml创建完成!");
        }else {
            globalMails = new Config(new File(gsn.getPlugin().getDataFolder(), "mails.yml"), Config.YAML);
            gsn.getPlugin().getLogger().info(TextFormat.YELLOW+"mails.yml已找到!");
        }
        gsn.getPlugin().getLogger().info(TextFormat.GREEN+"mails.yml加载完成!");
    }
}
