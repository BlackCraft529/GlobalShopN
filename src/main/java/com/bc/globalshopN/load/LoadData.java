package com.bc.globalshopN.load;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.bc.globalshopN.gsn;

import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 16:32
 */
public class LoadData {
    public static Config shopData;
    public static void loadData(){
        if(!new File(gsn.getPlugin().getDataFolder(), "shopdara.yml").exists()) {
            gsn.getPlugin().getLogger().info(TextFormat.BLUE+"未找到shopdara.yml，正在创建...");
            gsn.getPlugin().saveResource("shopdara.yml");
            shopData=new Config(new File(gsn.getPlugin().getDataFolder(), "shopdara.yml"), Config.YAML);
            gsn.getPlugin().getLogger().info(TextFormat.GREEN+"shopdara.yml创建完成!");
        }else {
            shopData = new Config(new File(gsn.getPlugin().getDataFolder(), "shopdara.yml"), Config.YAML);
            gsn.getPlugin().getLogger().info(TextFormat.YELLOW+"shopdara.yml已找到!");
        }
        gsn.getPlugin().getLogger().info(TextFormat.GREEN+"shopdara.yml加载完成!");
    }
}
