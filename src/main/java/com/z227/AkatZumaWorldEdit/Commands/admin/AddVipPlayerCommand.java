package com.z227.AkatZumaWorldEdit.Commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AddVipPlayerCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("add")
                                        .requires((commandSource) -> commandSource.hasPermission(2))
//                                        .requires((commandSource) -> commandSource.getPlayer()!=null)
                                        .then(Commands.literal("viplayer")
                                                .then(Commands.argument("玩家名字", StringArgumentType.string())
                                                        .executes((context)->{
                                                            addVipPlayer(context);
                                                            return 1;
                                                        })))
                                        .then(Commands.literal("whitelist")
                                                .then(Commands.argument("num", IntegerArgumentType.integer(-1))
                                                        .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                                                .executes((context)->{
                                                                    addBlock(context,false);
                                                                    return 1;
                                                                }))))
                                        .then(Commands.literal("vipwhitelist")
                                                .then(Commands.argument("num", IntegerArgumentType.integer(-1))
                                                        .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                                                .executes((context)->{
                                                                    addBlock(context,true);
                                                                    return 1;
                                                                }))))
                                        .then(Commands.literal("replaceList")
                                                .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                                        .executes((context)->{
                                                            addreplaceList(context);
                                                            return 1;
                                                        })))
                                        .then(Commands.literal("range")
                                                .then(Commands.argument("num",IntegerArgumentType.integer(0))
                                                        .executes((context)->{
                                                            setRange(context,false);
                                                            return 1;
                                                        })))
                                        .then(Commands.literal("viprange")
                                                .then(Commands.argument("num",IntegerArgumentType.integer(0))
                                                        .executes((context)->{
                                                            setRange(context,true);
                                                            return 1;
                                                        })))

                        )

        );

    }



    public static void addVipPlayer(CommandContext<CommandSourceStack> context){
        Player player = context.getSource().getPlayer();
        String playerName = StringArgumentType.getString(context, "玩家名字");
        List<? extends String> vipList = Config.VIPPlayerList.get();
        Set<String> set = new LinkedHashSet<>(vipList);
        set.add(playerName);
        Config.VIPPlayerList.set(set.stream().toList());
        Config.VIPPlayerList.save();
        AkatZumaWorldEdit.VipPlayerMap.put(playerName,true);
        Component component = Component.translatable("chat.akatzuma.success.add_viplayer");
        if(player!=null) AkatZumaWorldEdit.sendAkatMessage(Component.literal("")
                .append(component).withStyle(ChatFormatting.GREEN)
                .append(playerName), player);
        AkatZumaWorldEdit.LOGGER.info(component.getString() +playerName);

    }
    public static void addBlock(CommandContext<CommandSourceStack> context,boolean vip){
        Player player = context.getSource().getPlayer();
        int num = IntegerArgumentType.getInteger(context, "num");
        BlockState block = BlockStateArgument.getBlock(context, "block").getState();

        ForgeConfigSpec.ConfigValue<List<? extends String>> blockList;
        if(num == -1){
            if(vip)blockList = Config.VIPBLACKListBlock;
            else blockList = Config.BLACKListBlock;
        }else{
            if(vip)blockList = Config.VIPWHITEListBlock;
            else blockList = Config.WHITEListBlock;
        }

        Set<String> set = new LinkedHashSet<>(blockList.get());
        String blockName = BlockStateString.getBlockName(block);

        if(num == -1){
            set.add(blockName);
        }else{
            String configblockName = num + "#" + blockName;
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()){
                String k = iterator.next();
                String tempK = k.split("#")[1];
                if(tempK.equals(blockName)){
                    set.remove(k);
                    break;
                }
            }
            set.add(configblockName);
        }

        if(vip){
            blockList.set(set.stream().toList());
            blockList.save();
            AkatZumaWorldEdit.VipBlockMap.put(blockName,num);
        }
        else {
            blockList.set(set.stream().toList());
            blockList.save();
            AkatZumaWorldEdit.defaultBlockMap.put(blockName,num);
        }

        Component component = Component.translatable("chat.akatzuma.success.add_viplayer");
        if(player!=null) AkatZumaWorldEdit.sendAkatMessage(Component.literal("")
                .append(component).withStyle(ChatFormatting.GREEN)
                .append(blockName), player);
        AkatZumaWorldEdit.LOGGER.info(component.getString() + blockName + ":" + num);


    }

    public static void addreplaceList(CommandContext<CommandSourceStack> context){
        Player player = context.getSource().getPlayer();
        BlockState block = BlockStateArgument.getBlock(context, "block").getState();


        ForgeConfigSpec.ConfigValue<List<? extends String>> replaceList = Config.ReplaceBlockList;


        Set<String> set = new LinkedHashSet<>(replaceList.get());
        String blockName = BlockStateString.getBlockName(block);

        set.add(blockName);

        replaceList.set(set.stream().toList());
        replaceList.save();
        AkatZumaWorldEdit.ReplaceBlockMap.put(blockName,true);

        Component component = Component.translatable("chat.akatzuma.success.add_viplayer");
        if(player!=null) AkatZumaWorldEdit.sendAkatMessage(Component.literal("")
                .append(component).withStyle(ChatFormatting.GREEN)
                .append(blockName), player);
        AkatZumaWorldEdit.LOGGER.info(component.getString() + blockName);
    }

    public static void setRange(CommandContext<CommandSourceStack> context, boolean vip) {
        int num = IntegerArgumentType.getInteger(context, "num");
        if(vip){
            Config.VIPValue.set(num);
            Config.VIPValue.save();
            sendAddSuccess(context.getSource().getPlayer(), String.valueOf(num));
        }else{
            Config.DEFAULTValue.set(num);
            Config.DEFAULTValue.save();
            sendAddSuccess(context.getSource().getPlayer(), String.valueOf(num));
        }
    }

    public static void sendAddSuccess(Player player,String msg){
        Component component = Component.translatable("chat.akatzuma.success.add_viplayer");
        if(player!=null) AkatZumaWorldEdit.sendAkatMessage(Component.literal("")
                .append(component).withStyle(ChatFormatting.GREEN)
                .append(msg), player);
    }

}
