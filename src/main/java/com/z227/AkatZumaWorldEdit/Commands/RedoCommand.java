package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.UndoBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.UndoData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class RedoCommand {

    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
            Commands.literal(AkatZumaWorldEdit.MODID)
                .then(Commands.literal("redo")
                    .executes((context)->{
                        redo(context);
                        return 1;
                    })
            )

    );
//        dispatcher.register(Commands.literal("a").redirect(cmd));


}

    public static void redo(CommandContext<CommandSourceStack> context){

        Player player = context.getSource().getPlayer();
        //此处判断上一次操作是否完成
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        ServerLevel serverlevel = context.getSource().getLevel();
        Component component;
        // 判断上次操作是否完成
        if (!PMD.isFlag()) {
            component = Component.translatable("chat.akatzuma.error.wait");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return;
        }
        // 设置标志位
        PMD.setFlag(false);

        UndoData redoMap  = PMD.getRedoDataMap().pop();
        component = Component.translatable("chat.akatzuma.error.not_redo");

        if(redoMap!=null){
            if(!redoMap.equalsLevel(serverlevel)){
                //todo 提示错误
                PMD.setFlag(true);
                return;
            }
            UndoBlock.redoSetBlock(serverlevel, redoMap, player);
            component = Component.translatable("chat.akatzuma.success_redo");
        }

        AkatZumaWorldEdit.sendAkatMessage(component, player);
        // 设置标志位 此处再把flag设为true
        PMD.setFlag(true);

    }
}