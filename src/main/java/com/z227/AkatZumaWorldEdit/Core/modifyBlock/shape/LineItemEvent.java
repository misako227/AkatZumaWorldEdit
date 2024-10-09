package com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape;

import com.z227.AkatZumaWorldEdit.utilities.PlayerUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public class LineItemEvent {


    public static void onItemLeftAir(LocalPlayer player, LineBase lineBase){

        BlockHitResult blockHitResult = PlayerUtil.getPlayerPOVHitResult(player);
        BlockPos blockPos = blockHitResult.getBlockPos();
        if(player.level().getBlockState(blockPos).isAir()) return;

        lineBase.addPosToEnd(blockPos);

    }

    public static void onItemCtrlLeftAir(LocalPlayer player, LineBase lineBase){

        BlockHitResult blockHitResult = PlayerUtil.getPlayerPOVHitResult(player);
        BlockPos blockPos = blockHitResult.getBlockPos();
        if(player.level().getBlockState(blockPos).isAir()) return;

        lineBase.addPosToStart(blockPos);

    }



    public static void onItemRightAir(Player player, LineBase lineBase){

        BlockHitResult blockHitResult = PlayerUtil.getPlayerPOVHitResult(player);
        BlockPos blockPos = blockHitResult.getBlockPos();
        if(player.level().getBlockState(blockPos).isAir()) return;
//        LineBase lineBase = PMD.getLineBase();
        lineBase.setRightPos(blockPos);

    }


}
