package com.bc.globalshopN.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import com.bc.globalshopN.load.LoadCfg;
import com.bc.globalshopN.load.LoadLang;
import com.bc.globalshopN.math.MailWindowMath;
import com.bc.globalshopN.math.MenuWindowMath;
import com.bc.globalshopN.math.SellWindowMath;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 17:15
 */
public class MenuListener implements Listener {
    public static ThreadFactory threadFactory=new ThreadFactoryBuilder().setNameFormat("my-pool-%d").build();
    public static ExecutorService sigleThreadPool = new ThreadPoolExecutor(1,1,0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024),threadFactory,new ThreadPoolExecutor.AbortPolicy());

    /**
     * 主菜单操作
     * @param e 事件
     */
    @EventHandler
    public void onClickMenu(PlayerFormRespondedEvent e){
        if(e.getPlayer()==null){ return; }
        if (e.getResponse() == null){ return; }
        FormWindow window = e.getWindow();
        if(!(window instanceof FormWindowSimple)||e.wasClosed()){ return;}
        if(!((FormWindowSimple) window).getTitle().equals(LoadCfg.menuTitle)){return;}
        Player p=e.getPlayer();
        //打开银行
        if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.menuBtBank)){
            p.showFormWindow(MenuWindowMath.getBankMenu());
            return;
        //打开全球商场 ↓
        }else if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.menuBtOpen)){
            final Player ps=p;
            sigleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    ps.showFormWindow(MenuWindowMath.getShopMenu());
                }
            });
            return;
        //打开邮箱 ↓
        }else if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.menuBtMail)){
            final Player ps=p;
            sigleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    ps.showFormWindow(MailWindowMath.getMailMenu(ps));
                }
            });
            return;
        //打开出售 ↓
        }else if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.menuBtSell)){
            if(p.getInventory().getItemInHand().equals(new Item(0))){
                p.sendMessage(LoadLang.title+LoadLang.errorIsAir);
                return;
            }
            p.showFormWindow(SellWindowMath.getSellWindow(p));
            return;
        //打开发送邮件界面 ↓
        }else if(((FormResponseSimple)e.getResponse()).getClickedButton().getText().equalsIgnoreCase(LoadCfg.menuBtSendMail)){
            p.showFormWindow(MailWindowMath.getSendMailMenu(p));
            return;
        }
    }
}
