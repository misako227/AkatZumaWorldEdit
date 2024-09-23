package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RenderBlock {
    private static VertexConsumer vb ;
    private static MultiBufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

    public static void renderBlock(PoseStack stack, BlockPos pos, BlockState blockState){

        vb = buffer.getBuffer(RenderType.solid());
    }
}
