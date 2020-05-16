package com.bc.bankN.math;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import com.bc.Utils.PlayerFile;
import com.bc.Utils.load.LoadCfg;
import me.onebone.economyapi.EconomyAPI;
import net.player.api.Point;
import java.text.DecimalFormat;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 17:21
 */
public class BankWindowMath {
    private static DecimalFormat df = new DecimalFormat("#.00");
    /**
     * 获取存钱界面
     * @param p 取钱用户
     * @return 界面
     */
    public static FormWindowCustom getDepositWindow(Player p){
        FormWindowCustom wd=new FormWindowCustom(LoadCfg.bankOpDeposit);
        ElementInput takeMoney=new ElementInput(LoadCfg.bankDsIpMoney);
        ElementLabel hasMoney=new ElementLabel(LoadCfg.bankDsLbMoney.replaceAll("<money>", df.format(EconomyAPI.getInstance().myMoney(p))));
        //添加
        wd.addElement(hasMoney);
        wd.addElement(takeMoney);
        //点券相关
        if(LoadCfg.usePoint) {
            ElementInput takePoint = new ElementInput(LoadCfg.bankDsIpPoint);
            ElementLabel hasPoint=new ElementLabel(LoadCfg.bankDsLbPoint.replaceAll("<point>",df.format(Point.myPoint(p))));
            //添加
            wd.addElement(hasPoint);
            wd.addElement(takePoint);
        }
        return wd;
    }

    /**
     * 获取取钱界面
     * @param p 用户
     * @return 界面
     */
    public static FormWindowCustom getWithdrawWindow(Player p){
        FormWindowCustom wd=new FormWindowCustom(LoadCfg.bankOpWithdraw);
        ElementInput inputMoney=new ElementInput(LoadCfg.bankWdIpMoney);
        ElementLabel hasMoney=new ElementLabel(LoadCfg.bankWdLbMoney.replaceAll("<money>",df.format(PlayerFile.getPlayerBankMoney(p))));
        //添加
        wd.addElement(hasMoney);
        wd.addElement(inputMoney);
        if(LoadCfg.usePoint){
            ElementLabel hasPoint=new ElementLabel(LoadCfg.bankWdLbPoint.replaceAll("<point>",df.format(PlayerFile.getPlayerBankPoint(p))));
            ElementInput inputPoint=new ElementInput(LoadCfg.bankWdIpPoint);
            wd.addElement(hasPoint);
            wd.addElement(inputPoint);
        }
        return wd;
    }
}
