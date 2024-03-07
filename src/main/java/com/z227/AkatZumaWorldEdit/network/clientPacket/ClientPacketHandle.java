package com.z227.AkatZumaWorldEdit.network.clientPacket;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPos;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPosCapability;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandle {

    public static void handle(int message) {
//        Util.logInfo("Client收到的数据：" + message);
        switch (message){
            case 1 -> setClientPos(true);       // /a pos1
            case 2 -> setClientPos(false);      // /a pos2
            case 11 -> setClientCopyMap();      // copy
            case 12 -> setClientFlip(false);    // flip
            case 13 -> setClientFlip(true);     // flip up
        }
    }

    //同步cap到客户端
    public static void handle(CompoundTag tag) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        LazyOptional<BindInventoryPos> bindPos = player.getCapability(BindInventoryPosCapability.BIND_INV_POS_CAP);
        bindPos.ifPresent(bp -> {
            bp.setCompoundNBT(tag);
            Util.getPMD(player).setInvPosNBT(bp.getCompoundNBT(),player);
        });

    }

    @OnlyIn(Dist.CLIENT)
    public static void setClientPos(boolean b){
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        BlockPos pos = BlockPos.containing(player.getEyePosition());
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());;
        if(b)PMD.setPos1(pos);
        else PMD.setPos2(pos);
    }

    public static void setClientCopyMap(){
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = player.level();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());;
        CopyBlock copyBlock = new CopyBlock(PMD, player);
        if(copyBlock.checkPosAddCopyMap(level, PMD)){
            PMD.setCopyBlockClient(copyBlock);
        }
    }

    public static void setClientFlip(boolean up){
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;


        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        CopyBlock copyBlock = PMD.getCopyBlockClient();
        if(copyBlock != null){
            copyBlock.flip(up);
        }



    }
}
