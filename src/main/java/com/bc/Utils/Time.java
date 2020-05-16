package com.bc.Utils;

import com.bc.Utils.load.LoadCfg;
import com.bc.Utils.load.LoadData;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wrx-18090248
 * @date 2020/5/5 21:55
 */
public class Time {
    /**
     * 获取剩余天数
     * @param id 物品ID
     * @return 剩余的天数
     * @throws ParseException 转换错误
     */
    public static int getSurplusDay(String id) throws ParseException {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        if(LoadData.shopData.get(id)==null){
            return -1;
        }
        int vipLevel=LoadData.shopData.getInt("VipLevel");
        int addDay=0;
        if(LoadCfg.cfg.get("Shop.VipLevel."+vipLevel)!=null){
            addDay=LoadCfg.cfg.getInt("Shop.VipLevel."+vipLevel);
        }
        return (LoadCfg.keepDay+addDay)-daysBetween(LoadData.shopData.getString(id+".Date"),df.format(new Date()));
    }
    /**
     * 计算和当日的天数差距
     * @param oldTime 当前日期
     * @param newTime 之前日期
     * @return 相差天数
     * @throws ParseException 转换错误
     */
    public static int daysBetween(String oldTime,String newTime) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(oldTime));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(newTime));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算和现在的分钟差距
     * @param oldTime 以前时间
     * @param newTime 现在时间
     * @return 分钟数
     * @throws ParseException 转换错误
     */
    public static String minuteBetween(String oldTime,String newTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long NTime =df.parse(newTime).getTime();
        long OTime = df.parse(oldTime).getTime();
        long diff=(NTime-OTime)/1000/60;
        DecimalFormat dfs = new DecimalFormat("#.00");
        return dfs.format(diff);
    }
}
