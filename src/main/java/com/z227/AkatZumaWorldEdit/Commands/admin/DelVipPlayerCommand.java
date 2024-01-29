package com.z227.AkatZumaWorldEdit.Commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

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
                                                            addVipPlayer(context);
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
}
