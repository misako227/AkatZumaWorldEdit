package com.z227.ImGuiRender.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class UtilImGui {
    public static Vec3 getPlayerPOVHitEndVec3(Player pPlayer, int distance) {
        float f = pPlayer.getXRot();
        float f1 = pPlayer.getYRot();
        Vec3 vec3 = pPlayer.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;

        return vec3.add((double) f6 * distance, (double) f5 * distance, (double) f7 * distance);
    }
    public static BlockHitResult getPlayerPOVHitResult(Player player, int distance){

        Level level = player.level();
        Vec3 toEnd = getPlayerPOVHitEndVec3(player, distance);
        return level.clip(new ClipContext(player.getEyePosition(), toEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

    }
    public static BlockHitResult getPlayerPOVHitResult(Player player){
        return getPlayerPOVHitResult(player, 120);
    }

}
