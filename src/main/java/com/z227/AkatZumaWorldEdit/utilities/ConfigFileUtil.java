package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Path;

public class ConfigFileUtil {

    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static void createConfigDir() {
        File file = new File(getConfigPath() + "/AkatZumaWorldEdit");
        if (!file.exists()) {
            file.mkdir();
        }
    }

//    public static void copyFontToConfigDir() {
//        createConfigDir();
//        InputStream is = AkatZumaWorldEdit.class.getClassLoader().getResourceAsStream("assets/akatzumaworldedit/libs/fonts/NotoSansCJK-Regular-1.otf");
//        if (is == null) {
//
//            return;
//        }
//        File file = new File(getConfigPath() + "/AkatZumaWorldEdit/NotoSansCJK-Regular-1.otf");
//
//        if(!file.exists()){
//            try {
//                FileUtils.copyInputStreamToFile(is, file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
