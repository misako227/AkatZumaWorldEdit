package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class PlayerUtil {
    //获取玩家朝向，大于-45度 y = 1，小于45度 y = -1
    public static Vec3i getPlayerNormal(LocalPlayer player){
        float yAngle = Mth.floor(Mth.wrapDegrees(player.getXRot()));
        if(yAngle < -45.0 ) return new Vec3i(0,1,0);
        if(yAngle > 45.0 ) return new Vec3i(0,-1,0);

        return player.getDirection().getNormal();
    }

    public static BlockPos getPlayerPOVHitEnd(LocalPlayer pPlayer, int distance) {
        float f = pPlayer.getXRot();
        float f1 = pPlayer.getYRot();
        Vec3 vec3 = pPlayer.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;

        Vec3 vec31 = vec3.add((double) f6 * distance, (double) f5 * distance, (double) f7 * distance);
        return new BlockPos((int)vec31.x, (int)vec31.y, (int)vec31.z);
    }
}
