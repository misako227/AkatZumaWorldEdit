package com.z227.AkatZumaWorldEdit.utilities;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ClientCommandHandler;

public class SendCopyMessage {
    public static Component send(String message) {
        Component msg = new TranslatableComponent("chat.action.copy").append(new TextComponent(message).withStyle(ChatFormatting.GREEN));
        Component component = new TextComponent(message)
                .withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, msg))
                .withColor(ChatFormatting.GREEN)
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, message))
                );
        return component;

    }


    public static void sendSuccessMsg(BlockState blockState, Player player,String command) {
        Component blockName = blockState.getBlock().getName().withStyle(ChatFormatting.GREEN);
        Component setSuccess = new TranslatableComponent("chat.akatzuma.set.success").append(blockName).append(new TranslatableComponent("chat.akatzuma.undo.tip"));
        AkatZumaWorldEdit.sendClientMessage(setSuccess, player);

        Util.recordPosLog(blockState, player, command);
    }




    public static void sendCommand(String command){
        LocalPlayer Lplayer = Minecraft.getInstance().player;
        if (Lplayer != null) {
//            Lplayer.connection.sendCommand(command);
//            Lplayer.commandSigned(command, new TextComponent("[").append(AkatZumaWorldEdit.Akat));
            ClientCommandHandler.sendMessage("/" + command);
        }
    }
}
