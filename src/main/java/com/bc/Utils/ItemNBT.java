package com.bc.Utils;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import com.bc.Utils.load.LoadMails;
import com.bc.gsn;
import com.bc.Utils.load.LoadData;
import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 17:53
 */
public class ItemNBT {
    /**
     * 从全局邮件中获取物品
     * @param key 邮件ID
     * @return 物品
     */
    public static Item getItemFromGlobalMail(String key){
        if(LoadMails.globalMails.get(key+".Item")==null){
            return null;
        }
        return getItemByNbtString(LoadMails.globalMails.getString(key+".Item"));
    }
    /**
     * 从邮件中获取物品
     * @param p 玩家
     * @param key 邮件ID
     * @return 物品
     */
    public static Item getItemFromMail(Player p, String key){
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        Config pf=new Config(pfi,Config.YAML);
        if(pf.getString(key+".Item").equalsIgnoreCase("null")||pf.get(key+".Item")==null){
            return null;
        }
        return getItemByNbtString(pf.getString(key+".Item"));
    }

    /**
     * 保存物品到文件中
     * @param item 物品
     * @param key 关键字
     * @return 是否保存完成
     */
    public static boolean saveItemToYml(Item item,String key){
        if(LoadData.shopData.get(key)!=null){
            if(item.getCount()<=0){
                LoadData.shopData.remove(key);
                LoadData.shopData.save();
                return true;
            }
           LoadData.shopData.set(key+".Item",getItemNbt(item));
           LoadData.shopData.save();
           return true;
        }else{
           return false;
        }
    }

    /**
     * 获取物品NBT信息
     * @param item 物品
     * @return NBT字符串
     */
    public static String getItemNbt(Item item){
        String tag=item.hasCompoundTag() ?bytesToHexString(item.getCompoundTag()):"not";
        return item.getId()+":"+item.getDamage()+":"+item.getCount()+":"+tag+":"+ItemIDSunName.getIDByName(item);
    }

    /**
     * 根据物品NBT字符串获取物品
     * @param itemNbt 字符串
     * @return 物品
     */
    public static Item getItemByNbtString(String itemNbt){
        try {
            int id = Integer.parseInt(itemNbt.split(":")[0]),
                    sid = Integer.parseInt(itemNbt.split(":")[1]),
                    count = Integer.parseInt(itemNbt.split(":")[2]);
            Item it = new Item(id, sid, count, itemNbt.split(":")[4]);
            if (!itemNbt.split(":")[3].equals("not")) {
                CompoundTag tag = Item.parseCompoundTag(hexStringToBytes(itemNbt.split(":")[3]));
                it.setCompoundTag(tag);
            }
            return it;
        }catch (Exception ex){
            return null;
        }
    }
    public static Item getItemByKey(String key){
        return getItemByNbtString(LoadData.shopData.getString(key+".Item"));
    }
    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
