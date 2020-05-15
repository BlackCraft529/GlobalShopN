package com.bc.globalshopN.Utils;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import com.bc.globalshopN.gsn;
import com.bc.globalshopN.load.LoadData;
import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 17:53
 */
public class ItemNBT {
    public static Item getItemFromMail(Player p, String key){
        File pfi=new File(gsn.getPlugin().getDataFolder()+File.separator+"Players"+File.separator+"Mail",p.getName()+".yml");
        Config pf=new Config(pfi,Config.YAML);
        if(pf.getString(key+".Item").equalsIgnoreCase("null")||pf.get(key+".Item")==null){
            return null;
        }
        int id=Integer.parseInt(pf.getString(key+".Item").split(":")[0]),
                sid=Integer.parseInt(pf.getString(key+".Item").split(":")[1]),
                count=Integer.parseInt(pf.getString(key+".Item").split(":")[2]);
        Item it=new Item(id,sid,count,pf.getString(key+".Item").split(":")[4]);
        if(!pf.getString(key+".Item").split(":")[3].equals("not")){
            CompoundTag tag=Item.parseCompoundTag(hexStringToBytes(pf.getString(key+".Item").split(":")[3]));
            it.setCompoundTag(tag);
        }
        return it;
    }
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
    public static String getItemNbt(Item item){
        String tag=item.hasCompoundTag() ?bytesToHexString(item.getCompoundTag()):"not";
        return item.getId()+":"+item.getDamage()+":"+item.getCount()+":"+tag+":"+ItemIDSunName.getIDByName(item);
    }
    public static Item getItemByKey(String key){
        int id=Integer.parseInt(LoadData.shopData.getString(key+".Item").split(":")[0]),
                sid=Integer.parseInt(LoadData.shopData.getString(key+".Item").split(":")[1]),
                count=Integer.parseInt(LoadData.shopData.getString(key+".Item").split(":")[2]);
        Item it=new Item(id,sid,count,LoadData.shopData.getString(key+".Item").split(":")[4]);
        if(!LoadData.shopData.getString(key+".Item").split(":")[3].equals("not")){
            CompoundTag tag=Item.parseCompoundTag(hexStringToBytes(LoadData.shopData.getString(key+".Item").split(":")[3]));
            it.setCompoundTag(tag);
        }
        return it;
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
