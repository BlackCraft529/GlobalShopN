package com.bc.auctionN.clock;

import cn.nukkit.item.Item;
import com.bc.Utils.ItemNBT;
import com.bc.Utils.Time;
import com.bc.Utils.load.LoadAuction;
import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadLang;
import com.bc.gsn;
import com.bc.mailsN.math.MailMath;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;

/**
 * @author Luckily_Baby
 * @date 2020/5/21 17:38
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
                    for(String key : LoadAuction.autionData.getKeys(false)) {
                        if(LoadAuction.autionData.get(key)!=null) {
                            if (Time.getAuctionSurplusDay(key) <= 0) {
                                //无人拍下，返还物品
                                if("NULL".equalsIgnoreCase(LoadAuction.autionData.getString(key+".Auction.Player"))){
                                    Item item = ItemNBT.getItemByNbtString(LoadAuction.autionData.getString(key + ".Item"));
                                    String receiver=LoadAuction.autionData.getString(key+".Player");
                                    LoadAuction.autionData.remove(key);
                                    LoadAuction.autionData.save();
                                    MailMath.sendMail(gsn.getPlugin().getServer().getConsoleSender(),receiver,item, LoadLang.auctionBack);
                                    continue;
                                }
                                //拍卖给予
                                Item item = ItemNBT.getItemByNbtString(LoadAuction.autionData.getString(key + ".Item"));
                                String receiver=LoadAuction.autionData.getString(key+".Auction.Player");
                                LoadAuction.autionData.remove(key);
                                LoadAuction.autionData.save();
                                MailMath.sendMail(gsn.getPlugin().getServer().getConsoleSender(),receiver,item, LoadLang.auctionSucc.replaceAll("<id>",key));
                            }
                        }
                    }
                    Thread.sleep(LoadCfg.watchDog);
                } catch (InterruptedException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
