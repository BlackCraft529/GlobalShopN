package com.bc.globalshopN.clock;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import com.bc.globalshopN.Utils.ItemNBT;
import com.bc.globalshopN.Utils.Time;
import com.bc.globalshopN.gsn;
import com.bc.globalshopN.load.LoadData;
import com.bc.globalshopN.load.LoadLang;
import com.bc.globalshopN.math.MailMath;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 16:56
 */
public class WatchDog {
    public SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static ThreadFactory threadFactory=new ThreadFactoryBuilder().setNameFormat("my-pool-%d").build();
    public static ExecutorService sigleThreadPool = new ThreadPoolExecutor(1,1,0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024),threadFactory,new ThreadPoolExecutor.AbortPolicy());
    /**
     * 检视线程
     */
    public static void onWatch(){
        sigleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                //do Something
                while(true){
                    try {
                        for(String key : LoadData.shopData.getKeys(false)){
                            if(LoadData.shopData.get(key)!=null){
                                try {
                                    if(Time.getSurplusDay(key)<=0){
                                        Item item= ItemNBT.getItemByKey(key);
                                        Player p=gsn.getPlugin().getServer().getPlayer(LoadData.shopData.getString(key+".Player"));
                                        LoadData.shopData.remove(key);
                                        LoadData.shopData.save();
                                        MailMath.sendMail(gsn.getPlugin().getServer().getConsoleSender(),p,item, LoadLang.mailBack);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //半小时判断一次
                        Thread.sleep(1800000);
                        //System.out.println("休眠完成。。。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
