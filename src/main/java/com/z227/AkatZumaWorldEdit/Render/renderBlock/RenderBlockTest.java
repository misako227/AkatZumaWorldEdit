package com.z227.AkatZumaWorldEdit.Render.renderBlock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.BitSet;
import java.util.List;

public class RenderBlockTest {
    static final Direction[] DIRECTIONS = Direction.values();

    public static void renderBatched(BlockState pState, BlockPos pPos, BlockAndTintGetter pLevel, PoseStack pPoseStack, VertexConsumer pConsumer, boolean pCheckSides, RandomSource pRandom) {
        renderBatched(pState, pPos, pLevel, pPoseStack, pConsumer, pCheckSides, pRandom, net.minecraftforge.client.model.data.ModelData.EMPTY, null);
    }

    public static void renderBatched(BlockState pState, BlockPos pPos, BlockAndTintGetter pLevel, PoseStack pPoseStack, VertexConsumer pConsumer, boolean pCheckSides, RandomSource pRandom, net.minecraftforge.client.model.data.ModelData modelData, net.minecraft.client.renderer.RenderType renderType) {
        try {
            RenderShape rendershape = pState.getRenderShape();
            if (rendershape == RenderShape.MODEL) {
                tesselateBlock(pLevel, getBlockModel(pState), pState, pPos, pPoseStack, pConsumer, pCheckSides, pRandom, pState.getSeed(pPos), OverlayTexture.NO_OVERLAY, modelData, renderType);
            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Tesselating block in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Block being tesselated");
            CrashReportCategory.populateBlockDetails(crashreportcategory, pLevel, pPos, pState);
            throw new ReportedException(crashreport);
        }
    }



    public static BakedModel getBlockModel(BlockState pState) {
//        Minecraft mc = Minecraft.getInstance();
//        BlockModelShaper blockModelShaper = mc.getBlockRenderer().getBlockModelShaper();
//        return blockModelShaper.getBlockModel(pState);
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(pState);
    }

    public static void tesselateBlock(BlockAndTintGetter pLevel, BakedModel pModel, BlockState pState, BlockPos pPos, PoseStack pPoseStack, VertexConsumer pConsumer, boolean pCheckSides, RandomSource pRandom, long pSeed, int pPackedOverlay, net.minecraftforge.client.model.data.ModelData modelData, net.minecraft.client.renderer.RenderType renderType) {
        // 判断是否使用环境光遮蔽
//        boolean flag = Minecraft.useAmbientOcclusion() && pState.getLightEmission(pLevel, pPos) == 0 && pModel.useAmbientOcclusion(pState, renderType);
        boolean flag =false;
        Vec3 vec3 = pState.getOffset(pLevel, pPos);
        pPoseStack.translate(vec3.x, vec3.y, vec3.z);

        try {
//            if (flag) {
//                modelRenderer.tesselateWithAO(pLevel, pModel, pState, pPos, pPoseStack, pConsumer, pCheckSides, pRandom, pSeed, pPackedOverlay, modelData, renderType);
//            } else {
                tesselateWithoutAO(pLevel, pModel, pState, pPos, pPoseStack, pConsumer, pCheckSides, pRandom, pSeed, OverlayTexture.pack(3,10), modelData, renderType);
//            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Block model being tesselated");
            CrashReportCategory.populateBlockDetails(crashreportcategory, pLevel, pPos, pState);
            crashreportcategory.setDetail("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }



    public static void tesselateWithoutAO(BlockAndTintGetter blockAndTintGetter, BakedModel bakedModel, BlockState blockState, BlockPos blockPos, PoseStack poseStack, VertexConsumer vertexConsumer, boolean skipAo, RandomSource randomSource, long seed, int packedLight, net.minecraftforge.client.model.data.ModelData modelData, net.minecraft.client.renderer.RenderType renderType) {
        // 创建一个BitSet来跟踪处理过的方向
        BitSet bitSet = new BitSet(3);
        // 使用可变的BlockPos来减少对象创建，提高性能
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

        // 遍历所有方向，获取并渲染每个方向的四边形
        for (Direction direction : DIRECTIONS) {
            // 为每个方向重新设置随机种子，确保一致性
            randomSource.setSeed(seed);
            // 获取当前方向的四边形列表
            List<BakedQuad> quads = bakedModel.getQuads(blockState, direction, randomSource, modelData, renderType);
            if (!quads.isEmpty()) {
                // 设置邻接块的位置
                mutableBlockPos.setWithOffset(blockPos, direction);
                // 检查是否应该渲染当前方向的面
                if (!skipAo || Block.shouldRenderFace(blockState, blockAndTintGetter, blockPos, direction, mutableBlockPos)) {
                    // 获取当前块位置的光照颜色
//                    int lightColor = LevelRenderer.getLightColor(blockAndTintGetter, blockState, mutableBlockPos);
//                    int lightColor = 15728880;     //655363
                    // 渲染模型的平面
                    renderModelFaceFlat(blockAndTintGetter, blockState, blockPos, 15728880, packedLight, false, poseStack, vertexConsumer, quads, bitSet);
                }
            }
        }

        // 重新设置随机种子，为无方向的四边形生成做准备
        randomSource.setSeed(seed);
        // 获取无方向的四边形列表
        List<BakedQuad> quadsNoDirection = bakedModel.getQuads(blockState, null, randomSource, modelData, renderType);
        if (!quadsNoDirection.isEmpty()) {
            // 渲染模型的无方向平面
            renderModelFaceFlat(blockAndTintGetter, blockState, blockPos, 15728880, 655363, true, poseStack, vertexConsumer, quadsNoDirection, bitSet);
        }
    }

    /**
     * 以平铺方式渲染模型面
     *
     * 此方法主要用于渲染一个给定位置的方块模型面，考虑到光照和遮蔽效果
     * 它通过计算每个面片的光照值，并将结果存储在一个顶点消费者中
     *
     * @param pLevel 游戏级别，用于获取光照和遮蔽信息
     * @param pState 方块的状态
     * @param pPos 方块的位置
     * @param pPackedLight 预打包的光照值
     * @param pPackedOverlay 预打包的覆盖层值
     * @param pRepackLight 是否需要重新计算光照值
     * @param pPoseStack 姿态栈，用于变换顶点位置
     * @param pConsumer 顶点消费者，存储计算结果
     * @param pQuads 模型的四边形面列表
     * @param pShapeFlags 位集合，用于标记形状特征
     */
    public static void renderModelFaceFlat(BlockAndTintGetter pLevel, BlockState pState, BlockPos pPos, int pPackedLight, int pPackedOverlay, boolean pRepackLight, PoseStack pPoseStack, VertexConsumer pConsumer, List<BakedQuad> pQuads, BitSet pShapeFlags) {
        for(BakedQuad bakedquad : pQuads) {
            // 如果需要重新计算光照，根据当前面的方向计算受影响的方块位置
//            if (pRepackLight) {
//                // 计算形状的光照信息，并更新形状标记
//                this.calculateShape(pLevel, pState, pPos, bakedquad.getVertices(), bakedquad.getDirection(), (float[])null, pShapeFlags);
//                // 根据形状标记确定光照计算位置
//                BlockPos blockpos = pShapeFlags.get(0) ? pPos.relative(bakedquad.getDirection()) : pPos;
//                // 重新获取光照值
//                pPackedLight = LevelRenderer.getLightColor(pLevel, pState, blockpos);
//            }

            // 根据当前面的方向和是否需要遮蔽，计算光照因子
            float pBrightness0 = pLevel.getShade(bakedquad.getDirection(), bakedquad.isShade());
//            if (bakedquad.isTinted()) {
//                putQuadData(pState, pConsumer, pPoseStack.last(), bakedquad, pBrightness0, pPackedLight,pPackedOverlay);
//            }else{
//                // 将计算结果放入顶点消费者
//                putQuadData(pConsumer, pPoseStack.last(), bakedquad, pBrightness0, pPackedLight,pPackedOverlay);
//            }
            putQuadData(pState, pConsumer, pPoseStack.last(), bakedquad, pBrightness0,pBrightness0,pBrightness0,pBrightness0,pPackedLight,pPackedLight,pPackedLight,pPackedLight, pPackedOverlay);

        }
    }

    public static void renderModelFaceFlat(BlockState pState, int pPackedLight, int pPackedOverlay, PoseStack pPoseStack, VertexConsumer pConsumer, List<BakedQuad> pQuads) {
        float pBrightness0 = 1.0F;
        for(BakedQuad bakedquad : pQuads) {
            // 如果需要重新计算光照，根据当前面的方向计算受影响的方块位置
//            if (pRepackLight) {
//                // 计算形状的光照信息，并更新形状标记
//                this.calculateShape(pLevel, pState, pPos, bakedquad.getVertices(), bakedquad.getDirection(), (float[])null, pShapeFlags);
//                // 根据形状标记确定光照计算位置
//                BlockPos blockpos = pShapeFlags.get(0) ? pPos.relative(bakedquad.getDirection()) : pPos;
//                // 重新获取光照值
//                pPackedLight = LevelRenderer.getLightColor(pLevel, pState, blockpos);
//            }

            // 根据当前面的方向和是否需要遮蔽，计算光照因子
//            float pBrightness0 = pLevel.getShade(bakedquad.getDirection(), bakedquad.isShade());
//            if (bakedquad.isTinted()) {
//                putQuadData(pState, pConsumer, pPoseStack.last(), bakedquad, pBrightness0, pPackedLight,pPackedOverlay);
//            }else{
//                // 将计算结果放入顶点消费者
//                putQuadData(pConsumer, pPoseStack.last(), bakedquad, pBrightness0, pPackedLight,pPackedOverlay);
//            }
            putQuadData(pState, pConsumer, pPoseStack.last(), bakedquad, pBrightness0,pBrightness0,pBrightness0,pBrightness0,pPackedLight,pPackedLight,pPackedLight,pPackedLight, pPackedOverlay);

        }
    }

    public static void putQuadData(BlockState pState, VertexConsumer pConsumer, PoseStack.Pose pPose, BakedQuad pQuad, float pBrightness0, float pBrightness1, float pBrightness2, float pBrightness3, int pLightmap0, int pLightmap1, int pLightmap2, int pLightmap3, int pPackedOverlay) {
        float f;
        float f1;
        float f2;
        if (pQuad.isTinted()) {

            int i = Minecraft.getInstance().getBlockColors().getColor(pState, (BlockAndTintGetter)null, (BlockPos)null, 0);
            f = (float)(i >> 16 & 255) / 255.0F;
            f1 = (float)(i >> 8 & 255) / 255.0F;
            f2 = (float)(i & 255) / 255.0F;
        } else {
            f = 1.0F;
            f1 = 1.0F;
            f2 = 1.0F;
        }

        pConsumer.putBulkData(pPose, pQuad, new float[]{pBrightness0, pBrightness1, pBrightness2, pBrightness3}, f,f1,f2, new int[]{pLightmap0, pLightmap1, pLightmap2, pLightmap3}, pPackedOverlay, false);
    }


    public static void putQuadData(VertexConsumer pConsumer, PoseStack.Pose pPose, BakedQuad pQuad, float pBrightness0, int pLightmap0,int pPackedOverlay) {
        pConsumer.putBulkData(pPose, pQuad, new float[]{pBrightness0, pBrightness0, pBrightness0, pBrightness0}, 1.0F, 1.0F,1.0F, new int[]{pLightmap0, pLightmap0, pLightmap0, pLightmap0}, pPackedOverlay, false);
    }

    public static void putQuadData(BlockState pState,VertexConsumer pConsumer, PoseStack.Pose pPose, BakedQuad pQuad, float pBrightness0, int pLightmap0,int pPackedOverlay) {
        putQuadData(pState,pConsumer,pPose, pQuad, pBrightness0, pBrightness0, pBrightness0, pBrightness0,  pLightmap0, pLightmap0, pLightmap0, pLightmap0, pPackedOverlay );
    }


//    public static boolean shouldRenderFace(BlockState pState, BlockGetter pLevel, BlockPos pOffset, Direction pFace, BlockPos pPos) {
//        // 检查是否跳过渲染
//        BlockState blockstate = pLevel.getBlockState(pPos);
//        if (pState.skipRendering(blockstate, pFace)) {
//            return false;
//        } else if (pState.supportsExternalFaceHiding() && blockstate.hidesNeighborFace(pLevel, pPos, pState, pFace.getOpposite())) {
//            return false;
//        } else if (blockstate.canOcclude()) {
//            // 使用缓存处理遮挡检测
//            Block.BlockStatePairKey block$blockstatepairkey = new Block.BlockStatePairKey(pState, blockstate, pFace);
//            Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> object2bytelinkedopenhashmap = OCCLUSION_CACHE.get();
//            byte b0 = object2bytelinkedopenhashmap.getAndMoveToFirst(block$blockstatepairkey);
//            if (b0 != 127) {
//                return b0 != 0;
//            } else {
//                // 计算并比较两个方块面的形状
//                VoxelShape voxelshape = pState.getFaceOcclusionShape(pLevel, pOffset, pFace);
//                if (voxelshape.isEmpty()) {
//                    return true;
//                } else {
//                    VoxelShape voxelshape1 = blockstate.getFaceOcclusionShape(pLevel, pPos, pFace.getOpposite());
//                    boolean flag = Shapes.joinIsNotEmpty(voxelshape, voxelshape1, BooleanOp.ONLY_FIRST);
//                    if (object2bytelinkedopenhashmap.size() == 2048) {
//                        object2bytelinkedopenhashmap.removeLastByte();
//                    }
//
//                    object2bytelinkedopenhashmap.putAndMoveToFirst(block$blockstatepairkey, (byte)(flag ? 1 : 0));
//                    return flag;
//                }
//            }
//        } else {
//            return true;
//        }
//    }

}
