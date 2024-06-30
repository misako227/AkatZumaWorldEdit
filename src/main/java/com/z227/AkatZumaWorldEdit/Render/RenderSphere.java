package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class RenderSphere {
    public static void render(PoseStack stack,Item item, PlayerMapData PMD)
    {
        BrushBase brushBase = PMD.getBrushMap().get(item);
        Map<BlockPos, Byte> shapePosMap= brushBase.getShapePosMap();
        BlockState blockState = brushBase.getShapeBase().getBlockState();

        if(!shapePosMap.isEmpty()){


            for (Map.Entry<BlockPos, Byte> entry : shapePosMap.entrySet()){
                BlockPos pos = entry.getKey();
                PreviewingRender.drawBlock(stack, pos, blockState);

            }
        }

    }
}
