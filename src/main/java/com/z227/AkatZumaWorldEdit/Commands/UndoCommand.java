package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.UndoBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.UndoData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public class UndoCommand {

    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
            Commands.literal(AkatZumaWorldEdit.MODID)
                .then(Commands.literal("undo")
                    .executes((context)->{
                        undoContext(context);
                        return 1;
                    })
            )

    );
//        dispatcher.register(Commands.literal("a").redirect(cmd));


}

    public static void undoContext(CommandContext<CommandSourceStack> context) {
        Player player = context.getSource().getPlayer();
        ServerLevel serverlevel = context.getSource().getLevel();
        undo(player, serverlevel);
    }


    public static void undo(Player player, ServerLevel serverlevel) {

        //此处判断上一次操作是否完成
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

        Component component;
        // 判断上次操作是否完成
        if (!PMD.isFlag()) {
            component = Component.translatable("chat.akatzuma.error.wait");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return;
        }
        // 设置标志位
        PMD.setFlag(false);

//        Map<BlockPos,BlockState> undoMap  = PMD.getUndoDataMap().pop();
//        Map<BlockPos,BlockState> redoMap  = new HashMap<>();
        UndoData undoMap  = PMD.getUndoDataMap().peekFirst();
        UndoData redoMap  = new UndoData(serverlevel);

        component = Component.translatable("chat.akatzuma.error.not_undo");

        if(undoMap!=null){
            if(!undoMap.equalsLevel(serverlevel)){

                component = Component.translatable("chat.akatzuma.error.level_not_match");
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                PMD.setFlag(true);
                return;
            }
            PMD.getUndoDataMap().pop();
            PMD.getRedoDataMap().push(redoMap);

            AttributeInstance attribute = player.getAttribute(AkatZumaWorldEdit.SET_FLAG_ATTRIBUTE.get());
            boolean flag = false;
            if(attribute != null) {
                double v = attribute.getBaseValue();
                flag = v > 0;
            }
            UndoBlock.undoSetBlock(serverlevel,undoMap, redoMap, flag);
            component = Component.translatable("chat.akatzuma.success_undo").append("(" + undoMap.getUndoMap().size()+ ")").withStyle(ChatFormatting.DARK_PURPLE);
        }

        AkatZumaWorldEdit.sendAkatMessage(component, player);
        // 设置标志位 此处再把flag设为true
        PMD.setFlag(true);

    }
}