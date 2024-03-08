package com.z227.AkatZumaWorldEdit.network.serverPacket;

import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class ServerIntPacketHandle {
    public static void handle(Player player, int message) {
//        Util.logInfo("server收到的数据：" + message);
        switch (message){
            case 0 -> setQueryBlockState(player);       // /a pos1

        }
    }

    public static void setQueryBlockState(Player player) {
        PlayerMapData PMD = Util.getPMD(player);
        PMD.setQueryBlockState(Blocks.AIR.defaultBlockState());

    }
}
