package com.bc.Utils;

import cn.nukkit.form.element.ElementButtonImageData;

/**
 * @author Luckily_Baby
 * @date 2020/5/5 16:27
 */
public class Image {
    public static ElementButtonImageData getImageByCfg(String path){
        return new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH,path);
    }
}
