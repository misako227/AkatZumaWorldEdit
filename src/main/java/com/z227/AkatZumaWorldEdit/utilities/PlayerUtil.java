package com.z227.AkatZumaWorldEdit.utilities;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;

public class PlayerUtil {
    //获取玩家朝向，大于-45度 y = 1，小于45度 y = -1
    public static Vec3i getPlayerNormal(LocalPlayer player){
        float yAngle = Mth.floor(Mth.wrapDegrees(player.getXRot()));
        if(yAngle < -45.0 ) return new Vec3i(0,1,0);
        if(yAngle > 45.0 ) return new Vec3i(0,-1,0);

        return player.getDirection().getNormal();
    }

    public static BlockPos getPlayerPOVHitEnd(Player pPlayer, int distance) {
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
        return getPlayerPOVHitResult(player, 256);
    }


    //获取玩家是否更新方块
    public static boolean isSetUpdateBlock(Player player){
        AttributeInstance attribute = player.getAttribute(AkatZumaWorldEdit.SET_FLAG_ATTRIBUTE.get());
        boolean flag = false;
        if(attribute != null) {
            double v = attribute.getBaseValue();
            flag = v > 0;
        }
        return flag;
    }

    public static boolean getPermission(Player player, PermissionNode<Boolean> permiss){
        boolean per = PermissionAPI.getPermission((ServerPlayer) player, permiss);
        System.out.println("check: "+ per);
        return per;
    }


}
