package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.UndoBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class UndoCommand {

    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
            Commands.literal(AkatZumaWorldEdit.MODID)
                .then(Commands.literal("undo")
                    .executes((context)->{
                        undo(context);
                        return 1;
                    })
            )

    );
//        dispatcher.register(Commands.literal("a").redirect(cmd));


}

    public static void undo(CommandContext<CommandSourceStack> context){

        Player player = null;
        try {
            player = context.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        //此处判断上一次操作是否完成
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        ServerLevel serverlevel = context.getSource().getLevel();
        Component component;
        // 判断上次操作是否完成
        if (!PMD.isFlag()) {
            component = new TranslatableComponent("chat.akatzuma.error.wait");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return;
        }
        // 设置标志位
        PMD.setFlag(false);

        Map<BlockPos,BlockState> undoMap  = PMD.getUndoDataMap().pop();
        Map<BlockPos,BlockState> redoMap  = new HashMap<>();

        component = new TranslatableComponent("chat.akatzuma.error.not_undo");

        if(undoMap!=null){
            PMD.getRedoDataMap().push(redoMap);
            UndoBlock.undoSetBlock(serverlevel,undoMap,redoMap);
            component = new TranslatableComponent("chat.akatzuma.success_undo").append("(" + undoMap.size()+ ")").withStyle(ChatFormatting.DARK_PURPLE);
        }

        AkatZumaWorldEdit.sendAkatMessage(component, player);
        // 设置标志位 此处再把flag设为true
        PMD.setFlag(true);

    }
}