package com.z227.ImGuiRender.render;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private static final Map<ResourceLocation, Integer> TEXTURE_CACHE = new HashMap<>();

    public static int getTextureId(ResourceLocation location) {
        if (TEXTURE_CACHE.containsKey(location)) {
            return TEXTURE_CACHE.get(location);
        }
        return loadTexture(location);
    }

    private static int loadTexture(ResourceLocation location) {
        try (InputStream stream = Minecraft.getInstance().getResourceManager().open(location)) {
            Resource resource = Minecraft.getInstance().getResourceManager().getResource(location).orElseThrow();
            NativeImage image = NativeImage.read(stream);

            int textureId = GL30.glGenTextures();
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureId);

            // 设置纹理参数
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);

            // 上传图像数据到 GPU
            GL30.glTexImage2D(
                    GL30.GL_TEXTURE_2D,
                    0,
                    GL30.GL_RGBA,
                    image.getWidth(),
                    image.getHeight(),
                    0,
                    GL30.GL_RGBA,
                    GL30.GL_UNSIGNED_BYTE,
                    image.getPixelsRGBA()
            );

            TEXTURE_CACHE.put(location, textureId);
            image.close(); // 释放 NativeImage 内存
            return textureId;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture: " + location, e);
        }
    }

    // 在 Mod 卸载时释放纹理
    public static void releaseTextures() {
        TEXTURE_CACHE.values().forEach(GL30::glDeleteTextures);
        TEXTURE_CACHE.clear();
    }
}