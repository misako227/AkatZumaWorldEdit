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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DelVipPlayerCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("del")
                                        .requires((commandSource) -> commandSource.hasPermission(2))
                                        .then(Commands.literal("viplayer")
                                                .then(Commands.argument("玩家名字", StringArgumentType.string())
                                                        .executes((context)->{
                                                            delVipPlayer(context);
                                                            return 1;
                                                        })))
                                .then(Commands.literal("whitelist")
                                        .then(Commands.argument("num", IntegerArgumentType.integer(-1))
                                                .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                                        .executes((context1)->{delWhiteList(context1, false);return 1; })
                                )))
                                .then(Commands.literal("vipwhitelist")
                                        .then(Commands.argument("num", IntegerArgumentType.integer(-1))
                                                .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                                        .executes((context1)->{delWhiteList(context1, true);return 1; })
                                                )))

                        )

        );

    }



    public static void delVipPlayer(CommandContext<CommandSourceStack> context){
        Player player = context.getSource().getPlayer();
        String playerName = StringArgumentType.getString(context, "玩家名字");
        List<? extends String> vipList = Config.VIPPlayerList.get();
        Set<String> set = new LinkedHashSet<>(vipList);
        Component component;
        if(set.remove(playerName)){
            Config.VIPPlayerList.set(set.stream().toList());
            Config.VIPPlayerList.save();
            AkatZumaWorldEdit.VipPlayerMap.remove(playerName);
            component = Component.translatable("chat.akatzuma.success.del_viplayer");
        }else{
            component = Component.translatable("chat.akatzuma.error.not_viplayer");
        }
        if(player!=null) {
            AkatZumaWorldEdit.sendAkatMessage(Component.literal("")
                    .append(component).withStyle(ChatFormatting.GREEN)
                    .append(playerName), player);
        }
        AkatZumaWorldEdit.LOGGER.info(component.getString() +playerName);

    }

    private static void delWhiteList(CommandContext<CommandSourceStack> context, boolean vip) {
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
            set.remove(blockName);
        }else{
            String configblockName = num + "#" + blockName;
            set.remove(configblockName);
        }

        if(vip){
            blockList.set(set.stream().toList());
            blockList.save();
            AkatZumaWorldEdit.VipBlockMap.remove(blockName);
        }
        else {
            blockList.set(set.stream().toList());
            blockList.save();
            AkatZumaWorldEdit.defaultBlockMap.remove(blockName);
        }

        Component component = Component.translatable("chat.akatzuma.success.del_viplayer");
        if(player!=null) AkatZumaWorldEdit.sendAkatMessage(Component.literal("")
                .append(component).withStyle(ChatFormatting.GREEN)
                .append(blockName), player);
        AkatZumaWorldEdit.LOGGER.info(component.getString() + blockName + ":" + num);
    }
}
