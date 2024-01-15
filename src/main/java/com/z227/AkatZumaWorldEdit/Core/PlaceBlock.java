package com.z227.AkatZumaWorldEdit.Core;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;

import java.util.Map;
import java.util.UUID;

public class PlaceBlock {

    //判断能否放置方块
    public Boolean isPlaceBlock(Level world, Player player, BlockPos blockPos, BlockState blockState){

        BlockSnapshot snapshot = BlockSnapshot.create(world.dimension(), world, blockPos);
        BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(snapshot, blockState, player);
        MinecraftForge.EVENT_BUS.post(placeEvent);
        if(placeEvent.isCanceled()) {
            return false;
        }
        return true;
    }

        //计算长宽高
    public static Vec3i calculateCubeDimensions(BlockPos pos1, BlockPos pos2) {
        int length = Math.abs(pos1.getX() - pos2.getX()) + 1;
        int width = Math.abs(pos1.getY() - pos2.getY()) + 1;
        int height = Math.abs(pos1.getZ() - pos2.getZ()) + 1;
        return new Vec3i(length,width,height);
    }

    //遍历两个坐标之间的每个点
    public static void traverseCube(BlockPos pos1, BlockPos pos2, Level world, Player player, BlockState blockState) {
        UUID uuid = player.getUUID();
        PlayerMapData pwm = AkatZumaWorldEdit.PlayerWEMap.get(uuid);

        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
//                    world.setBlockAndUpdate(new BlockPos(x,y,z),blockState);
//                    world.setBlock(new BlockPos(x,y,z),blockState);
                    BlockPos v3 = new BlockPos(x,y,z);
//                    pwm.setUndoDataMap(v3, blockState);
//                    pwm.setUndoDataMap(world.getBlockState(v3), v3);
                    world.setBlock(v3,blockState, 2);

                }
            }
        }
    }

    //遍历两个坐标之间的每个点
    public static void copy(BlockPos pos1, BlockPos pos2, Level world, Player player) {
        UUID uuid = player.getUUID();
        PlayerMapData pwm = AkatZumaWorldEdit.PlayerWEMap.get(uuid);

        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
                    BlockPos v3 = new BlockPos(x,y,z);
                    BlockState blockState = world.getBlockState(v3);
                    pwm.setUndoDataMap(v3, blockState);

                }
            }
        }
    }




     public static boolean checkArea(BlockPos pos1, BlockPos pos2,Player player,int configVolume ){
         // 选区大小
         Vec3i vec3 = calculateCubeDimensions(pos1, pos2);
         int volume =  vec3.getX() * vec3.getY()* vec3.getZ();

//         int configVolume = Config.DEFAULTValue.get();
//         if(Config.VIPPlayerList.get().contains(player.getName().toString())){
//             configVolume = Config.VIPValue.get();
//         }
         if(volume > configVolume){
             Component component = Component.translatable("chat.akatzuma.error.volume_too_long");
             AkatZumaWorldEdit.sendAkatMessage(component,String.valueOf(configVolume), player);
             return false;
         }
         return true;
     }

    public static boolean checkBlackList(Player player, BlockState blockState, Map<String,Integer> CM){
        //检查黑名单
        String blockName = blockState.getBlock().toString().replace("Block{","").replace("}" ,"");
//        Map<String,Boolean> CM =  AkatZumaWorldEdit.ConfigMap;

        if(CM.get(blockName) != null && CM.get(blockName) < 0){
            MutableComponent component = Component.translatable("chat.akatzuma.error.black_list");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }
        return true;
    }

    //检查背包
    public static boolean isCheckInventory( Player player, BlockState blockState){
        Map<String, Integer> blockInInvMap = Util.findBlockFromPlayerInv(blockState,player);
        if(blockInInvMap != null && blockInInvMap.get("count") > 0){
            return true;
        }
        return false;
    }

    public static boolean canSetBlock(BlockPos pos1, BlockPos pos2, Level world, Player player, BlockState blockState, boolean permissionLevel, PlayerMapData PMD){
        // 判断上次操作是否完成
        if(!PMD.isFlag()){
            Component component = Component.translatable("chat.akatzuma.error.wait");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }
        // 设置标志位
        PMD.setFlag(false);

        // 判断坐标是否为null
        if(pos1 == null||pos2 == null){
            MutableComponent component = Component.translatable("chat.akatzuma.error.invalid_pos");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }


        //如果不是管理员
        if(!permissionLevel){
            int areaValue = Config.DEFAULTValue.get();      //选区大小
            boolean checkInventory = Config.CHECKInventory.get();    //是否检查背包
            Map<String,Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
            if(PMD.isVip()){
                areaValue = Config.VIPValue.get();      //选区大小
                checkInventory = Config.VIPCHECKInventory.get();    //是否检查背包
                blackWhiteMap = AkatZumaWorldEdit.VipBlockMap;    //黑白名单方块
            }

//            if(checkVip(player, Config.VIPPlayerList.get())){
//
//            }

            // 选区大小
            if(!checkArea(pos1, pos2,player, areaValue)){
                return  false;
            }
            //检查黑名单
            if(!checkBlackList(player, blockState, blackWhiteMap)){
                return  false;
            }

            //区块是否加载
            if(!world.hasChunkAt(pos1) || !world.hasChunkAt(pos2)){
                MutableComponent component = Component.translatable("chat.akatzuma.error.chunk_not_loaded");
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return false;
            }


            //检查白名单，检查背包
//            if(checkInventory){
//
//            }


            //检查放置权限

        }





        return true;

    }
}