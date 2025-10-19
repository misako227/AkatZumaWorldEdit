//package com.z227.ImGuiRender;
//
//import com.mojang.blaze3d.platform.NativeImage;
//import com.mojang.blaze3d.platform.TextureUtil;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.TextureAtlas;
//import net.minecraft.server.packs.resources.ResourceManager;
//import net.minecraft.resources.ResourceLocation;
//import javax.annotation.Nullable;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class AtlasExporter {
//
//    // 获取所有可用的纹理图集类型
//    public static ResourceLocation[] getAllAtlasTypes() {
//        return new ResourceLocation[]{
//                TextureAtlas.LOCATION_BLOCKS, // 方块纹理图集（包含物品）
//                TextureAtlas.LOCATION_PARTICLES, // 粒子效果图集
//
//                // 可以添加更多图集类型
//        };
//    }
//
//    // 导出指定纹理图集到文件
//    public static boolean exportAtlas(ResourceLocation atlasType, @Nullable String customPath) {
//        Minecraft minecraft = Minecraft.getInstance();
//        TextureAtlas textureAtlas = (TextureAtlas) minecraft.getTextureManager().getTexture(atlasType);
//
//        if (textureAtlas == null) {
//            System.err.println("Atlas not found: " + atlasType);
//            return false;
//        }
//
//        // 获取图集尺寸
//        int width = textureAtlas.getWidth();
//        int height = textureAtlas.height;
//
//        if (width <= 0 || height <= 0) {
//            System.err.println("Invalid atlas dimensions: " + width + "x" + height);
//            return false;
//        }
//
//        // 绑定图集纹理
//        TextureUtil.bind(textureAtlas.getId());
//
//        // 创建NativeImage保存纹理数据
//        NativeImage image = new NativeImage(width, height, false);
//        image.downloadTexture(0, false); // 从GPU读取纹理数据
//
//        // 确定保存路径
//        Path exportPath;
//        if (customPath != null && !customPath.isEmpty()) {
//            exportPath = Paths.get(customPath);
//        } else {
//            exportPath = Paths.get(minecraft.gameDirectory.getAbsolutePath(), "atlas_exports");
//        }
//
//        // 确保目录存在
//        if (!Files.exists(exportPath)) {
//            try {
//                Files.createDirectories(exportPath);
//            } catch (IOException e) {
//                System.err.println("Failed to create directory: " + exportPath);
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        // 生成文件名
//        String fileName = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) +
//                "_" + atlasType.getPath().replace("/", "_") +
//                "_" + width + "x" + height + ".png";
//
//        Path outputPath = exportPath.resolve(fileName);
//
//        // 保存到文件
//        try {
//            image.writeToFile(outputPath);
//            System.out.println("Successfully saved atlas to: " + outputPath);
//            return true;
//        } catch (IOException e) {
//            System.err.println("Failed to save atlas: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        } finally {
//            image.close(); // 释放资源
//        }
//    }
//
//    // 导出所有纹理图集
//    public static void exportAllAtlases() {
//        System.out.println("Exporting all Minecraft atlases...");
//        for (ResourceLocation atlasType : getAllAtlasTypes()) {
//            System.out.println("Exporting atlas: " + atlasType);
//            if (exportAtlas(atlasType, null)) {
//                System.out.println("Success: " + atlasType);
//            } else {
//                System.err.println("Failed: " + atlasType);
//            }
//        }
//    }
//}