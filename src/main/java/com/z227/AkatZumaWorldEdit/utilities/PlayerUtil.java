package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;

public class PlayerUtil {
    //获取玩家朝向，大于-45度 y = 1，小于45度 y = -1
    public static Vec3i getPlayerNormal(LocalPlayer player){
        float yAngle = Mth.floor(Mth.wrapDegrees(player.getXRot()));
        if(yAngle < -45.0 ) return new Vec3i(0,1,0);
        if(yAngle > 45.0 ) return new Vec3i(0,-1,0);

        return player.getDirection().getNormal();
    }
}
