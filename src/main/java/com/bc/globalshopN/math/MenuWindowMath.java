package com.bc.globalshopN.math;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import com.bc.Utils.Image;
import com.bc.Utils.ItemIDSunName;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadData;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 16:24
 */
public class MenuWindowMath {
    /**
     * 获取主页面
     * @return 主页面
     */
    public static FormWindowSimple getMenu(){
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.menuTitle,LoadCfg.menuText);
        wd.addButton(new ElementButton(LoadCfg.menuBtBank, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Menu.Bank.Image"))));
        wd.addButton(new ElementButton(LoadCfg.menuBtOpen, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Menu.Open.Image"))));
        wd.addButton(new ElementButton(LoadCfg.menuBtSell, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Menu.Sell.Image"))));
        wd.addButton(new ElementButton(LoadCfg.menuBtMail, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Menu.Mail.Image"))));
        wd.addButton(new ElementButton(LoadCfg.menuBtSendMail,Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Menu.SendMail.Image"))));
        return wd;
    }

    /**
     * 获取银行界面
     * @return 银行
     */
    public static FormWindowSimple getBankMenu(){
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.bankTitle,LoadCfg.bankText);
        wd.addButton(new ElementButton(LoadCfg.bankBtDeposit, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Bank.Deposit.Image"))));
        wd.addButton(new ElementButton(LoadCfg.bankBtWithdraw, Image.getImageByCfg(LoadCfg.cfg.getString("Gui.Bank.Withdraw.Image"))));
        return wd;
    }

    /**
     * 获取商店界面 - 调用使用异步
     * @return 商店
     */
    public static FormWindowSimple getShopMenu(){
        FormWindowSimple wd=new FormWindowSimple(LoadCfg.shopTitle,LoadCfg.shopText);
        for(String key:LoadData.shopData.getKeys(false)){
            //添加商品按钮
            if(LoadData.shopData.getString(key)==null){continue;}
            Item item=ItemNBT.getItemByKey(key);
            String itemName=item.hasCustomName()?item.getCustomName():item.getName();
            wd.addButton(new ElementButton(LoadCfg.shopBtText.replaceAll("<item>",itemName),Image.getImageByCfg(ItemIDSunName.getIDByPath(item))));
        }
        return wd;
    }
}
