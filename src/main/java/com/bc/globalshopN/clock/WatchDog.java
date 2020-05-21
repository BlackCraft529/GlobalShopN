package com.bc.globalshopN.clock;

import cn.nukkit.item.Item;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.Time;
import com.bc.Utils.load.LoadCfg;
import com.bc.gsn;
import com.bc.Utils.load.LoadData;
import com.bc.Utils.load.LoadLang;
import com.bc.mailsN.math.MailMath;
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
    private static ThreadFactory threadFactory=new ThreadFactoryBuilder().setNameFormat("my-pool-%d").build();
    public static ExecutorService sigleThreadPool = new ThreadPoolExecutor(1,1,0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024),threadFactory,new ThreadPoolExecutor.AbortPolicy());
    /**
     * 检视线程
     */
    public static void onWatch(){
        sigleThreadPool.execute(() -> {
            //do Something
            while(true){
                try {
                    for(String key : LoadData.shopData.getKeys(false)){
                        if(LoadData.shopData.get(key)!=null){
                            try {
                                if(Time.getGoodsSurplusDay(key)<0){
                                    Item item= ItemNBT.getItemByNbtString(LoadData.shopData.getString(key+".Item"));
                                    String receiver=LoadData.shopData.getString(key+".Player");
                                    LoadData.shopData.remove(key);
                                    LoadData.shopData.save();
                                    MailMath.sendMail(gsn.getPlugin().getServer().getConsoleSender(),receiver,item, LoadLang.mailBack);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //半小时判断一次
                    Thread.sleep(LoadCfg.watchDog);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
